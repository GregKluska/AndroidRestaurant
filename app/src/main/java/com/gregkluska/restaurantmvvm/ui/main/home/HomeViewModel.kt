package com.gregkluska.restaurantmvvm.ui.main.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.repository.main.MenuRepository
import com.gregkluska.restaurantmvvm.util.Resource

class HomeViewModel
    @ViewModelInject
    constructor(
        private val repository: MenuRepository
    ): ViewModel() {

    fun getDishes() : LiveData<Resource<List<Dish>>> {
        return repository.getDishes()
    }


}