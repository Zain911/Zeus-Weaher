package com.example.zeusweather.data.local

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherforcast.data.roomdb.Converters
import com.example.zeusweather.data.model.AlertModel
import com.example.zeusweather.data.model.Weather
import com.example.zeusweather.data.model.WeatherResponse
import com.example.zeusweather.data.repository.RepositoryInterface
import com.example.zeusweather.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [WeatherResponse::class,
                     AlertModel::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDataBase : RoomDatabase() {

    abstract fun weatherDao(): WeatherDao
    abstract fun alertDao() : AlertDao
    class Callback @Inject constructor(
        private val dataBase: Provider<AppDataBase>,
        @AppModule.ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            val dao = dataBase.get().weatherDao()
            applicationScope.launch {
                // dao.insert()
            }
        }

    }

}
