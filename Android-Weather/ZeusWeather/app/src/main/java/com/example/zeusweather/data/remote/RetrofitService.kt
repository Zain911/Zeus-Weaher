package com.example.zeusweather.data.remote

import android.icu.util.ULocale.getLanguage
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.data.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Inject

const val API_KEY = "c8fb7ff8299135714c6222d1cebd1e0e"

interface RetrofitService {

    @GET("/data/2.5/onecall?")
    suspend fun getAllStatusWeatherByLatLon(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") unit: String = "standard",
        @Query("lang") lang:String="ar",
        @Query("exclude") exclulde: String = "minutely",
        @Query("appid") appid: String = API_KEY
    ): Response<WeatherResponse>
}
