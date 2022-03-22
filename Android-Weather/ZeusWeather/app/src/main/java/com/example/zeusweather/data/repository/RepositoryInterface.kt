package com.example.zeusweather.data.repository

import androidx.lifecycle.LiveData
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.model.WeatherResponse
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response


interface RepositoryInterface {

    suspend fun getCurrentWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String,
        lang: String
    ): Response<WeatherResponse>

    fun getStoredWeather(): Flow<WeatherResponse>
    fun getWeatherForAlert(): WeatherResponse
    fun getFavouriteWeatherList(isFavourite: Boolean): Flow<List<WeatherResponse>>
    suspend fun insertWeather(weatherResponse: WeatherResponse)
    suspend fun deleteWeatherByTimeZone(timeZone: String)
    suspend fun insertFavouriteWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String,
        lang: String
    )

    suspend fun getCurrentWeatherAPIForWidget(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<WeatherResponse>


    suspend fun updateFavouriteWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String,
        lang: String
    ): Response<WeatherResponse>

    suspend fun insertAlert(alert: AlertModel):Long
    suspend fun deleteAlert(alert: AlertModel)
    fun getAlerts(): Flow<List<AlertModel>>
}