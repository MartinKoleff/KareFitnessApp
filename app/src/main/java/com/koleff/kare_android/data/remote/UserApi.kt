package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.model.request.FetchUserByEmail
import com.koleff.kare_android.data.model.request.FetchUserByUsername
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.response.UserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

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