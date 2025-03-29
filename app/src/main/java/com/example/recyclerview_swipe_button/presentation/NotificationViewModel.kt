package com.example.recyclerview_swipe_button.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recyclerview_swipe_button.domain.NotificationMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NotificationViewModel: ViewModel() {

    init {
        getNotifications()
    }

    private val _notifications = MutableLiveData<List<NotificationMessage>>()
    val notifications: LiveData<List<NotificationMessage>> get() = _notifications

    fun markAsRead(notification: NotificationMessage) {
        val currentList = _notifications.value.orEmpty()
        val updatedList = currentList.map { item ->
            if (item.id == notification.id) {
                item.copy(isRead = true)
            } else {
                item
            }
        }
        _notifications.value = updatedList
    }

    fun markAsUnRead(notification: NotificationMessage) {
        val currentList = _notifications.value.orEmpty()
        val updatedList = currentList.map { item ->
            if (item.id == notification.id) {
                item.copy(isRead = false)
            } else {
                item
            }
        }
        _notifications.value = updatedList
    }



    private fun getNotifications() {
        viewModelScope.launch {
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
            _notifications.postValue(list)
        }
    }

}