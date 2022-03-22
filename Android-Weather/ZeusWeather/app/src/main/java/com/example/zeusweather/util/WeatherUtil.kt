package com.example.zeusweather.util

import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.appcompat.app.AlertDialog
import com.example.zeusweather.data.local.SharedPreferencesFactory
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object WeatherUtil {
    fun getHourAndMinute(timestamp: Long): String {


        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (timestamp * 1000)
        val format = SimpleDateFormat("hh:00 aaa")
        return format.format(calendar.time)
    }

    fun getHourAndMinuteAlert(timestamp: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = (timestamp * 1000)
        val format = SimpleDateFormat("hh:mm aaa")
        return format.format(calendar.time)
    }

    fun converterHourAndMinutes(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm a");
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun converterToDay(timestamp: Long): String {
        val formatter = DateTimeFormatter.ofPattern("MMM, dd");
        val instant = Instant.ofEpochMilli(timestamp)
        val date = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        return formatter.format(date)
    }

    fun dateFormat(milliSeconds: Int): String {
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTimeInMillis(milliSeconds.toLong() * 1000)
        var month = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
        var day = calendar.get(Calendar.DAY_OF_MONTH).toString()
        var year = calendar.get(Calendar.YEAR).toString()
        return day + "/ " + month//

    }

    fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }

    fun TwoButtonsDialogBuilder(
        context: Context?,
        title: String?,
        message: String?,
        positiveButtonText: String?,
        negativeButtonText: String?,
        positiveClick: DialogInterface.OnClickListener?,
        negativeClick: DialogInterface.OnClickListener?
    ): AlertDialog.Builder {
        val builder = AlertDialog.Builder(
            context!!
        )
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton(negativeButtonText, negativeClick)
        builder.setPositiveButton(positiveButtonText, positiveClick)
        return builder
    }

    fun getCurrentLanguage(context: Context): String {
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        val lang = sharedPreferencesFactory.getLanguage().toString()
        return lang
    }



}