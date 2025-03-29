package com.example.recyclerview_swipe_button

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview_swipe_button.databinding.ActivityMainBinding
import com.example.recyclerview_swipe_button.presentation.NotificationAdapter
import com.example.recyclerview_swipe_button.presentation.NotificationViewModel
import com.example.recyclerview_swipe_button.presentation.SwipeHelper
import com.example.recyclerview_swipe_button.presentation.UnderlayButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NotificationViewModel by viewModel()
    private lateinit var swipeHelper: SwipeHelper

    @SuppressLint("ClickableViewAccessibility", "NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val adapter = NotificationAdapter(
            onClick = { notification ->
                viewModel.markAsRead(notification)
            }
        )

        binding.rvNotifications.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvNotifications.adapter = adapter


        viewModel.notifications.observe(this) { notifications ->
            if(notifications.isEmpty()) {
                binding.tvEmptyList.visibility = View.VISIBLE
                binding.rvNotifications.visibility = View.GONE
            } else {
                binding.tvEmptyList.visibility = View.GONE
                binding.rvNotifications.visibility = View.VISIBLE
            }
            swipeHelper.resetSwipeState()
            adapter.submitList(notifications)
        }

        swipeHelper = object : SwipeHelper(
            context = this@MainActivity,
            recyclerView = binding.rvNotifications,
            buttonWidthDp = 100
        ) {
            override fun instantiateUnderlayButton(
                viewHolder: RecyclerView.ViewHolder,
                underlayButtons: MutableList<UnderlayButton>
            ) {
                val position = viewHolder.adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val notification = adapter.currentList[position]
                    notification.isRead.let { isRead ->
                        if (isRead) {
                            underlayButtons.add(
                                UnderlayButton(
                                    text = getString(R.string.mark_as_unread),
                                    imageResId = R.drawable.ic_notification_unread_light,
                                    backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.notificationBackgroundColor),
                                    textColor = ContextCompat.getColor(this@MainActivity,R.color.notificationTextColor),
                                    textSize = 14f,
                                    textStyle = Typeface.BOLD,
                                    iconSize = 16f,
                                    iconTextSpacing = 8f,
                                    clickListener = {
                                        viewModel.markAsUnRead(notification)
                                    }
                                )
                            )
                        } else {
                            underlayButtons.add(
                                UnderlayButton(
                                    text = getString(R.string.mark_as_read),
                                    imageResId = R.drawable.ic_notification_read_light,
                                    backgroundColor = ContextCompat.getColor(this@MainActivity, R.color.notificationBackgroundColor),
                                    textColor = ContextCompat.getColor(this@MainActivity,R.color.notificationTextColor),
                                    textSize = 14f,
                                    textStyle = Typeface.BOLD,
                                    iconSize = 16f,
                                    iconTextSpacing = 8f,
                                    clickListener = {
                                        viewModel.markAsRead(notification)
                                    }
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}