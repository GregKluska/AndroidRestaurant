package com.gregkluska.restaurantmvvm.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import com.gregkluska.restaurantmvvm.util.*
import com.gregkluska.restaurantmvvm.util.Constants.Companion.ERROR_UNKNOWN
import com.gregkluska.restaurantmvvm.util.Constants.Companion.NETWORK_TIMEOUT
import com.gregkluska.restaurantmvvm.util.Constants.Companion.TESTING_NETWORK_DELAY
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

private const val TAG = "AppDebug"

abstract class NetworkBoundResource<ResultType, RequestType> {

    private val result = MediatorLiveData<Resource<ResultType>>()
    protected lateinit var job: CompletableJob
    private lateinit var coroutineScope: CoroutineScope

    init {
        setJob(initNewJob())
        setValue(Resource.Loading(null))

        coroutineScope.launch {
            // simulate a network delay for testing
            delay(TESTING_NETWORK_DELAY)

            withContext(Main) { //MediatorLiveData has to be done in the Main thread
                val apiResponse = createCall()
                result.addSource(apiResponse, Observer {  response ->
                    Log.d(TAG, "mediatorld: called")
                    result.removeSource(apiResponse)
                    coroutineScope.launch {
                        handleNetworkCall(response)
                    }
                })

            }
        }

        GlobalScope.launch(IO) {
            delay(NETWORK_TIMEOUT)

            if (!job.isCompleted) {
                Log.e(TAG, "NetworkBoundResource: JOB NETWORK TIMEOUT.")
                job.cancel(CancellationException("UNABLE TO RESOLVE HOST"))
            }
        }


    }

    private fun setValue(newValue: Resource<ResultType>) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    private suspend fun handleNetworkCall(response: GenericApiResponse<RequestType>) {
        Log.d(TAG, "handleNetworkCall: called")
        when(response){
            is ApiSuccessResponse -> {
                handleApiSuccessResponse(response)
            }
            is ApiErrorResponse ->{
                Log.e(TAG, "NetworkBoundResource: ${response.errorMessage}")
                onErrorReturn(response.errorMessage)
            }
            is ApiEmptyResponse ->{
                Log.e(TAG, "NetworkBoundResource: Request returned NOTHING (HTTP 204).")
                onErrorReturn("HTTP 204. Returned NOTHING.")
            }
        }
    }

    fun onCompleteJob(data: Resource<ResultType>){
        GlobalScope.launch(Main) {
            job.complete()
            setValue(data)
        }
    }

    fun onErrorReturn(errorMessage: String?){
        var msg = errorMessage
        Log.e(TAG, "onErrorReturn: $msg" )

        if (msg == null) {
            msg = ERROR_UNKNOWN
        }

        onCompleteJob(Resource.Error(msg, null))
    }

    @OptIn(InternalCoroutinesApi::class)
    private fun initNewJob(): Job{
        Log.d(TAG, "initNewJob: called.")
        job = Job() // create new job
        job.invokeOnCompletion(onCancelling = true, invokeImmediately = true, handler = object: CompletionHandler{
            override fun invoke(cause: Throwable?) {
                if(job.isCancelled){
                    Log.e(TAG, "NetworkBoundResource: Job has been cancelled.")
                    cause?.let{
                        onErrorReturn(it.message)
                    }?: onErrorReturn("Unknown error.")
                }
                else if(job.isCompleted){
                    Log.e(TAG, "NetworkBoundResource: Job has been completed.")
                    // Do nothing? Should be handled already
                }
            }
        })
        coroutineScope = CoroutineScope(IO + job)
        return job
    }

    fun asLiveData() = result as LiveData<Resource<ResultType>>

    abstract suspend fun handleApiSuccessResponse(response: ApiSuccessResponse<RequestType>)

    abstract fun createCall(): LiveData<GenericApiResponse<RequestType>>

    abstract fun setJob(job: Job)
}