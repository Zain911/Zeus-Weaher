package com.example.zeusweather.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "Alerts")
data class AlertModel(
    @PrimaryKey(autoGenerate = true)
    var id: Long,
    @ColumnInfo(name = "startDate")
    var startDate: Long,
    @ColumnInfo(name = "endDate")
    var endDate: Long,
    @ColumnInfo(name = "fireAlertTime")
    var fireAlertTime: Long,
    @ColumnInfo(name = "isNotification")
    var isNotification: Boolean
)
