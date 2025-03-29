package com.example.recyclerview_swipe_button

import android.app.Application
import com.example.recyclerview_swipe_button.di.notificationModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AppModule: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@AppModule)
            androidLogger()
            modules(notificationModule)
        }
    }
}