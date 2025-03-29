package com.example.recyclerview_swipe_button.presentation

import android.content.res.ColorStateList
import com.example.recyclerview_swipe_button.databinding.ItemNotificationBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclerview_swipe_button.R
import com.example.recyclerview_swipe_button.domain.NotificationMessage

class NotificationAdapter(
    private val onClick: (NotificationMessage) -> Unit,
) : ListAdapter<NotificationMessage, NotificationAdapter.NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val task = getItem(position)
        holder.bind(task)
    }

    inner class NotificationViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(notification: NotificationMessage) {

            binding.titleNotification.text = notification.title
            binding.descNotification.text = notification.message
            binding.dateNotification.text = notification.date

            if(notification.isRead) {
                binding.notificationIndicator.apply {
                    val color = ContextCompat.getColor(context, R.color.neutralGray)
                    backgroundTintList = ColorStateList.valueOf(color)
                }
            }

            binding.notificationCard.setOnClickListener {
                onClick(notification)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationMessage>() {
            override fun areItemsTheSame(
                oldItem: NotificationMessage,
                newItem: NotificationMessage
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: NotificationMessage,
                newItem: NotificationMessage
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}