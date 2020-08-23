package com.gregkluska.restaurantmvvm.repository.main

import androidx.lifecycle.LiveData
import com.gregkluska.restaurantmvvm.api.main.MainService
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.util.AbsentLiveData
import com.gregkluska.restaurantmvvm.util.Resource
import kotlinx.coroutines.Job
import javax.inject.Inject

private const val TAG = "AppDebug"

class MainRepository
@Inject
constructor(
    val mainService: MainService
)
{
    private var repositoryJob: Job? = null

    fun getDishes(): LiveData<Resource<List<Dish>>> {
        return AbsentLiveData.create()
    }

    fun getDishesByCategory(query: String): LiveData<Resource<List<Dish>>> {
        return AbsentLiveData.create()
    }

}