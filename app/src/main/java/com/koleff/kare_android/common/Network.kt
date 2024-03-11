package com.koleff.kare_android.common

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
object Network {
    private const val MAX_RETRY_COUNT = 1

    suspend fun <T> executeApiCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T,
        unsuccessfulRetriesCount: Int = 0
    ): Flow<ResultWrapper<T>> where T : ServerResponseData = flow {
        try {
            emit(ResultWrapper.Loading())

            Log.d("Network", "Network request sent.")
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

            val error = when (throwable) {
                is HttpException -> KareError.NETWORK
                else -> KareError.GENERIC
            }
            emitAll(doRetryCall(dispatcher, apiCall, error, unsuccessfulRetriesCount))
        }
    }.flowOn(dispatcher)

    private suspend fun <T> doRetryCall(
        dispatcher: CoroutineDispatcher,
        apiCall: suspend () -> T,
        error: KareError,
        unsuccessfulRetriesCount: Int = 0
    ): Flow<ResultWrapper<T>> where T : ServerResponseData {
        Log.d("Network", "Network request failed. Retrying...")

        return if (unsuccessfulRetriesCount < MAX_RETRY_COUNT) {
            executeApiCall(dispatcher, apiCall, unsuccessfulRetriesCount + 1)
        } else {
            Log.d(
                "Network",
                "Network request failed. Error: $error."
            )

            flowOf(
                ResultWrapper.ApiError(
                    error,
                    null
                )
            )
        }
    }
}