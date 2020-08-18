package com.gregkluska.restaurantmvvm.ui.main.home

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.gregkluska.restaurantmvvm.api.main.responses.DishResponse
import com.gregkluska.restaurantmvvm.models.Dish
import com.gregkluska.restaurantmvvm.repository.main.MainRepository
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import com.gregkluska.restaurantmvvm.util.Resource
import com.gregkluska.restaurantmvvm.util.Resource.*

private const val TAG = "AppDebug"

class MenuViewModel
@ViewModelInject
constructor(
    private val repository: MainRepository
): ViewModel() {

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    private val _dishes: MediatorLiveData<Resource<List<Dish>>> = MediatorLiveData()

    // query vars
    private var query: String? = null

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dishes: LiveData<Resource<List<Dish>>>
        get() = _dishes

    fun setViewCategories() {
        _viewState.value = ViewState.CATEGORIES
    }

    fun searchMenuItems(query: String) {
        this.query = query
        executeSearch()
    }

    private fun executeSearch() {
        query?.let{ categoryName ->
            _viewState.value = ViewState.DISHES
            val repositorySource = repository.getDishesByCategory(categoryName)
            _dishes.addSource(repositorySource, Observer {
                when(it) {
                    is Success -> {
                        Log.d(TAG, "executeSearch: Success, query: ${this.query}")
                        Log.d(TAG, "executeSearch: Response: ${it.data}")
                        _dishes.value = it
                        _dishes.removeSource(repositorySource)
                    }

                    is Loading -> {
                        Log.e(TAG, "executeSearch: Loading..." )
                        _dishes.removeSource(repositorySource)
                    }

                    is Error -> {
                        Log.e(TAG, "executeSearch: Error." )
                        _dishes.removeSource(repositorySource)
                    }
                }
            })
        }
    }

    companion object {
        enum class ViewState {CATEGORIES, DISHES}
    }
}