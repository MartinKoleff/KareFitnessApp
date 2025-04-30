package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.UpdateExerciseSetRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseSetRequest
import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseListResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExerciseApi {

    @GET("api/v1/exercise/getcatalogexercises/all/{muscle_group_id}")
    suspend fun getCatalogExercises(
        @Body body: FetchExercisesByMuscleGroupRequest
    ): ExerciseListResponse

    @GET("api/v1/exercise/getcatalogexercise/{exercise_id}")
    suspend fun getCatalogExercise(
        @Body body: FetchExerciseRequest
    ): ExerciseResponse

    @GET("api/v1/exercise/getexercise/{workout_id}")
    suspend fun getExercise(@Body body: FetchExerciseRequest): ExerciseResponse

    @GET("api/v1/exercise/getexercisedetails/{exercise_id}")
    suspend fun getExerciseDetails(
        @Body body: FetchExerciseRequest
    ): ExerciseDetailsResponse

    @POST("api/v1/exercise/addnewexerciseset") //TODO: update method type...
    fun addNewExerciseSet(body: UpdateExerciseSetRequest): ExerciseResponse

    @POST("api/v1/exercise/deleteexerciseset") //TODO: update method type...
    fun deleteExerciseSet(body: DeleteExerciseSetRequest): ExerciseResponse

    @POST("api/v1/exercise/addnewexerciseset") //TODO: update method type...
    fun deleteLatestExerciseSet(body: UpdateExerciseSetRequest): ExerciseResponse
}