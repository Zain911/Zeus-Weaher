package com.example.zeusweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.zeusweather.data.model.WeatherResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {

    @Query("SELECT * FROM ZeusWeather WHERE isFavourite=:isFavourite")
    fun getCurrentWeatherResponse(isFavourite: Boolean=false): Flow<WeatherResponse>

    @Query("SELECT * FROM ZeusWeather  WHERE isFavourite=:isFavourite")
    fun getWeatherResponseAlert(isFavourite: Boolean=false): WeatherResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weather: WeatherResponse)

    @Query("SELECT * FROM ZeusWeather WHERE isFavourite=:isFavourite")
    fun getFavourite(isFavourite: Boolean=true):Flow<List<WeatherResponse>>

    @Query("DELETE FROM ZeusWeather WHERE timezone = :timezone")
    suspend fun deleteWeatherByTimeZone(timezone:String)


}