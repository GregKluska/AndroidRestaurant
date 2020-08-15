package com.gregkluska.restaurantmvvm.api.main

import androidx.lifecycle.LiveData
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import retrofit2.http.GET

interface MainService {

    @GET("dishes")
    fun getDishes(): LiveData<GenericApiResponse<List<DishResponse>>>

}