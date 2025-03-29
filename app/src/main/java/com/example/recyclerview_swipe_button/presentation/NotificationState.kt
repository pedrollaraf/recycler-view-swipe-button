package com.example.recyclerview_swipe_button.presentation

import com.example.recyclerview_swipe_button.domain.NotificationMessage

data class NotificationState(
    val notifications: List<NotificationMessage> = emptyList<NotificationMessage>(),
    val isLoading: Boolean = false
)