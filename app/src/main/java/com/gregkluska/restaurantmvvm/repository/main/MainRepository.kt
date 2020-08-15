package com.gregkluska.restaurantmvvm.repository.main

import androidx.lifecycle.LiveData
import com.gregkluska.restaurantmvvm.api.main.MainService
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import javax.inject.Inject

class MainRepository
@Inject
constructor(
    val mainService: MainService
)
{
    fun getDishes(): LiveData<GenericApiResponse<List<DishResponse>>> {
        return mainService.getDishes()
    }

}