package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.statistics.general.GeneralStatisticsDataSource
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.domain.repository.GeneralStatisticsRepository
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.MuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.StrongestMuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutStreakWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import kotlinx.coroutines.flow.Flow

class GeneralStatisticsRepositoryImpl(private val generalStatisticsDataSource: GeneralStatisticsDataSource) :
    GeneralStatisticsRepository {
    override suspend fun getWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> {
        return generalStatisticsDataSource.getWorkoutsCompleted()
    }

    override suspend fun getDistinctWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> {
        return generalStatisticsDataSource.getDistinctWorkoutsCompleted()
    }

    override suspend fun getWorkoutStreak(): Flow<ResultWrapper<WorkoutStreakWrapper>> {
        return generalStatisticsDataSource.getWorkoutStreak()
    }

    override suspend fun getMostFrequentWorkout(): Flow<ResultWrapper<WorkoutWrapper>> {
        return generalStatisticsDataSource.getMostFrequentWorkout()
    }

    override suspend fun getMostTrainedMuscleGroup(): Flow<ResultWrapper<MuscleGroupWrapper>> {
        return generalStatisticsDataSource.getMostTrainedMuscleGroup()
    }

    override suspend fun getMostTrainedExercise(): Flow<ResultWrapper<ExerciseWrapper>> {
        return generalStatisticsDataSource.getMostTrainedExercise()
    }

    override suspend fun getTotalWeightLifted(): Flow<ResultWrapper<TotalWeightLiftedWrapper>> {
        return generalStatisticsDataSource.getTotalWeightLifted()
    }

    override suspend fun getStrongestMuscleGroup(): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> {
        return generalStatisticsDataSource.getStrongestMuscleGroup()
    }

    override suspend fun getMuscleGroupTotalWeightLifted(selectedMuscleGroup: MuscleGroup): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> {
        return generalStatisticsDataSource.getMuscleGroupTotalWeightLifted(selectedMuscleGroup)
    }
}