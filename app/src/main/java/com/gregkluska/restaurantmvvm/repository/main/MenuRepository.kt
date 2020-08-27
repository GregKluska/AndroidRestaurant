package com.gregkluska.restaurantmvvm.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.gregkluska.restaurantmvvm.api.main.MainService
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.models.DishCategory
import com.gregkluska.restaurantmvvm.repository.NetworkBoundResource
import com.gregkluska.restaurantmvvm.util.AbsentLiveData
import com.gregkluska.restaurantmvvm.util.ApiSuccessResponse
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import com.gregkluska.restaurantmvvm.util.Resource
import kotlinx.coroutines.Job
import javax.inject.Inject

private const val TAG = "AppDebug"

class MenuRepository
@Inject
constructor(
    val mainService: MainService
)
{

    private var repositoryJob: Job? = null

    fun getDishes(categoryName: String? = null): LiveData<Resource<List<Dish>>> {
        return object: NetworkBoundResource<List<Dish>, List<DishResponse>>(){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<List<DishResponse>>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")
                val dishList: ArrayList<Dish> = ArrayList()
                for(dishResponse in response.body) {
                    dishList.add(
                        Dish(
                            id = dishResponse.id,
                            name = dishResponse.name,
                            description = dishResponse.description,
                            image = dishResponse.image
                        )
                    )
                }

                onCompleteJob(
                    Resource.Success(dishList)
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<DishResponse>>> {
                return categoryName?.let { category ->
                    mainService.getDishesByCategory(category)
                }?:mainService.getDishes()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getCategories(): LiveData<Resource<List<DishCategory>>> {
        return object: NetworkBoundResource<List<DishCategory>, List<DishCategory>>(){
            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<List<DishCategory>>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response}")
                onCompleteJob(
                    Resource.Success(response.body)
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<DishCategory>>> {
                return mainService.getCategories()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

}