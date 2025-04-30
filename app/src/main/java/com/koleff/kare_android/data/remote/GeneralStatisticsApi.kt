package com.koleff.kare_android.data.remote

import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.MuscleGroupResponse
import com.koleff.kare_android.data.model.response.StrongestMuscleGroupResponse
import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.WorkoutStreakResponse
import retrofit2.http.Body
import retrofit2.http.GET

interface GeneralStatisticsApi {

    @GET("api/v1/statistics/general/getworkoutscompleted")
    suspend fun getWorkoutsCompleted(): TotalTimesCompletedResponse

    @GET("api/v1/statistics/general/getdistinctworkoutscompleted")
    suspend fun getDistinctWorkoutsCompleted(): TotalTimesCompletedResponse

    @GET("api/v1/statistics/general/getworkoutstreak")
    suspend fun getWorkoutStreak(): WorkoutStreakResponse

    @GET("api/v1/statistics/general/getmostfrequentworkout")
    suspend fun getMostFrequentWorkout(): WorkoutResponse

    @GET("api/v1/statistics/general/getmosttrainedmusclegroup")
    suspend fun getMostTrainedMuscleGroup(): MuscleGroupResponse

    @GET("api/v1/statistics/general/getmosttrainedexercise")
    suspend fun getMostTrainedExercise(): ExerciseResponse

    @GET("api/v1/statistics/general/gettotalweightlifted")
    suspend fun getTotalWeightLifted(): TotalWeightLiftedResponse

    @GET("api/v1/statistics/general/getstrongestmusclegroup")
    suspend fun getStrongestMuscleGroup(): StrongestMuscleGroupResponse

    @GET("api/v1/statistics/general/getmusclegrouptotalweightlifted")
    suspend fun getMuscleGroupTotalWeightLifted(@Body body: FetchExercisesByMuscleGroupRequest): StrongestMuscleGroupResponse
}