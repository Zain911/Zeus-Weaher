package com.example.zeusweather.data.repository

import com.example.zeusweather.data.local.AlertDao
import com.example.zeusweather.data.local.WeatherDao
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.data.remote.RetrofitService
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

@Module
@InstallIn(SingletonComponent::class)
class Repository(
    private var retrofitService: RetrofitService,
    private var weatherDao: WeatherDao,
    private var alertDao: AlertDao
) : RepositoryInterface {


    override suspend fun getCurrentWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String,
        lang: String
    ): Response<WeatherResponse> {
        retrofitService.getAllStatusWeatherByLatLon(lat, lon,lang).body().let {

            it?.timezone = currentCity
            it?.isFavourite = false
            if (it != null)
                weatherDao.insert(it)
        }
        return retrofitService.getAllStatusWeatherByLatLon(lat, lon, lang = lang)
    }

    override suspend fun updateFavouriteWeatherAPI(
        lat: Double,
        lon: Double,
        currentCity: String,
        lang:String
    ): Response<WeatherResponse> {
        retrofitService.getAllStatusWeatherByLatLon(lat, lon, lang = lang).body().let {

            it?.timezone = currentCity
            it?.isFavourite = true
            if (it != null)
                weatherDao.insert(it)
        }
        return retrofitService.getAllStatusWeatherByLatLon(lat, lon,lang=lang)
    }

    override suspend fun insertFavouriteWeatherAPI(lat: Double, lon: Double, currentCity: String,lang: String) {
        retrofitService.getAllStatusWeatherByLatLon(lat, lon, lang = lang).body().let {

            it?.timezone = currentCity
            it?.isFavourite = true
            if (it != null)
                weatherDao.insert(it)
        }
    }

    override suspend fun getCurrentWeatherAPIForWidget(
        lat: Double,
        lon: Double,
        lang: String
    ): Response<WeatherResponse> {
        return retrofitService.getAllStatusWeatherByLatLon(lat, lon,lang=lang)
    }

    override fun getStoredWeather(): Flow<WeatherResponse> = weatherDao.getCurrentWeatherResponse()

    override fun getWeatherForAlert(): WeatherResponse =
        weatherDao.getWeatherResponseAlert(false)



    override fun getFavouriteWeatherList(isFavourite: Boolean): Flow<List<WeatherResponse>> =
        weatherDao.getFavourite(isFavourite)


    override suspend fun insertWeather(weatherResponse: WeatherResponse) {
        weatherDao.insert(weatherResponse)
    }

    override suspend fun deleteWeatherByTimeZone(timeZone: String) {

        weatherDao.deleteWeatherByTimeZone(timeZone)
    }

    override suspend fun insertAlert(alert: AlertModel) =
        alertDao.insetAlert(alert)


    override suspend fun deleteAlert(alert: AlertModel) {
        alertDao.deleteAlert(alert)
    }

    override fun getAlerts(): Flow<List<AlertModel>> = alertDao.getAlerts()


}