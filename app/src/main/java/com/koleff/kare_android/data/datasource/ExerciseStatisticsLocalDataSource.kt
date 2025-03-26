package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.response.GetExercisePRResponse
import com.koleff.kare_android.data.model.response.GetExerciseTotalRepsResponse
import com.koleff.kare_android.data.model.response.GetExerciseTotalSetsResponse
import com.koleff.kare_android.data.model.response.GetExerciseTotalWeightResponse
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.OnboardingDao
import com.koleff.kare_android.data.room.dao.StatisticsDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.domain.wrapper.ExercisePRWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseTotalWeightWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExerciseStatisticsLocalDataSource(
    val statisticsDao: StatisticsDao,
    val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
    val doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao,
    val onboardingDao: OnboardingDao,
    val exerciseDao: ExerciseDao,
    val workoutDao: WorkoutDao,
    val exerciseSetDao: ExerciseSetDao
) : ExerciseStatisticsDataSource {

    //Get exercise best PR
    override suspend fun getPR(exerciseId: Int): Flow<ResultWrapper<ExercisePRWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val bestPR = statisticsDao.getBestPR(exerciseId) ?: 0.0f
        val result = ExercisePRWrapper(
            GetExercisePRResponse(
                pr = bestPR
            )
        )

        emit(ResultWrapper.Success(result))
    }

    //Total reps performed for exercise
    override suspend fun getTotalRepsPerformed(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalRepsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val repsPerformed = statisticsDao.getTotalRepsPerformedForExercise(exerciseId) ?: 0
            val result = ExerciseTotalRepsWrapper(
                GetExerciseTotalRepsResponse(
                    totalReps = repsPerformed
                )
            )

            emit(ResultWrapper.Success(result))
        }

    //Total sets performed for exercise
    override suspend fun getTotalSetsPerformed(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalSetsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val setsPerformed = statisticsDao.getTotalSetsPerformedForExercise(exerciseId) ?: 0
            val result = ExerciseTotalSetsWrapper(
                GetExerciseTotalSetsResponse(
                    totalSets = setsPerformed
                )
            )

            emit(ResultWrapper.Success(result))
        }

    //Total weight lifted for exercise
    override suspend fun getTotalWeightLifted(exerciseId: Int): Flow<ResultWrapper<ExerciseTotalWeightWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val totalWeight = statisticsDao.getTotalWeightLiftedForExercise(exerciseId) ?: 0.0f
            val result = ExerciseTotalWeightWrapper(
                GetExerciseTotalWeightResponse(
                    totalWeight = totalWeight
                )
            )

            emit(ResultWrapper.Success(result))
        }
}