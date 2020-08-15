package com.gregkluska.restaurantmvvm.di.Main

import com.gregkluska.restaurantmvvm.api.main.MainService
import com.gregkluska.restaurantmvvm.persistence.AppDatabase
import com.gregkluska.restaurantmvvm.persistence.DishDao
import com.gregkluska.restaurantmvvm.persistence.DishOptionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import retrofit2.Retrofit

@Module
@InstallIn(ActivityRetainedComponent::class)
object MainModule {

    @Provides
    fun provideMainService(retrofitBuilder: Retrofit.Builder): MainService {
        return retrofitBuilder
            .build()
            .create(MainService::class.java)
    }

}