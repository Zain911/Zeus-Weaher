package com.example.zeusweather.di

import com.example.zeusweather.data.local.AlertDao
import com.example.zeusweather.data.local.ILocalDataSource
import com.example.zeusweather.data.local.WeatherDao
import com.example.zeusweather.data.remote.RetrofitService
import com.example.zeusweather.data.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideMainRepository(
        retrofit: RetrofitService,
        weatherDao: WeatherDao,
        alertDao: AlertDao
    ): Repository {
        return Repository(retrofit, weatherDao,alertDao)
    }
}


