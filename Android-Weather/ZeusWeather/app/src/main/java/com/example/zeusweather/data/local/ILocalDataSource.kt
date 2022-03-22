package com.example.zeusweather.data.local

import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.model.WeatherResponse
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

interface ILocalDataSource {

    suspend fun insertWeather(weatherResponse: WeatherResponse)

    fun getWeatherData(): Flow<WeatherResponse>
    suspend fun deleteWeather(timeZone: String)
    fun getFavouriteWeatherData(isFavourite: Boolean): Flow<List<WeatherResponse>>

    suspend fun insertAlert(alert:AlertModel):Long
    suspend fun deleteAlert(alert: AlertModel)
    fun getAlerts() : Flow<List<AlertModel>>
}