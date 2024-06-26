package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.AddNewExerciseSetRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseSetRequest
import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByWorkoutIdRequest
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApi {

    @GET("api/v1/exercise/getcatalogexercises/all/{muscle_group_id}") //TODO: update endpoint...
    suspend fun getCatalogExercises(
        @Body body: FetchExercisesByMuscleGroupRequest
    ): GetExercisesResponse

    @GET("api/v1/exercise/getcatalogexercise/{exercise_id}") //TODO: update endpoint...
    suspend fun getCatalogExercise(
        @Body body: FetchExerciseRequest
    ): ExerciseResponse

    @GET("api/v1/exercise/getexercise/{workout_id}") //TODO: update endpoint...
    suspend fun getExercise(@Body body: FetchExerciseRequest): ExerciseResponse

    @GET("api/v1/exercise/getexercisedetails/{exercise_id}") //TODO: update endpoint...
    suspend fun getExerciseDetails(
        @Body body: FetchExerciseRequest
    ): ExerciseDetailsResponse

    @GET("api/v1/exercise/addnewexerciseset") //TODO: update endpoint...
    fun addNewExerciseSet(body: AddNewExerciseSetRequest): ExerciseResponse

    @GET("api/v1/exercise/deleteexerciseset") //TODO: update endpoint...
    fun deleteExerciseSet(body: DeleteExerciseSetRequest): ExerciseResponse

}