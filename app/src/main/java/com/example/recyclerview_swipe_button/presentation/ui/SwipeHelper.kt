package com.example.recyclerview_swipe_button.presentation.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Point
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview_swipe_button.utils.dpToPx
import java.util.HashMap
import java.util.LinkedList
import java.util.Queue

@SuppressLint("ClickableViewAccessibility")
abstract class SwipeHelper(
    private val context: Context,
    private val recyclerView: RecyclerView,
    private val buttonWidthDp: Int // largura do botão em dp
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    companion object {
        private const val DEFAULT_SWIPE_THRESHOLD = 0.3f
    }

    private var swipedPos: Int = -1
    private var swipeThreshold = DEFAULT_SWIPE_THRESHOLD
    private var buttons: MutableList<UnderlayButton> = mutableListOf()
    private val buttonsBuffer: MutableMap<Int, List<UnderlayButton>> = HashMap()
    private val recoverQueue: Queue<Int> = object : LinkedList<Int>() {
        override fun add(element: Int): Boolean {
            return if (contains(element)) false else super.add(element)
        }
    }

    private val gestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            buttons.forEach { button ->
                if (button.onClick(e.x, e.y)) return@forEach
            }
            return true
        }
    }
    private val gestureDetector = GestureDetector(context, gestureListener)

    private val onTouchListener = View.OnTouchListener { _, e ->
        if (swipedPos < 0) return@OnTouchListener false
        val point = Point(e.rawX.toInt(), e.rawY.toInt())

        recyclerView.findViewHolderForAdapterPosition(swipedPos)?.itemView?.let { swipedItem ->
            val rect = Rect()
            swipedItem.getGlobalVisibleRect(rect)

            if (e.action == MotionEvent.ACTION_DOWN ||
                e.action == MotionEvent.ACTION_UP ||
                e.action == MotionEvent.ACTION_MOVE) {
                if (rect.top < point.y && rect.bottom > point.y) {
                    gestureDetector.onTouchEvent(e)
                } else {
                    recoverQueue.add(swipedPos)
                    swipedPos = -1
                    recoverSwipedItem()
                }
            }
        }
        false
    }


    init {
        recyclerView.setOnTouchListener(onTouchListener)
        attachSwipe()
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val pos = viewHolder.adapterPosition

        if (swipedPos != pos) {
            recoverQueue.add(swipedPos)
        }
        swipedPos = pos

        buttons = buttonsBuffer[swipedPos]?.toMutableList() ?: mutableListOf()
        buttonsBuffer.clear()
        recoverSwipedItem()
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float = swipeThreshold

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float = 0.1f * defaultValue

    override fun getSwipeVelocityThreshold(defaultValue: Float): Float = 5.0f * defaultValue

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val pos = viewHolder.adapterPosition
        var translationX = dX
        val itemView = viewHolder.itemView

        if (pos < 0) {
            swipedPos = pos
            return
        }

        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE && dX < 0) {
            var buffer = mutableListOf<UnderlayButton>()
            if (!buttonsBuffer.containsKey(pos)) {
                instantiateUnderlayButton(viewHolder, buffer)
                buttonsBuffer[pos] = buffer
            } else {
                buffer = buttonsBuffer[pos]!!.toMutableList()
            }
            translationX = dX * buffer.size * context.dpToPx(buttonWidthDp).toInt() / itemView.width.toFloat()
            drawButtons(c, itemView, buffer, translationX)
        }

        super.onChildDraw(c, recyclerView, viewHolder, translationX, dY, actionState, isCurrentlyActive)
    }

    fun clearButtonsBuffer() {
        buttonsBuffer.clear()
    }

    private fun recoverSwipedItem() {
        while (recoverQueue.isNotEmpty()) {
            val pos = recoverQueue.poll()
            pos?.let {
                if (pos > -1) {
                    recyclerView.adapter?.notifyItemChanged(pos)
                }
            }
        }
    }

    fun resetSwipeState() {
        if (swipedPos != -1) {
            recoverQueue.add(swipedPos)
            swipedPos = -1
            recoverSwipedItem()
        }
    }

    private fun drawButtons(
        c: Canvas,
        itemView: View,
        buffer: List<UnderlayButton>,
        dX: Float
    ) {
        var right = itemView.right.toFloat()
        val dButtonWidth = (-dX) / buffer.size
        buffer.forEach { button ->
            val left = right - dButtonWidth
            button.onDraw(
                c,
                RectF(left, itemView.top.toFloat(), right, itemView.bottom.toFloat()),
                recyclerView.context
            )
            right = left
        }
    }

    fun attachSwipe() {
        val itemTouchHelper = ItemTouchHelper(this)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    abstract fun instantiateUnderlayButton(
        viewHolder: RecyclerView.ViewHolder,
        underlayButtons: MutableList<UnderlayButton>
    )
}