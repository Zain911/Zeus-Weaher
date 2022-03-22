package com.example.zeusweather.data.local

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.model.Current
import com.example.zeusweather.data.model.WeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    appDataBase: AppDataBase,

    ) : ILocalDataSource {
    var weatherDao: WeatherDao
    var alertDao: AlertDao

    init {
        weatherDao = appDataBase.weatherDao()
        alertDao = appDataBase.alertDao()
    }

    override suspend fun insertWeather(weatherResponse: WeatherResponse) {

        CoroutineScope(Dispatchers.IO).launch {
            weatherDao.insert(weatherResponse)

        }
    }

    override fun getWeatherData(): Flow<WeatherResponse> {
        return weatherDao.getCurrentWeatherResponse()


    }

    override suspend fun deleteWeather(timeZone: String) {
        weatherDao.deleteWeatherByTimeZone(timeZone)
    }

    override fun getFavouriteWeatherData(isFavourite: Boolean): Flow<List<WeatherResponse>> =
        weatherDao.getFavourite(isFavourite)

    override suspend fun insertAlert(alert: AlertModel):Long =
        alertDao.insetAlert(alert)



    override suspend fun deleteAlert(alert: AlertModel) {
        alertDao.deleteAlert(alert)
    }

    override fun getAlerts(): Flow<List<AlertModel>> = alertDao.getAlerts()

}