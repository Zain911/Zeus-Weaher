package com.example.zeusweather.ui.favourite

import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.zeusweather.R
import com.example.zeusweather.activity.Main2Activity
import com.example.zeusweather.data.local.SharedPreferencesFactory
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun addFavouritePlace(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_navigation_favourite_to_navigation_map)

    }

    suspend fun makeRequest(lat: Double, lon: Double, currentCity: String,lang:String) =
        repository.updateFavouriteWeatherAPI(lat, lon, currentCity,lang)


    fun getFavouriteListFromRoom(isFavourite: Boolean) =
        repository.getFavouriteWeatherList(isFavourite)

    fun deleteWeatherObjectByTimeZone(timezone: String) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteWeatherByTimeZone(timezone)
        }
    }

    val _detailsSelectedItem = MutableLiveData<WeatherResponse>()

    val detailsSelectedItem: LiveData<WeatherResponse> = _detailsSelectedItem

    fun setDetailsForItemSelected(weatherResponse: WeatherResponse) {
        _detailsSelectedItem.postValue(weatherResponse)
    }



}