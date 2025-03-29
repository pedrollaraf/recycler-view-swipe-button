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
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NotificationViewModel by viewModel()

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


        viewModel.notificationsState.observe(this) { notificationState ->
            if(notificationState.isLoading) {
                binding.piLoading.visibility = View.VISIBLE
                binding.tvEmptyList.visibility = View.GONE
                binding.rvNotifications.visibility = View.GONE
            } else {
                binding.piLoading.visibility = View.GONE
                if(notificationState.notifications.isEmpty()) {
                    binding.tvEmptyList.visibility = View.VISIBLE
                    binding.rvNotifications.visibility = View.GONE
                } else {
                    binding.tvEmptyList.visibility = View.GONE
                    binding.rvNotifications.visibility = View.VISIBLE
                }
                adapter.submitList(notificationState.notifications)
            }
        }
    }
}