package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.BaseWorkoutRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseRequest
import com.koleff.kare_android.data.model.request.SaveWorkoutRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutRequest
import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT

interface WorkoutApi {
    @PUT("api/v1/workout/selectworkout")
    suspend fun selectWorkout(
        @Body body: BaseWorkoutRequest
    ): BaseResponse

    @GET("api/v1/workout/getselectedworkout")
    suspend fun getSelectedWorkout(): GetWorkoutResponse

    @GET("api/v1/workout/getworkout")
    suspend fun getWorkout(
        @Body body: BaseWorkoutRequest
    ): GetWorkoutResponse

    @GET("api/v1/workout/getallworkouts")
    suspend fun getAllWorkouts(): GetAllWorkoutsResponse

    @GET("api/v1/workout/getworkoutdetails")
    suspend fun getWorkoutDetails(
        @Body body: BaseWorkoutRequest
    ): GetWorkoutDetailsResponse

    @DELETE("api/v1/workout/deleteworkout")
    suspend fun deleteWorkout(
        @Body body: BaseWorkoutRequest
    ): BaseResponse

    @DELETE("api/v1/workout/deleteexercise")
    suspend fun deleteExercise(
        @Body body: DeleteExerciseRequest
    ): GetWorkoutDetailsResponse

    @PUT("api/v1/workout/saveworkout")
    suspend fun saveWorkout(
        @Body body: SaveWorkoutRequest
    ): BaseResponse

    @PUT("api/v1/workout/updateworkout")
    suspend fun updateWorkout(
        @Body body: UpdateWorkoutRequest
    ): BaseResponse

    @GET("api/v1/workout/createworkout")
    suspend fun createWorkout(): GetWorkoutResponse
}