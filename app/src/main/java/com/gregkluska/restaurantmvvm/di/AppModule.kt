package com.gregkluska.restaurantmvvm.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregkluska.restaurantmvvm.R
import com.gregkluska.restaurantmvvm.persistence.AppDatabase
import com.gregkluska.restaurantmvvm.persistence.AppDatabase.Companion.DATABASE_NAME
import com.gregkluska.restaurantmvvm.persistence.DishDao
import com.gregkluska.restaurantmvvm.persistence.DishOptionDao
import com.gregkluska.restaurantmvvm.util.Constants.Companion.BASE_URL
import com.gregkluska.restaurantmvvm.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(gson: Gson) : Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideRequestOptions(): RequestOptions {
        return RequestOptions
            .placeholderOf(R.drawable.default_image)
            .error(R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext app: Context, requestOptions: RequestOptions): RequestManager {
        return Glide.with(app)
            .setDefaultRequestOptions(requestOptions)
    }

    @Singleton
    @Provides
    fun provideAppDb(app: Application): AppDatabase {
        return Room.databaseBuilder(app, AppDatabase::class.java, DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideDishDao(db: AppDatabase): DishDao {
        return db.getDishDao()
    }

    @Singleton
    @Provides
    fun provideDishOptionDao(db: AppDatabase): DishOptionDao {
        return db.getDishOptionDao()
    }

}