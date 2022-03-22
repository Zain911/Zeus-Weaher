package com.example.zeusweather

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.data.repository.Repository
import com.example.zeusweather.util.UnitsConverters
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@AndroidEntryPoint
class NewAppWidget : AppWidgetProvider() {
    private val job = SupervisorJob()
    val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    @Inject
    lateinit var repository: Repository


    override fun onReceive(context: Context, intent: Intent?) {
        super.onReceive(context, intent)
        val sharedPreferencesFactory = SharedPreferencesFactory(context)
        val lang = sharedPreferencesFactory.getLanguage()
        val latLng = sharedPreferencesFactory.getLatLng()
        var weatherResponse: Response<WeatherResponse>
        coroutineScope.launch {
            repository.getStoredWeather().collect {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val man = AppWidgetManager.getInstance(context)
                val ids = man.getAppWidgetIds(ComponentName(context, NewAppWidget::class.java))
                if (it != null) {
                    for (appWidgetId in ids) {
                        updateAppWidget(
                            context, appWidgetManager, appWidgetId, it
                        )
                    }
                }
            }
        }
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    weather: WeatherResponse
) {
    var unitsConverters = UnitsConverters(context)
    var temp =
        unitsConverters.returnTemperatureUsingUserFormat(weather.current.temp.toInt().toString())
    val views = RemoteViews(context.packageName, R.layout.new_app_widget)
    views.setTextViewText(R.id.appWidget_name_city_textview, weather.timezone)
    views.setTextViewText(
        R.id.appWidget_temperature_textview, temp)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}