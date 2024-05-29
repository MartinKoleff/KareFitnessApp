package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.datasource.SignInRequest
import com.koleff.kare_android.data.model.request.RegenerateTokenRequest
import com.koleff.kare_android.data.model.request.RegistrationRequest
import com.koleff.kare_android.data.model.response.LoginResponse
import com.koleff.kare_android.data.model.response.TokenResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthenticationApi {

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body body: SignInRequest
    ): LoginResponse

    @POST("api/v1/auth/register")
    suspend fun register(
        @Body body: RegistrationRequest
    ): BaseResponse

    @GET("api/v1/auth/logout")
    suspend fun logout(
        @Body body: RegistrationRequest
    ): BaseResponse

    @GET("api/v1/auth/regeneratetoken")
    suspend fun regenerateToken(
        @Body body: RegenerateTokenRequest
    ): TokenResponse


    @POST("api/v1/auth/changepassword")
    suspend fun changePassword() //TODO: implement...
}