package com.gregkluska.restaurantmvvm.di

import android.app.Application
import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.gregkluska.restaurantmvvm.R
import com.gregkluska.restaurantmvvm.api.main.MainService
import com.gregkluska.restaurantmvvm.util.Constants.Companion.BASE_URL
import com.gregkluska.restaurantmvvm.util.LiveDataCallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(gson: Gson, okHttpClient: OkHttpClient) : Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
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

}