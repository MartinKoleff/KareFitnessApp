package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.FetchUserByEmail
import com.koleff.kare_android.data.model.request.FetchUserByUsername
import com.koleff.kare_android.data.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface UserApi {

    @GET("api/v1/user/getbyemail")
    suspend fun getUserByEmail(
        @Body body: FetchUserByEmail
    ): UserResponse

    @GET("api/v1/user/getbyusername")
    suspend fun getUserByUsername(
        @Body body: FetchUserByUsername
    ): UserResponse

}