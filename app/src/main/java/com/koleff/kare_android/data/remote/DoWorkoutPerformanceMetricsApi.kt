package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.datasource.FetchPerformanceMetricsByWorkoutIdRequest
import com.koleff.kare_android.data.model.request.DeletePerformanceMetricsRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByDateAndIdRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByDateAndWorkoutIdRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByDateRequest
import com.koleff.kare_android.data.model.request.FetchPerformanceMetricsByIdRequest
import com.koleff.kare_android.data.model.request.SaveDoWorkoutExerciseSetRequest
import com.koleff.kare_android.data.model.request.SaveDoWorkoutExerciseSetsRequest
import com.koleff.kare_android.data.model.request.UpdatePerformanceMetricsRequest
import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsListResponse
import com.koleff.kare_android.data.model.response.DoWorkoutPerformanceMetricsResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface DoWorkoutPerformanceMetricsApi {

    @POST("api/v1/doworkoutperformancemetrics/getalldoworkoutperformancemetricsbydateandid")
    suspend fun getAllDoWorkoutPerformanceMetrics(
        @Body body: FetchPerformanceMetricsByDateAndIdRequest
    ): DoWorkoutPerformanceMetricsListResponse

    @POST("api/v1/doworkoutperformancemetrics/getalldoworkoutperformancemetricsbyid")
    suspend fun getAllDoWorkoutPerformanceMetrics(
        @Body body: FetchPerformanceMetricsByIdRequest
    ): DoWorkoutPerformanceMetricsResponse

    @POST("api/v1/doworkoutperformancemetrics/getalldoworkoutperformancemetricsbyworkoutid")
    suspend fun getAllDoWorkoutPerformanceMetrics(
        @Body body: FetchPerformanceMetricsByDateAndWorkoutIdRequest
    ): DoWorkoutPerformanceMetricsListResponse

    @POST("api/v1/doworkoutperformancemetrics/getalldoworkoutperformancemetrics")
    suspend fun getAllDoWorkoutPerformanceMetrics(): DoWorkoutPerformanceMetricsListResponse

    @POST("api/v1/doworkoutperformancemetrics/getalldoworkoutperformancemetricsbydate")
    suspend fun getAllDoWorkoutPerformanceMetrics(
        @Body body: FetchPerformanceMetricsByDateRequest
    ): DoWorkoutPerformanceMetricsListResponse

    @POST("api/v1/doworkoutperformancemetrics/getdoworkoutperformancemetricsby")
    suspend fun getDoWorkoutPerformanceMetrics(
        @Body body: FetchPerformanceMetricsByWorkoutIdRequest
    ): DoWorkoutPerformanceMetricsListResponse

    @POST("api/v1/doworkoutperformancemetrics/savedoworkoutperformancemetrics")
    suspend fun saveDoWorkoutPerformanceMetrics(
        @Body body: UpdatePerformanceMetricsRequest
    ): DoWorkoutPerformanceMetricsResponse

    @POST("api/v1/doworkoutperformancemetrics/updatedoworkoutperformancemetrics")
    suspend fun updateDoWorkoutPerformanceMetrics(
        @Body body: UpdatePerformanceMetricsRequest
    ): DoWorkoutPerformanceMetricsResponse

    @POST("api/v1/doworkoutperformancemetrics/deletedoworkoutperformancemetrics")
    suspend fun deleteDoWorkoutPerformanceMetrics(
        @Body body: DeletePerformanceMetricsRequest
    ): BaseResponse

    @POST("api/v1/doworkoutperformancemetrics/savedoworkoutexerciseset")
    suspend fun saveDoWorkoutExerciseSet(
        @Body body: SaveDoWorkoutExerciseSetRequest
    ): BaseResponse

    @POST("api/v1/doworkoutperformancemetrics/savealldoworkoutexercisesets")
    suspend fun saveAllDoWorkoutExerciseSets(
        @Body body: SaveDoWorkoutExerciseSetsRequest
    ): BaseResponse
}