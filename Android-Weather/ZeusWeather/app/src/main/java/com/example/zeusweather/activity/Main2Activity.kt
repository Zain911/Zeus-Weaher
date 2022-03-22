package com.example.zeusweather.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.thunder.util.ContextUtils
import com.example.zeusweather.R
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.databinding.ActivityMain2Binding
import com.example.zeusweather.util.PermissionUtil
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class Main2Activity : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    lateinit var sharedPreferencesFactory: SharedPreferencesFactory

    @SuppressLint("RestrictedApi")
    override fun attachBaseContext(newBase: Context) {
        // get chosen language from shread preference
        val shared = SharedPreferencesFactory(newBase)
        if (shared.getIsLangSet()) {
            val localeToSwitchTo = shared.getLanguage()
            val localeUpdatedContext: ContextWrapper =
                ContextUtils.updateLocale(newBase, Locale(localeToSwitchTo))
            super.attachBaseContext(localeUpdatedContext)
        } else {
            shared.setLanguage(Locale.getDefault().language)
            super.attachBaseContext(newBase)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main2)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favourite,
                R.id.navigation_alert,
                R.id.navigation_setting
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        PermissionUtil.checkOverlayPermission(this)

    }
}