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
                ),
                // Itens adicionais:
                NotificationMessage(
                    id = 5,
                    title = "Bem-vindo!",
                    message = "Bem-vindo ao nosso aplicativo",
                    date = "29/03/2025 - 10:50 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 6,
                    title = "Oferta Especial",
                    message = "Confira nossa oferta exclusiva",
                    date = "29/03/2025 - 11:00 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 7,
                    title = "Alerta de Segurança",
                    message = "Atualize sua senha para maior segurança",
                    date = "29/03/2025 - 11:05 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 8,
                    title = "Convite",
                    message = "Você foi convidado para um evento especial",
                    date = "29/03/2025 - 11:15 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 9,
                    title = "Lembrete",
                    message = "Não esqueça da reunião agendada para hoje",
                    date = "29/03/2025 - 11:20 AM",
                    isRead = true
                ),
                NotificationMessage(
                    id = 10,
                    title = "Mensagem de Aniversário",
                    message = "Feliz aniversário! Aproveite seu dia",
                    date = "29/03/2025 - 11:30 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 11,
                    title = "Alerta de Atualização",
                    message = "Nova atualização crítica disponível",
                    date = "29/03/2025 - 11:35 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 12,
                    title = "Novidade",
                    message = "Novos recursos foram adicionados ao app",
                    date = "29/03/2025 - 11:40 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 13,
                    title = "Confirmação",
                    message = "Seu pedido foi confirmado com sucesso",
                    date = "29/03/2025 - 11:45 AM",
                    isRead = true
                ),
                NotificationMessage(
                    id = 14,
                    title = "Desconto Exclusivo",
                    message = "Aproveite um desconto exclusivo para você",
                    date = "29/03/2025 - 11:50 AM",
                    isRead = false
                ),
                NotificationMessage(
                    id = 15,
                    title = "Alerta de Mensagem",
                    message = "Você recebeu uma nova mensagem",
                    date = "29/03/2025 - 11:55 AM",
                    isRead = false
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