package com.example.zeusweather.data.local

import android.content.Context
import android.content.SharedPreferences
import com.google.android.gms.maps.model.LatLng

class SharedPreferencesFactory(context: Context) {

    private val sharedPreferencesFileName = "SETTINGS"
    private val unitsOfTemperature = "UNITS_OF_TEMPERATURE"
    private val language = "LANGUAGE"
    private val unitsOfWindSpeed = "UNITS_OF_WIND_SPEED"
    private val latSavedName = "LAT"
    private val lngSavedName = "LNG"

    private val isLangaugeSet = "IS_LANGUAGE_SET"


    private val notificationEnabledOrDisabled = "NOTIFICATION_ENABLED_OR_DISABLED"
    private val alertDialogEnabledOrDisabled = "ALERT_DIALOG_ENABLED_OR_DISABLED"

    val sharedPreferencesVal: SharedPreferences =
        context.getSharedPreferences(sharedPreferencesFileName, Context.MODE_PRIVATE)
    var editor = sharedPreferencesVal.edit()

    fun getUnitsOfTemperature() =
        sharedPreferencesVal.getString(unitsOfTemperature, "K")

    fun setUnitsOfTemperature(unit: String) {
        editor.putString(unitsOfTemperature, unit)
        editor.commit()
    }


    fun setLatLng(latLng: LatLng) {
        editor.putString(latSavedName, latLng.latitude.toString())
        editor.putString(lngSavedName, latLng.longitude.toString())
        editor.commit()
    }

    fun getLatLng(): LatLng =
        LatLng(
            sharedPreferencesVal.getString(latSavedName, "-34.0")!!.toDouble(),
            sharedPreferencesVal.getString(lngSavedName, "151.0")!!.toDouble()
        )

    fun getIsLangSet() = sharedPreferencesVal.getBoolean(isLangaugeSet, false)

    fun getLanguage() =
        sharedPreferencesVal.getString(language, "")

    fun setLanguage(unit: String) {
        editor.putBoolean(isLangaugeSet, true)
        editor.putString(language, unit)
        editor.commit()
    }

    fun getUnitsOfWindSpeed() =
        sharedPreferencesVal.getString(unitsOfWindSpeed, "meter/sec")

    fun setUnitsOfUnitOfWindSpeed(unit: String) {
        editor.putString(unitsOfWindSpeed, unit)
        editor.commit()
    }

    fun getAlertEnabledOrDisabled() =
        sharedPreferencesVal.getBoolean(alertDialogEnabledOrDisabled, false)

    fun setAlertEnabledOrDisabled(unit: Boolean) {
        editor.putBoolean(alertDialogEnabledOrDisabled, unit)
        editor.commit()
    }

    fun getNotificationEnabledOrDisabled() =
        sharedPreferencesVal.getBoolean(notificationEnabledOrDisabled, false)

    fun setNotificationEnabledOrDisabled(unit: Boolean) {
        editor.putBoolean(notificationEnabledOrDisabled, unit)
        editor.commit()
    }


}