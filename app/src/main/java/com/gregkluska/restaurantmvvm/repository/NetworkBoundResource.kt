package com.gregkluska.restaurantmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.gregkluska.restaurantmvvm.util.*
import com.gregkluska.restaurantmvvm.util.Constants.Companion.NETWORK_TIMEOUT
import com.gregkluska.restaurantmvvm.util.Constants.Companion.TESTING_CACHE_DELAY
import com.gregkluska.restaurantmvvm.util.Constants.Companion.TESTING_NETWORK_DELAY
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

/**
 * NetworkBoundResource. I've edited it a bit to work with my app
 *
 * @source https://github.com/mitchtabian/Open-API-Android-App/blob/NetworkBoundResource-for-MVI-and-Coroutines/app/src/main/java/com/codingwithmitch/openapi/repository/NetworkBoundResource.kt
 */
abstract class NetworkBoundResource<CacheObject, RequestObject>
    (
    isNetworkAvailable: Boolean, //is there a network connection?
    shouldLoadFromCache: Boolean // should the cached data be loaded
){

    private val TAG: String = "AppDebug"

    protected val result = MediatorLiveData<Resource<CacheObject>>()
    protected lateinit var job: CompletableJob
    protected lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(Resource.Loading(data = null))

        if(shouldLoadFromCache){
            // view cache to start
            val dbSource = loadFromCache()
            result.addSource(dbSource){
                result.removeSource(dbSource)
                setValue(Resource.Loading(data = it))
            }
        }

        if (isNetworkAvailable) {
            doNetworkRequest()
        } else {
            doCacheRequest()
        }
    }

    private fun doCacheRequest(){
        coroutineScope.launch {
            delay(TESTING_CACHE_DELAY)
            // View data from cache only and return
            createCacheRequestAndReturn()
        }
    }

    private fun doNetworkRequest(){
        coroutineScope.launch {

            // simulate a network delay for testing
            delay(TESTING_NETWORK_DELAY)

            withContext(Main){

                // make network call
                val apiResponse = createCall()
                result.addSource(apiResponse){ response ->
                    result.removeSource(apiResponse)

                    coroutineScope.launch {
                        handleNetworkCall(response)
                    }
                }
            }
        }

        GlobalScope.launch(IO){
            delay(NETWORK_TIMEOUT)

            if(!job.isCompleted){
                Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT." )
                job.cancel(CancellationException("Unable to resolve host"))
            }
        }
    }

    private suspend fun handleNetworkCall(response: GenericApiResponse<RequestObject>?) {
        when(response){
            is ApiSuccessResponse ->{
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse ->{
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}" )
//                onErrorReturn(response.errorMessage, true, false)
            }
            is ApiEmptyResponse ->{
                Log.e(TAG, "NetworkBoundResource: Request returned NOTHING (HTTP 204)" )
//                onErrorReturn("HTTP 204. Returned nothing.", true, false)
            }
        }
    }

    fun onCompleteJob(value: Resource<CacheObject>){
        GlobalScope.launch(Main){
            job.complete()
            setValue(value)
        }
    }

    private fun setValue(value: Resource<CacheObject>) {
        if(result.value != value) {
            result.value = value
        }
    }

//    fun onErrorReturn(errorMessage: String?, shouldUseDialog: Boolean, shouldUseToast: Boolean){
//        var msg = errorMessage
//        var useDialog = shouldUseDialog
//        var responseType: ResponseType = ResponseType.None()
//        if(msg == null){
//            msg = ERROR_UNKNOWN
//        }
//        else if(ErrorHandling.isNetworkError(msg)){
//            msg = ERROR_CHECK_NETWORK_CONNECTION
//            useDialog = false
//        }
//        if(shouldUseToast){
//            responseType = ResponseType.Toast()
//        }
//        if(useDialog){
//            responseType = ResponseType.Dialog()
//        }
//
//        onCompleteJob(Resource.Error(
//
//            response = Response(
//                message = msg,
//                responseType = responseType
//            )
//        ))
//    }

    @OptIn(InternalCoroutinesApi::class)
    private fun initNewJob(): Job {
        Log.d(TAG, "initNewJob: called...")
        job = Job()
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object : CompletionHandler{

            override fun invoke(cause: Throwable?) {
                if(job.isCancelled){
                    Log.e(TAG, "NetworkBoundResource: Job has been cancelled." )
//                    cause?.let{
//                        onErrorReturn(it.message, false, true)
//                    }?: onErrorReturn("UNKNOWN ERROR", false, true)
                }
                else if(job.isCompleted){
                    Log.e(TAG, "NetworkBoundResource: Job has been completed...")
                    // Do nothing. Should be handled already.
                }
            }

        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<Resource<CacheObject>>

    abstract suspend fun createCacheRequestAndReturn()

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RequestObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<RequestObject>>

    abstract fun loadFromCache(): LiveData<CacheObject>

    abstract suspend fun updateLocalDb(cacheObject: CacheObject?)

    abstract fun setJob(job: Job)
}