package com.example.zeusweather.util

import android.content.Context
import android.os.Build
import com.example.zeusweather.R
import android.content.Intent
import android.provider.Settings

object PermissionUtil {
    fun checkOverlayPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(context)) {
                val builder = WeatherUtil.TwoButtonsDialogBuilder(context,
                    context.resources.getString(R.string.EnableNotiDialog),
                    context.resources.getString(R.string.EnableNotiDialogContent),
                    context.resources.getString(R.string.yes),
                    context.resources.getString(R.string.close),
                    { dialogInterface, i ->
                        val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(myIntent)
                    }
                ) { dialogInterface, i -> dialogInterface.dismiss() }
                builder.show()
            }
        }
    }
}