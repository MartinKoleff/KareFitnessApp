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

    @GET("api/v1/exercise/getcatalogexercises/all")
    suspend fun getCatalogExercises(
        @Body body: FetchExercisesByMuscleGroupRequest
    ): ExerciseListResponse

    @GET("api/v1/exercise/getcatalogexercise")
    suspend fun getCatalogExercise(
        @Body body: FetchExerciseRequest
    ): ExerciseResponse

    @GET("api/v1/exercise/getexercise")
    suspend fun getExercise(@Body body: FetchExerciseRequest): ExerciseResponse

    @GET("api/v1/exercise/getexercisedetails")
    suspend fun getExerciseDetails(
        @Body body: FetchExerciseRequest
    ): ExerciseDetailsResponse
}