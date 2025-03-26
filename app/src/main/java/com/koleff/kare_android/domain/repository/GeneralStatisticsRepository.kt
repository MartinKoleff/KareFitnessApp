package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.MuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.StrongestMuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutStreakWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import kotlinx.coroutines.flow.Flow

interface GeneralStatisticsRepository {

    suspend fun getWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>>

    suspend fun getDistinctWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>>

    suspend fun getWorkoutStreak(): Flow<ResultWrapper<WorkoutStreakWrapper>>

    suspend fun getMostFrequentWorkout(): Flow<ResultWrapper<WorkoutWrapper>>

    suspend fun getMostTrainedMuscleGroup(): Flow<ResultWrapper<MuscleGroupWrapper>>

    suspend fun getMostTrainedExercise(): Flow<ResultWrapper<ExerciseWrapper>>

    suspend fun getTotalWeightLifted(): Flow<ResultWrapper<TotalWeightLiftedWrapper>>

    suspend fun getStrongestMuscleGroup(): Flow<ResultWrapper<StrongestMuscleGroupWrapper>>
}