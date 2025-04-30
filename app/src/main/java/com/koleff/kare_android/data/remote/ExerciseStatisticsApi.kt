package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.UpdateExerciseSetRequest
import com.koleff.kare_android.data.model.request.DeleteExerciseSetRequest
import com.koleff.kare_android.data.model.request.ExerciseStatisticsRequest
import com.koleff.kare_android.data.model.request.FetchExerciseRequest
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseListResponse
import com.koleff.kare_android.data.model.response.ExercisePRResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.TotalRepsResponse
import com.koleff.kare_android.data.model.response.TotalSetsResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ExerciseStatisticsApi {

    @GET("api/v1/statistics/exercise/getpr")
    suspend fun getPR(
        @Body body: ExerciseStatisticsRequest
    ): ExercisePRResponse

    @GET("api/v1/statistics/exercise/get1repmax")
    suspend fun get1RepMax(
        @Body body: ExerciseStatisticsRequest
    ): ExercisePRResponse

    @GET("api/v1/statistics/exercise/gettotalrepsperformed")
    suspend fun getTotalRepsPerformed(
        @Body body: ExerciseStatisticsRequest
    ): TotalRepsResponse

    @GET("api/v1/statistics/exercise/gettotalsetsperformed")
    suspend fun getTotalSetsPerformed(
        @Body body: ExerciseStatisticsRequest
    ): TotalSetsResponse

    @GET("api/v1/statistics/exercise/gettotalweightlifted")
    suspend fun getTotalWeighLifted(
        @Body body: ExerciseStatisticsRequest
    ): TotalWeightLiftedResponse
}