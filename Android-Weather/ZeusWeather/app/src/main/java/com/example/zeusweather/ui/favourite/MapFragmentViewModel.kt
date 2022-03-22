package com.example.thunder.ui.favourites

import androidx.lifecycle.ViewModel
import com.example.zeusweather.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapFragmentViewModel @Inject constructor(private val repo: Repository) :
    ViewModel() {

    suspend fun insertFavLocation(lat: Double, lng: Double, currentCity: String,lang:String) =
        repo.insertFavouriteWeatherAPI(lat, lng, currentCity,lang)


}