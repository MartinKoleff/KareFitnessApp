package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.BaseWorkoutRequest
import com.koleff.kare_android.data.model.request.SaveWorkoutRequest
import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface WorkoutApi {
    @PUT("api/v1/workout/selectworkout") //TODO: update endpoint...
    suspend fun selectWorkout(
        @Body body: BaseWorkoutRequest
    ): BaseResponse

    @GET("api/v1/workout/getselectedworkout") //TODO: update endpoint...
    suspend fun getSelectedWorkout(): GetWorkoutResponse

    @GET("api/v1/workout/getworkout") //TODO: update endpoint...
    suspend fun getWorkout(
        @Body body: BaseWorkoutRequest
    ): GetWorkoutResponse

    @GET("api/v1/workout/getallworkouts") //TODO: update endpoint...
    suspend fun getAllWorkouts(): GetAllWorkoutsResponse

    @GET("api/v1/workout/getworkoutdetails") //TODO: update endpoint...
    suspend fun getWorkoutDetails(
        @Body body: BaseWorkoutRequest
    ): GetWorkoutDetailsResponse

    @DELETE("api/v1/workout/deleteworkout") //TODO: update endpoint...
    suspend fun deleteWorkout(
        @Body body: BaseWorkoutRequest
    ): BaseResponse

    @PUT("api/v1/workout/saveworkout") //TODO: update endpoint...
    suspend fun saveWorkout(
        @Body body: SaveWorkoutRequest
    ): BaseResponse
}