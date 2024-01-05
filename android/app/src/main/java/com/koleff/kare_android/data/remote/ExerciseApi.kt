package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.GetExerciseDetailsRequest
import com.koleff.kare_android.data.model.request.GetExercisesRequest
import com.koleff.kare_android.data.model.response.GetExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApi {

    @GET("api/v1/exercise/get/{muscle_group_id}/all") //TODO: update endpoint...
    suspend fun getExercises(
        @Body body: GetExercisesRequest
    ): GetExercisesResponse


    @GET("api/v1/exercise/get/{exercise_id}/details") //TODO: update endpoint...
    suspend fun getExerciseDetails(
        @Body body: GetExerciseDetailsRequest
    ): GetExerciseDetailsResponse

}