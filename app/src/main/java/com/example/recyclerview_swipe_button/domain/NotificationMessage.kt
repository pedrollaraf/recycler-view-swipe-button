package com.example.recyclerview_swipe_button.domain

data class NotificationMessage(
    val id: Int,
    val title: String,
    val message: String,
    val date: String,
    var isRead: Boolean = false
)