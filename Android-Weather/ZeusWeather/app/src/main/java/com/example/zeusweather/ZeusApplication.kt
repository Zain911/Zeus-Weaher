package com.example.zeusweather

import android.app.Application
import com.example.zeusweather.data.local.SharedPreferencesFactory
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ZeusApplication : Application() {
    lateinit var sharedPreferencesFactory: SharedPreferencesFactory
    override fun onCreate() {
        super.onCreate()

    }

}