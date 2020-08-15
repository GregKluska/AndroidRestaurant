package com.gregkluska.restaurantmvvm.ui.main.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.repository.main.MainRepository
import com.gregkluska.restaurantmvvm.util.GenericApiResponse

class HomeViewModel
    @ViewModelInject
    constructor(
        private val repository: MainRepository
    ): ViewModel() {

    fun getDishes() : LiveData<GenericApiResponse<List<DishResponse>>> {
        return repository.getDishes()
    }

}