package com.example.zeusweather.util

import android.location.Geocoder
import android.media.AudioAttributes
import android.media.RingtoneManager
import androidx.core.content.ContentProviderCompat.requireContext
import java.util.*
import kotlin.coroutines.coroutineContext

object Constants {
    const val BASE_URL = "https://api.openweathermap.org"
    const val IMG_URL = "https://openweathermap.org/img/wn/"
    const val PERMISSION_REQUEST_ACCESS_LOCATION = 42

    const val ALERT_WEATHER_SPECS = "ALERT_WEATHER_SPECS"
    val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)



}