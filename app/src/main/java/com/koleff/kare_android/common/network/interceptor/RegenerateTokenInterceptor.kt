package com.koleff.kare_android.common.network.interceptor

import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.broadcast.handler.RegenerateTokenNotifier
import com.koleff.kare_android.common.preferences.Preferences
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegenerateTokenInterceptor @Inject constructor(
    private val preferences: Preferences,
    private val broadcastManager: LocalBroadcastManager,
    private val regenerateTokenNotifier: RegenerateTokenNotifier
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val tokens = preferences.getTokens()
        val accessToken = tokens?.accessToken ?: ""

        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $accessToken")
            .build()

        val response = chain.proceed(newRequest)

        //Unauthorized
        return if (response.code == 401) {
            response.close()

            lateinit var retryResponse: Response

            //On success
            regenerateToken {
                val newToken = preferences.getTokens() ?: return@regenerateToken

                //Re-build request
                val retriedRequest = originalRequest.newBuilder()
                    .removeHeader("Authorization")
                    .addHeader("Authorization", "Bearer ${newToken.accessToken}")
                    .build()
                retryResponse = chain.proceed(retriedRequest)
            }

            retryResponse
        } else {
            response
        }
    }

    private fun logout() {
        Log.d("ApiAuthorizationCallWrapper", "Invalid token. Logging out.")

        val intent = Intent(Constants.ACTION_LOGOUT)
        broadcastManager.sendBroadcast(intent)
    }

    private fun regenerateToken(onSuccess: () -> Unit) = runBlocking {
        Log.d("ApiAuthorizationCallWrapper", "Token regenerating...")

        val intent = Intent(Constants.ACTION_REGENERATE_TOKEN)
        broadcastManager.sendBroadcast(intent)

        regenerateTokenNotifier.regenerateTokenState.collect { result ->
            Log.d("ApiAuthorizationCallWrapper", "Regenerate token state received. State: $result")

            if (result.isSuccessful) {

                //Retry API call
                onSuccess()
            } else if (result.isError) {

                //Logout
                logout()
            }
        }
    }
}