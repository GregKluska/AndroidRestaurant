package com.gregkluska.restaurantmvvm.util

/**
 * A generic class that holds a value with its loading status.
 *
 * @source https://developer.android.com/jetpack/guide
 * @param <T>
</T> */
// A generic class that contains data and status about loading this data.
sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}