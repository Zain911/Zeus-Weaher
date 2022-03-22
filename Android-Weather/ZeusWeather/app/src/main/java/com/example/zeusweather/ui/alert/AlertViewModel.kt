package com.example.zeusweather.ui.alert


import androidx.lifecycle.ViewModel
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlertViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    fun getAlerts(): Flow<List<AlertModel>> {
        return repository.getAlerts()
    }

    fun deleteAlert(alert: AlertModel) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteAlert(alert)
        }
    }


}