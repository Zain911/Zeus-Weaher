package com.example.zeusweather.util

import android.content.Context
import com.example.zeusweather.data.local.SharedPreferencesFactory

class UnitsConverters(var context: Context) {

    fun returnTemperatureUsingUserFormat(value: String): String {
        var result = value
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        val units = sharedPreferencesFactory.getUnitsOfTemperature()
        val lang = sharedPreferencesFactory.getLanguage()
        when {
            units.equals("K") -> {
                result = "${value.toInt()} \u212A"
            }
            units.equals("C") -> {
                val celsius: Float = value.toFloat() - 273.15f
                result = "${celsius.toInt()} \u2103"
            }
            units.equals("F") -> {
                val fah = 1.8 * (value.toFloat() - 273) + 32
                result = "${fah.toInt()} \u2109"
            }
        }
        if (lang.equals("ar"))
            return convertToArabic(result)
        return result
    }

    fun returnWindSpeedUsingUserFormat(value: String): String {
        var result = value
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        val lang = sharedPreferencesFactory.getLanguage()
        val units = sharedPreferencesFactory.getUnitsOfWindSpeed()
        if (units.equals("meter/sec")) {
            result = "$value m/s"
        } else if (units.equals("miles/hour")) {
            val mph = value.toFloat() * 2.23694
            result = "${mph.toInt()} mi/h"
        }
        if (lang.equals("ar"))
            return convertToArabic(result)
        return result
    }

    fun convertToArabic(value: String): String {
        return (value + "")
            .replace("1".toRegex(), "١").replace("2".toRegex(), "٢")
            .replace("3".toRegex(), "٣").replace("4".toRegex(), "٤")
            .replace("5".toRegex(), "٥").replace("6".toRegex(), "٦")
            .replace("7".toRegex(), "٧").replace("8".toRegex(), "٨")
            .replace("9".toRegex(), "٩").replace("0".toRegex(), "٠")
    }


}