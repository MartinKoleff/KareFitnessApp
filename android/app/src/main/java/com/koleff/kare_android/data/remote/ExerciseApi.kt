package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.GetExercisesRequest
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface ExerciseApi {

    @GET("api/v1/exercise/get/{muscle_group}/all") //TODO: update endpoint...
    suspend fun getExercises(
        @Path("muscle_group") muscleGroup: String,
        @Body body: GetExercisesRequest
    ): GetExercisesResponse


}