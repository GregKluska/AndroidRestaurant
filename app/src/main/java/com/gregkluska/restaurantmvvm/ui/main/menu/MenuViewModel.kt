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
import com.gregkluska.restaurantmvvm.models.DishCategory
import com.gregkluska.restaurantmvvm.repository.main.MenuRepository
import com.gregkluska.restaurantmvvm.util.GenericApiResponse
import com.gregkluska.restaurantmvvm.util.Resource
import com.gregkluska.restaurantmvvm.util.Resource.*

private const val TAG = "AppDebug"

class MenuViewModel
@ViewModelInject
constructor(
    private val repository: MenuRepository
): ViewModel() {

    private val _viewState: MutableLiveData<ViewState> = MutableLiveData()
    private val _dishes: MediatorLiveData<Resource<List<Dish>>> = MediatorLiveData()
    private val _categories: MediatorLiveData<Resource<List<DishCategory>>> = MediatorLiveData()

    // query vars
    private var query: String? = "aaaa"

    val viewState: LiveData<ViewState>
        get() = _viewState

    val dishes: LiveData<Resource<List<Dish>>>
        get() = _dishes

    val categories: LiveData<Resource<List<DishCategory>>>
        get() = _categories


    fun executeCategories() {
        Log.d(TAG, "executeCategories: called")
        _viewState.value = ViewState.CATEGORIES

        val repositorySource = repository.getCategories()
        _categories.addSource(repositorySource) {
            when (it) {
                is Success -> {
                    Log.d(TAG, "executeSearch: Response: ${it.data}")
                    _categories.value = it
                    _categories.removeSource(repositorySource)
                }
                is Loading -> {
                    Log.e(TAG, "executeCategories: Loading..." )
                }

                is Error -> {
                    Log.e(TAG, "executeSearch: Error.")
                    _categories.removeSource(repositorySource)
                }
            }
        }
    }

    fun searchMenuItems(query: String) {
        this.query = query
        executeSearch()
    }

    private fun executeSearch() {
        Log.d(TAG, "executeSearch: called")
        query?.let{ categoryName ->
            _viewState.value = ViewState.DISHES
            val repositorySource = repository.getDishes(categoryName)
            _dishes.addSource(repositorySource) {
                when (it) {
                    is Success -> {
                        Log.d(TAG, "executeSearch: Response: ${it.data}")
                        _dishes.value = it
                        _dishes.removeSource(repositorySource)
                    }

                    is Loading -> {
                        Log.e(TAG, "executeSearch: Loading...")
                    }

                    is Error -> {
                        Log.e(TAG, "executeSearch: Error.")
                        _dishes.removeSource(repositorySource)
                    }
                }
            }
        }
    }

    companion object {
        enum class ViewState {CATEGORIES, DISHES}
    }
}