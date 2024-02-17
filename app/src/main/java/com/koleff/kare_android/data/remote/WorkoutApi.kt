package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.BaseWorkoutRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseRequest
import com.koleff.kare_android.data.model.request.SaveWorkoutRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutRequest
import com.koleff.kare_android.data.model.response.GetAllWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutResponse
import com.koleff.kare_android.data.model.response.GetSelectedWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface WorkoutApi {
    @PUT("api/v1/workout/selectworkout")
    suspend fun selectWorkout(
        @Body body: BaseWorkoutRequest
    ): BaseResponse

    @GET("api/v1/workout/getselectedworkout")
    suspend fun getSelectedWorkout(): GetSelectedWorkoutResponse

    @GET("api/v1/workout/getworkout")
    suspend fun getWorkout(
        @Body body: BaseWorkoutRequest
    ): GetWorkoutResponse

    @GET("api/v1/workout/getallworkouts")
    suspend fun getAllWorkouts(): GetAllWorkoutsResponse

    @GET("api/v1/workout/getallworkoutdetails")
    suspend fun getAllWorkoutDetails(): GetAllWorkoutDetailsResponse

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

    @POST("api/v1/workout/addexercise")
    fun addExercise(body: DeleteExerciseRequest): GetWorkoutDetailsResponse

    @PUT("api/v1/workout/saveworkout")
    suspend fun updateWorkoutDetails(
        @Body body: SaveWorkoutRequest
    ): BaseResponse

    @PUT("api/v1/workout/updateworkout")
    suspend fun updateWorkout(
        @Body body: UpdateWorkoutRequest
    ): BaseResponse

    @GET("api/v1/workout/createworkout")
    suspend fun createNewWorkout(): GetWorkoutResponse

    @GET("api/v1/workout/createcustomworkout")
    suspend fun createCustomWorkout(body: UpdateWorkoutRequest): GetWorkoutResponse

    @GET("api/v1/workout/createcustomworkoutdetails")
    suspend fun createCustomWorkoutDetails(body: SaveWorkoutRequest): GetWorkoutDetailsResponse
}