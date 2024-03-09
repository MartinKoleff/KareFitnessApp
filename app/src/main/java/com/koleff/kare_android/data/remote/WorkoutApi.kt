package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.ExerciseAddRequest
import com.koleff.kare_android.data.model.request.FetchWorkoutByIdRequest
import com.koleff.kare_android.data.model.request.ExerciseDeletionRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutDetailsRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutRequest
import com.koleff.kare_android.data.model.response.WorkoutDetailsListResponse
import com.koleff.kare_android.data.model.response.WorkoutsListResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.SelectedWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface WorkoutApi {
    @PUT("api/v1/workout/selectworkout")
    suspend fun selectWorkout(
        @Body body: FetchWorkoutByIdRequest
    ): BaseResponse

    @PUT("api/v1/workout/deselectworkout")
    suspend fun deselectWorkout(
        @Body body: FetchWorkoutByIdRequest
    ): BaseResponse

    @GET("api/v1/workout/getselectedworkout")
    suspend fun getSelectedWorkout(): SelectedWorkoutResponse

    @GET("api/v1/workout/getworkout")
    suspend fun getWorkout(
        @Body body: FetchWorkoutByIdRequest
    ): WorkoutResponse

    @GET("api/v1/workout/getallworkouts")
    suspend fun getAllWorkouts(): WorkoutsListResponse

    @GET("api/v1/workout/getallworkoutdetails")
    suspend fun getAllWorkoutDetails(): WorkoutDetailsListResponse

    @GET("api/v1/workout/getworkoutdetails")
    suspend fun getWorkoutDetails(
        @Body body: FetchWorkoutByIdRequest
    ): WorkoutDetailsResponse

    @DELETE("api/v1/workout/deleteworkout")
    suspend fun deleteWorkout(
        @Body body: FetchWorkoutByIdRequest
    ): BaseResponse


    @DELETE("api/v1/workout/deleteexercise")
    suspend fun deleteExercise(
        @Body body: ExerciseDeletionRequest
    ): WorkoutDetailsResponse

    @POST("api/v1/workout/addexercise")
    fun addExercise(
        @Body body: ExerciseAddRequest
    ): WorkoutDetailsResponse

    @PUT("api/v1/workout/updateworkoutdetails")
    suspend fun updateWorkoutDetails(
        @Body body: UpdateWorkoutDetailsRequest
    ): BaseResponse

    @PUT("api/v1/workout/updateworkout")
    suspend fun updateWorkout(
        @Body body: UpdateWorkoutRequest
    ): BaseResponse

    @GET("api/v1/workout/createworkout")
    suspend fun createNewWorkout(): WorkoutResponse

    @GET("api/v1/workout/createcustomworkout")
    suspend fun createCustomWorkout(
        @Body body: UpdateWorkoutRequest
    ): WorkoutResponse

    @GET("api/v1/workout/createcustomworkoutdetails")
    suspend fun createCustomWorkoutDetails(
        @Body body: UpdateWorkoutDetailsRequest
    ): WorkoutDetailsResponse
}