package com.example.zeusweather.ui.setting

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.example.zeusweather.activity.Main2Activity
import com.example.zeusweather.data.local.SharedPreferencesFactory
import java.util.*

class SettingViewModel : ViewModel() {

    fun saveLanguageInSharedPreferences(language: String, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setLanguage(language)
    }

    fun saveTemperatureUnitInSharedPreferences(unit: String, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setUnitsOfTemperature(unit)
    }

    fun saveWindSpeedUnitInSharedPreferences(unit: String, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setUnitsOfUnitOfWindSpeed(unit)
    }

    fun saveNotificationEnabledOrDisabledInSharedPreferences(unit: Boolean, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setNotificationEnabledOrDisabled(unit)
    }

    fun saveAlertEnabledOrDisabledInSharedPreferences(unit: Boolean, context: Context) {
        val spFactory = SharedPreferencesFactory(context)
        spFactory.setAlertEnabledOrDisabled(unit)
    }

    fun updateApplicationLanguage(language: String, activity: Activity) {

        val local = Locale(language)
        val res = activity.resources
        val dm = res.displayMetrics
        val config = res.configuration
        res.updateConfiguration(config, dm)
        val refresh = Intent(
            activity,
            Main2Activity::class.java
        )
        refresh.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(refresh)
    }

}