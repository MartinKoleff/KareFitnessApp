package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.WorkoutTotalTimesCompleted
import com.koleff.kare_android.data.model.response.DatesOfCompletionResponse
import com.koleff.kare_android.data.model.response.TotalRepsResponse
import com.koleff.kare_android.data.model.response.TotalSetsResponse
import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import com.koleff.kare_android.data.room.dao.StatisticsDao
import com.koleff.kare_android.domain.wrapper.DatesOfCompletionWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TotalRepsWrapper
import com.koleff.kare_android.domain.wrapper.TotalSetsWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WorkoutStatisticsLocalDataSource(
    val statisticsDao: StatisticsDao,
) : WorkoutStatisticsDataSource {

    //Total times a workout has been completed
    override suspend fun getTotalTimesCompleted(workoutId: Int): Flow<ResultWrapper<TotalTimesCompletedWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val timesCompleted = statisticsDao.getTotalWorkoutsCompleted(workoutId) ?: 0
            val datesOfCompletion =
                statisticsDao.getDatesOfCompletionForWorkout(workoutId) ?: emptyList()
            val result = TotalTimesCompletedWrapper(
                TotalTimesCompletedResponse(
                    WorkoutTotalTimesCompleted(
                        totalTimesCompleted = timesCompleted,
                        datesOfCompletion = datesOfCompletion
                    )
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

    override suspend fun getDatesOfCompletionForWorkout(workoutId: Int): Flow<ResultWrapper<DatesOfCompletionWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val datesOfCompletion = statisticsDao.getDatesOfCompletionForWorkout(workoutId) ?: emptyList()
            val result = DatesOfCompletionWrapper(
                DatesOfCompletionResponse(
                    dates = datesOfCompletion
                )
            )
            emit(ResultWrapper.Success(result))
        }

    //Total reps performed per workout
    override suspend fun getTotalRepsPerformed(
        workoutId: Int
    ): Flow<ResultWrapper<TotalRepsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val repsPerformed = statisticsDao.getTotalRepsPerformedForWorkout(
            workoutId = workoutId
        ) ?: 0
        val result = TotalRepsWrapper(
            TotalRepsResponse(
                totalReps = repsPerformed
            )
        )
        emit(ResultWrapper.Success(result))
    }

    //Total sets performed per workout
    override suspend fun getTotalSetsPerformed(
        workoutId: Int
    ): Flow<ResultWrapper<TotalSetsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val setsPerformed = statisticsDao.getTotalSetsPerformedForWorkout(
            workoutId = workoutId
        ) ?: 0
        val result = TotalSetsWrapper(
            TotalSetsResponse(
                totalSets = setsPerformed
            )
        )
        emit(ResultWrapper.Success(result))
    }
}