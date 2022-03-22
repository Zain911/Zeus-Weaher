package com.example.zeusweather.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.zeusweather.R
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.example.zeusweather.activity.Main2Activity
import com.example.zeusweather.data.local.SharedPreferencesFactory
import java.util.*

class SplashActivity : AppCompatActivity() {
    lateinit var sharedPreferencesFactory: SharedPreferencesFactory
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportActionBar != null) supportActionBar!!.hide()
        setContentView(R.layout.activity_splash)
        sharedPreferencesFactory = SharedPreferencesFactory(this)
        val lang = Locale.getDefault().getLanguage()
        sharedPreferencesFactory.setLanguage(lang)

        Log.d("Splash", "onCreate: $lang")
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(
                Intent(this, Main2Activity::class.java).addFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                )
            )
        }, 3000)
    }
}