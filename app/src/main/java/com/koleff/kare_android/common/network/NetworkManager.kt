package com.koleff.kare_android.common.network

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.broadcast.RegenerateTokenNotifier
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

class NetworkManager @Inject constructor(
//    private val broadcastManager: LocalBroadcastManager,
//    private val regenerateTokenNotifier: RegenerateTokenNotifier
) {
    @Inject
    lateinit var broadcastManager: LocalBroadcastManager

    @Inject
    lateinit var regenerateTokenNotifier: RegenerateTokenNotifier

    companion object {
        private const val MAX_RETRY_COUNT = 1
    }

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
                when (apiResult.error) {

                    //Regenerate token
                    KareError.TOKEN_EXPIRED -> {
                        regenerateToken {

                            //Retry API call
                            emitAll(
                                doRetryCall(dispatcher, apiCall, apiResult.error, unsuccessfulRetriesCount)
                            )
                        }
                        return@flow
                    }

                    //Logout
                    KareError.INVALID_TOKEN -> {
                        logout()
                        return@flow
                    }

                    //Default case
                    else -> {
                        emit(
                            ResultWrapper.ApiError(
                                apiResult.error,
                                apiResult
                            )
                        )
                    }
                }
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

    private fun logout() {
        Log.d("Network", "Invalid token. Logging out.")

        val intent = Intent(Constants.ACTION_LOGOUT)
        broadcastManager.sendBroadcast(intent)
    }

    private suspend fun regenerateToken(onSuccess: suspend () -> Unit) {
        Log.d("Network", "Token regenerating...")

        val intent = Intent(Constants.ACTION_REGENERATE_TOKEN)
        broadcastManager.sendBroadcast(intent)

        regenerateTokenNotifier.regenerateTokenState.collect { result ->
            Log.d("Network", "Regenerate token state received. State: $result")

            if (result.isSuccessful){

                //Retry API call
                onSuccess()
            }else if(result.isError){

                //Logout
                logout()
            }
        }
    }

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