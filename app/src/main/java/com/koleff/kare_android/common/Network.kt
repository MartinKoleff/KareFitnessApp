package com.koleff.kare_android.common

import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

object Network {
    private const val MAX_RETRY_COUNT = 1

    suspend fun <T> executeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T,
        unsuccessfulRetriesCount: Int = 0
    ): Flow<ResultWrapper<T>> where T : ServerResponseData = flow {
        try {
            emit(ResultWrapper.Loading())

            val apiResult = apiCall.invoke()

            if (apiResult.isSuccessful) {
                emit(ResultWrapper.Success(apiResult))
            } else {
                emit(
                    ResultWrapper.ApiError(
                        apiResult.error,
                        apiResult
                    )
                )
            }
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            doRetryCall(
                dispatcher,
                apiCall,
                null,
                unsuccessfulRetriesCount
            )
        }
    }.flowOn(dispatcher)

    private suspend fun <T> doRetryCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T,
        apiResult: T?,
        unsuccessfulRetriesCount: Int = 0
    ): Flow<ResultWrapper<T>> where T : ServerResponseData = flow {
        if (unsuccessfulRetriesCount < MAX_RETRY_COUNT) {
            executeApiCall(dispatcher, apiCall, unsuccessfulRetriesCount + 1)
        } else {
            emit(
                ResultWrapper.ApiError(
                    apiResult?.error,
                    apiResult
                )
            )
        }
    }
}