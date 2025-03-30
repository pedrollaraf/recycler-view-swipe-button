package com.example.recyclerview_swipe_button.di

import com.example.recyclerview_swipe_button.presentation.viewmodel.NotificationViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val notificationModule = module {
    viewModelOf(::NotificationViewModel)
}