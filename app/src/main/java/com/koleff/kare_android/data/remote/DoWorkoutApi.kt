package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.UpdateExerciseSetsRequest
import com.koleff.kare_android.data.model.request.UpdateWorkoutDetailsRequest
import com.koleff.kare_android.data.model.response.DoWorkoutResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DoWorkoutApi {

    @POST("api/v1/doworkout/initialsetup")
    suspend fun initialSetup(
        @Body body: UpdateWorkoutDetailsRequest
    ): DoWorkoutResponse

    @POST("api/v1/doworkout/skipnextset")
    suspend fun skipNextSet(
        @Body body: UpdateExerciseSetsRequest
    ): DoWorkoutResponse

    @POST("api/v1/doworkout/skipnextexercise")
    suspend fun skipNextExercise(
        @Body body: UpdateExerciseSetsRequest
    ): DoWorkoutResponse
}