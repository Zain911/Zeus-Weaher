package com.example.zeusweather.ui.home

import androidx.lifecycle.ViewModel
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.data.repository.Repository
import com.example.zeusweather.util.WeatherUtil.getCurrentLanguage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {


    suspend fun makeRequest(lat: Double, lon: Double, currentCity: String,lang:String) =
        repository.getCurrentWeatherAPI(lat, lon,currentCity,lang)

    fun getDataFromRoom(): Flow<WeatherResponse> =
        repository.getStoredWeather()



}

