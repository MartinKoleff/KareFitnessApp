package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.MuscleGroupMaxWeight
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.MuscleGroupResponse
import com.koleff.kare_android.data.model.response.StrongestMuscleGroupResponse
import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.WorkoutStreakResponse
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.StatisticsDao
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.MuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.StrongestMuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutStreakWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Date

class GeneralStatisticsLocalDataSource(
    val statisticsDao: StatisticsDao,
    val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao
) : GeneralStatisticsDataSource {

    //All completed workouts (can have the same workout completed multiple times on different dates)
    override suspend fun getWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutsCompleted =
                doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics().size
            val result = TotalTimesCompletedWrapper(
                TotalTimesCompletedResponse(
                    totalTimesCompleted = workoutsCompleted
                )
            )
            emit(ResultWrapper.Success(result))
        }

    //All distinct completed workouts
    override suspend fun getDistinctWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutsCompleted = doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics()
                .distinctBy { it.workout.workoutId }
                .size

            val result = TotalTimesCompletedWrapper(
                TotalTimesCompletedResponse(
                    totalTimesCompleted = workoutsCompleted
                )
            )
            emit(ResultWrapper.Success(result))
        }

    //Workout streak (consecutive days of training)
    override suspend fun getWorkoutStreak(): Flow<ResultWrapper<WorkoutStreakWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutDates = doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics()
                .map { it.performanceMetrics.date }

            val sortedDates = workoutDates.sortedDescending()
            var streak = 1
            for (i in 1 until sortedDates.size) {
                if (isConsecutiveDay(sortedDates[i - 1], sortedDates[i])) {
                    streak++
                } else {
                    break
                }
            }

            val result = WorkoutStreakWrapper(
                WorkoutStreakResponse(
                    streak = streak
                )
            )
            emit(ResultWrapper.Success(result))
        }

    private fun isConsecutiveDay(date1: Date, date2: Date): Boolean {
        val diff = (date1.time - date2.time) / (1000 * 60 * 60 * 24) //Day
        return diff == 1L
    }

    //Most frequent workout
    override suspend fun getMostFrequentWorkout(): Flow<ResultWrapper<WorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val workouts = doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics()
            .distinctBy { it.workout.workoutId }
            .map { it.workout }
            .map(Workout::toDto)

        val workoutsByRepetition = workouts.groupingBy { it }.eachCount()
        val mostFrequentWorkout = workoutsByRepetition.maxByOrNull { it.value }?.key

        val result = WorkoutWrapper(
            WorkoutResponse(
                workout = mostFrequentWorkout ?: WorkoutDto()
            )
        )
        emit(ResultWrapper.Success(result))
    }

    //TODO: test...
    private suspend fun getMostFrequentWorkout2(): Flow<ResultWrapper<WorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val mostFrequentWorkout = statisticsDao.getFavoriteWorkout()?.toDto()

        val result = WorkoutWrapper(
            WorkoutResponse(
                workout = mostFrequentWorkout ?: WorkoutDto()
            )
        )
        emit(ResultWrapper.Success(result))
    }

    //Most trained muscle group for all workouts
    override suspend fun getMostTrainedMuscleGroup(): Flow<ResultWrapper<MuscleGroupWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mostTrainedMuscleGroup = statisticsDao.getFavoriteMuscleGroup() ?: MuscleGroup.NONE
            val result = MuscleGroupWrapper(
                MuscleGroupResponse(
                    muscleGroup = mostTrainedMuscleGroup
                )
            )
            emit(ResultWrapper.Success(result))
        }

    //Most common exercise for all workouts
    override suspend fun getMostTrainedExercise(): Flow<ResultWrapper<ExerciseWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val mostTrainedExercise = statisticsDao.getMostPerformedExercise()
        val mostTrainedExerciseWithSets = mostTrainedExercise?.toDto()

        //transform to exercise with sets
        val result = ExerciseWrapper(
            ExerciseResponse(
                exercise = mostTrainedExerciseWithSets ?: ExerciseDto()
            )
        )

        emit(ResultWrapper.Success(result))
    }

    //Total weight lifted for all workouts
    override suspend fun getTotalWeightLifted(): Flow<ResultWrapper<TotalWeightLiftedWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val totalWeightLifted = statisticsDao.getTotalWeightLifted() ?: 0.0f
        val result = TotalWeightLiftedWrapper(
            TotalWeightLiftedResponse(
                totalWeight = totalWeightLifted
            )
        )
        emit(ResultWrapper.Success(result))
    }

    //Strongest muscle group with its total weight lifted for all workouts
    override suspend fun getStrongestMuscleGroup(): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val strongestMuscleGroup = statisticsDao.getMaxWeightPerMuscleGroup() ?: MuscleGroupMaxWeight(MuscleGroup.NONE, 0.0f)
        val result = StrongestMuscleGroupWrapper(
            StrongestMuscleGroupResponse(
                data = strongestMuscleGroup
            )
        )
        emit(ResultWrapper.Success(result))
    }
}