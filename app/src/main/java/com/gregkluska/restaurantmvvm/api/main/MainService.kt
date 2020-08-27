package com.gregkluska.restaurantmvvm.api.main

import androidx.lifecycle.LiveData
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.models.DishCategory
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {

    @GET("dishes")
    fun getDishes(): LiveData<GenericApiResponse<List<DishResponse>>>

    @GET("dishes")
    fun getDishesByCategory(
        @Query("dish_categories.name_contains") categoryName: String
    ): LiveData<GenericApiResponse<List<DishResponse>>>

    @GET("dish-categories")
    fun getCategories(): LiveData<GenericApiResponse<List<DishCategory>>>

}