package com.example.zeusweather.util.notifier

import android.app.AlarmManager
import android.app.AlarmManager.INTERVAL_DAY
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.util.Constants
import dagger.hilt.android.AndroidEntryPoint

class AlertAlarmManager(
    context: Context?,
    alertModel: AlertModel
) {
    init {
        setAlarmSingle(context, alertModel)
        Log.d("TAG", "constractor alert manager: ")
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private fun setAlarmSingle(context: Context?, alertModel: AlertModel) {
        val intent = Intent(context, AlertNotificationReceiver::class.java)
        Log.d("TAG", "setAlarmSingle: id = ${alertModel.id}")
        Log.d("TAG", "setAlarmSingle: start = ${alertModel.startDate}")

        val intent1 = PendingIntent.getBroadcast(
            context,
            alertModel.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        alarmManager.setInexactRepeating (AlarmManager.RTC_WAKEUP,
            alertModel.startDate,
            INTERVAL_DAY ,
            intent1)
        Log.d("TAG", "alarmManager: ${alertModel.startDate}")
    }




}