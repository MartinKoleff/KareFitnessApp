package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.datasource.DoWorkoutSetupRequest
import com.koleff.kare_android.data.model.request.UpdateExerciseSetsRequest
import com.koleff.kare_android.data.model.response.DoWorkoutResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DoWorkoutApi {

    @POST("api/v1/doworkout/initialsetup")
    suspend fun initialSetup(
        @Body body: DoWorkoutSetupRequest
    ): DoWorkoutResponse

    @POST("api/v1/doworkout/updateexercisesetsaftertimer")
    suspend fun updateExerciseSetsAfterTimer(
        @Body body: UpdateExerciseSetsRequest
    ): DoWorkoutResponse
}