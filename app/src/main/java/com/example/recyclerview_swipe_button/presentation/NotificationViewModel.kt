package com.example.recyclerview_swipe_button.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recyclerview_swipe_button.domain.NotificationMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationViewModel: ViewModel() {

    private val _notificationsState = MutableLiveData<NotificationState>(NotificationState())
    val notificationsState: LiveData<NotificationState> get() = _notificationsState

    init {
        getNotifications()
    }

    fun markAsRead(notification: NotificationMessage) {
        val currentList = _notificationsState.value?.notifications.orEmpty()
        val updatedList = currentList.map { item ->
            if (item.id == notification.id) {
                item.copy(isRead = true)
            } else {
                item
            }
        }
        _notificationsState.value = notificationsState.value?.copy(
            notifications = updatedList
        )
    }

    fun markAsUnRead(notification: NotificationMessage) {
        val currentList = notificationsState.value?.notifications.orEmpty()
        val updatedList = currentList.map { item ->
            if (item.id == notification.id) {
                item.copy(isRead = false)
            } else {
                item
            }
        }
        _notificationsState.value = notificationsState.value?.copy(
            notifications = updatedList
        )
    }



    private fun getNotifications() {
        viewModelScope.launch {
            _notificationsState.value = notificationsState.value?.copy(
                isLoading = true
            )
            val list = listOf(
                NotificationMessage(
                    id = 1,
                    title = "Glad your day",
                    message = "Glad your day",
                    date = "29/03/2025 - 10:30 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 2,
                    title = "Nova Mensagem",
                    message = "Você tem uma nova mensagem não lida",
                    date = "29/03/2025 - 10:33 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 3,
                    title = "Você Sabia",
                    message = "Você sabia que o android está na versão xxxx",
                    date = "29/03/2025 - 10:49 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 4,
                    title = "Atualização do Sistema",
                    message = "Nova atualização disponível",
                    date = "29/03/2025 - 09:15 AM",
                    isRead = true
                )
            )

            delay(3000)
            _notificationsState.value = notificationsState.value?.copy(
                isLoading = false
            )
            _notificationsState.value = notificationsState.value?.copy(
                notifications = list
            )
        }
    }

}