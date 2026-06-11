package com.newtieba

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * 应用程序类
 */
@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
            private set
    }
}
