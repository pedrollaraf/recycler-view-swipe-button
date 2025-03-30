package com.example.recyclerview_swipe_button

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview_swipe_button.databinding.ActivityMainBinding
import com.example.recyclerview_swipe_button.presentation.ui.NotificationAdapter
import com.example.recyclerview_swipe_button.presentation.viewmodel.NotificationViewModel
import com.example.recyclerview_swipe_button.presentation.ui.SwipeHelper
import com.example.recyclerview_swipe_button.presentation.ui.UnderlayButton
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NotificationViewModel by viewModel()

    private lateinit var swipeHelper: SwipeHelper

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
                swipeHelper.resetSwipeState()
            }
        )

        binding.rvNotifications.layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvNotifications.adapter = adapter


        viewModel.notificationsState.observe(this) { notificationState ->
            if (notificationState.isLoading) {
                binding.piLoading.visibility = View.VISIBLE
                binding.tvEmptyList.visibility = View.GONE
                binding.rvNotifications.visibility = View.GONE
            } else {
                binding.piLoading.visibility = View.GONE
                if (notificationState.notifications.isEmpty()) {
                    binding.tvEmptyList.visibility = View.VISIBLE
                    binding.rvNotifications.visibility = View.GONE
                } else {
                    binding.tvEmptyList.visibility = View.GONE
                    binding.rvNotifications.visibility = View.VISIBLE
                    adapter.submitList(notificationState.notifications)
                    swipeHelper.clearButtonsBuffer()
                }
            }
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
                    underlayButtons.add(
                        UnderlayButton(
                            text = if (notification.isRead) getString(R.string.mark_as_unread) else getString(
                                R.string.mark_as_read
                            ),
                            imageResId = if (notification.isRead) R.drawable.ic_notification_unread_light else R.drawable.ic_notification_read_light,
                            backgroundColor = ContextCompat.getColor(
                                this@MainActivity,
                                R.color.notificationBackgroundColor
                            ),
                            textColor = ContextCompat.getColor(
                                this@MainActivity,
                                R.color.notificationTextColor
                            ),
                            textSize = 14f,
                            textStyle = Typeface.BOLD,
                            iconSize = 16f,
                            iconTextSpacing = 8f,
                            clickListener = {
                                if (notification.isRead) {
                                    viewModel.markAsUnRead(notification)
                                } else {
                                    viewModel.markAsRead(notification)
                                }
                            }
                        )
                    )
                }
            }
        }

        binding.rvNotifications.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                swipeHelper.resetSwipeState()
            }
        })
    }
}