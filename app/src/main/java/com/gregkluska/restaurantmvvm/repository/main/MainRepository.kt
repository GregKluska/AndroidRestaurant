package com.gregkluska.restaurantmvvm.repository.main

import android.util.Log
import androidx.lifecycle.LiveData
import com.gregkluska.restaurantmvvm.api.main.MainService
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.persistence.DishDao
import com.gregkluska.restaurantmvvm.persistence.DishOptionDao
import com.gregkluska.restaurantmvvm.repository.NetworkBoundResource
import com.gregkluska.restaurantmvvm.util.AbsentLiveData
import com.gregkluska.restaurantmvvm.util.ApiSuccessResponse
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import com.gregkluska.restaurantmvvm.util.Resource
import kotlinx.coroutines.Job
import javax.inject.Inject

private const val TAG = "AppDebug"

class MainRepository
@Inject
constructor(
    val mainService: MainService,
    val dishDao: DishDao,
    val dishOptionDao: DishOptionDao
)
{
    private var repositoryJob: Job? = null

    fun getDishes(): LiveData<Resource<List<Dish>>> {
        return object: NetworkBoundResource<List<Dish>, List<DishResponse>>(true, true) {
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<List<DishResponse>>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response.body}")

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

                return onCompleteJob(
                    Resource.Success(dishList)
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<DishResponse>>> {
                return mainService.getDishes()
            }

            override fun loadFromCache(): LiveData<List<Dish>> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<Dish>?) {

            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getDishesByCategory(query: String): LiveData<Resource<List<Dish>>> {
        return object: NetworkBoundResource<List<Dish>, List<DishResponse>>(true, true) {
            override suspend fun createCacheRequestAndReturn() {
                TODO("Not yet implemented")
            }

            override suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<List<DishResponse>>) {
                Log.d(TAG, "handleApiSuccessResponse: ${response.body}")

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

                return onCompleteJob(
                    Resource.Success(dishList)
                )
            }

            override fun createCall(): LiveData<GenericApiResponse<List<DishResponse>>> {
                return mainService.getDishesByCategory(query)
            }

            override fun loadFromCache(): LiveData<List<Dish>> {
                return AbsentLiveData.create()
            }

            override suspend fun updateLocalDb(cacheObject: List<Dish>?) {

            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

}