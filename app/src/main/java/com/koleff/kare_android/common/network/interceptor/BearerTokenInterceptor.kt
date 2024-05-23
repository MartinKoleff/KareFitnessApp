package com.koleff.kare_android.common.network.interceptor

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.preferences.Preferences
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BearerTokenInterceptor @Inject constructor(
    private val preferences: Preferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val newUrl = original.url.newBuilder()
            .scheme(Constants.SCHEME)
            .host(Constants.BASE_URL)
//                    .port(Constants.PORT) //if port is needed
//                    .addQueryParameter("access_key", Constants.API_KEY) //if API key is needed
            .build()

        val requestBuilder = original.newBuilder()
            .method(original.method, original.body)
            .url(newUrl)

        val unauthorizedRequestPaths = listOf("/login", "/register")
        val requestPath = original.url.encodedPath

        //Add token authorization for all requests except auth ones
        if (!requestPath.endsWith(unauthorizedRequestPaths[0]) &&
            !requestPath.endsWith(unauthorizedRequestPaths[1])) {
            val tokens = preferences.getTokens()
            val accessToken = tokens?.accessToken ?: ""

            requestBuilder.addHeader("Authorization", "Bearer $accessToken}")
        }

        val request = requestBuilder.build()
        val response = chain.proceed(request)
        return response
    }
}