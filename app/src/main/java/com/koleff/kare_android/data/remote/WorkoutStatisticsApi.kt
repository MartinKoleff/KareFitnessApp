package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.FetchByWorkoutIdRequest
import com.koleff.kare_android.data.model.response.DatesOfCompletionResponse
import com.koleff.kare_android.data.model.response.TotalRepsResponse
import com.koleff.kare_android.data.model.response.TotalSetsResponse
import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface WorkoutStatisticsApi {

    @GET("api/v1/statistics/workout/gettotaltimescompleted")
    suspend fun getTotalTimesCompleted(
        @Body body: FetchByWorkoutIdRequest
    ): TotalTimesCompletedResponse

    @GET("api/v1/statistics/workout/gettotalweightlifted")
    suspend fun getTotalWeightLifted(
        @Body body: FetchByWorkoutIdRequest
    ): TotalWeightLiftedResponse

    @GET("api/v1/statistics/workout/getdatesofcompletion")
    suspend fun getDatesOfCompletionForWorkout(
        @Body body: FetchByWorkoutIdRequest
    ): DatesOfCompletionResponse

    @GET("api/v1/statistics/workout/gettotalrepsperformed")
    suspend fun getTotalRepsPerformed(
        @Body body: FetchByWorkoutIdRequest
    ): TotalRepsResponse

    @GET("api/v1/statistics/workout/gettotalsetsperformed")
    suspend fun getTotalSetsPerformed(
        @Body body: FetchByWorkoutIdRequest
    ): TotalSetsResponse
}