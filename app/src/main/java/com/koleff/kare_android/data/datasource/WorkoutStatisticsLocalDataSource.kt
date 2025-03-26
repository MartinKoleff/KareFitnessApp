package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse
import com.koleff.kare_android.data.model.response.TotalRepsResponse
import com.koleff.kare_android.data.model.response.TotalSetsResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.OnboardingDao
import com.koleff.kare_android.data.room.dao.StatisticsDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkoutStatisticsLocalDataSource(
    val statisticsDao: StatisticsDao,
    val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
    val doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao,
    val onboardingDao: OnboardingDao,
    val exerciseDao: ExerciseDao,
    val workoutDao: WorkoutDao,
    val exerciseSetDao: ExerciseSetDao
) : WorkoutStatisticsDataSource {

    //Total times a workout has been completed
    override suspend fun getTotalTimesCompleted(workoutId: Int): Flow<ResultWrapper<TotalTimesCompletedWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val timesCompleted = statisticsDao.getTotalWorkoutsCompleted(workoutId) ?: 0
            val result = TotalTimesCompletedWrapper(
                TotalTimesCompletedResponse(
                    totalTimesCompleted = timesCompleted
                )
            )

            emit(ResultWrapper.Success(result))
        }

    //Total weight lifted for workout
    override suspend fun getTotalWeightLifted(workoutId: Int): Flow<ResultWrapper<TotalWeightLiftedWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val weightLifted = statisticsDao.getTotalWeightLiftedForWorkout(workoutId) ?: 0.0f
            val result = TotalWeightLiftedWrapper(
                TotalWeightLiftedResponse(
                    totalWeight = weightLifted
                )
            )
            emit(ResultWrapper.Success(result))
        }

    //Total reps performed for exercise per workout
    override suspend fun getTotalRepsPerformed(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<TotalRepsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val repsPerformed = statisticsDao.getTotalRepsPerformedForExercise(
            workoutId = workoutId,
            exerciseId = exerciseId
        ) ?: 0
        val result = TotalRepsWrapper(
            TotalRepsResponse(
                totalReps = repsPerformed
            )
        )
        emit(ResultWrapper.Success(result))
    }

    //Total sets performed for exercise per workout
    override suspend fun getTotalSetsPerformed(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<TotalSetsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val setsPerformed = statisticsDao.getTotalSetsPerformedForExercise(
            workoutId = workoutId,
            exerciseId = exerciseId
        ) ?: 0
        val result = TotalSetsWrapper(
            TotalSetsResponse(
                totalSets = setsPerformed
            )
        )
        emit(ResultWrapper.Success(result))
    }
}