package com.example.zeusweather.ui.alert

import android.app.DatePickerDialog
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AddingAlertDialogViewModel @Inject constructor(
    val repository: Repository
) : ViewModel() {

    suspend fun insertAlert(alert: AlertModel) =
        repository.insertAlert(alert)





}