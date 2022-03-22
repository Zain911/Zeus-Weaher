package com.example.zeusweather.data.local

import androidx.room.*
import com.example.zeusweather.data.model.AlertModel
import kotlinx.coroutines.flow.Flow


@Dao
interface AlertDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insetAlert(alertModel: AlertModel):Long

    @Delete
    suspend fun deleteAlert(alertModel: AlertModel)

    @Query("SELECT * FROM alerts")
    fun getAlerts(): Flow<List<AlertModel>>
}