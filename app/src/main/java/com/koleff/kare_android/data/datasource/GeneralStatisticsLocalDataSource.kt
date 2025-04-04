package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.MuscleGroupMaxWeight
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutTotalTimesCompleted
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.MuscleGroupResponse
import com.koleff.kare_android.data.model.response.StrongestMuscleGroupResponse
import com.koleff.kare_android.data.model.response.TotalTimesCompletedResponse
import com.koleff.kare_android.data.model.response.TotalWeightLiftedResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.WorkoutStreakResponse
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
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
import java.util.logging.Logger

class GeneralStatisticsLocalDataSource(
    val statisticsDao: StatisticsDao,
    val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
    val doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao,
    val exerciseDao: ExerciseDao
) : GeneralStatisticsDataSource {

    //All completed workouts (can have the same workout completed multiple times on different dates)
    override suspend fun getWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutsCompleted =
                doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics().size

            val datesOfCompletion = doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics().map { it.performanceMetrics.date }

            val result = TotalTimesCompletedWrapper(
                TotalTimesCompletedResponse(
                    WorkoutTotalTimesCompleted(
                        totalTimesCompleted = workoutsCompleted,
                        datesOfCompletion = datesOfCompletion
                    )
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

            val datesOfCompletion = doWorkoutPerformanceMetricsDao.getAllWorkoutPerformanceMetrics().map { it.performanceMetrics.date }

            val result = TotalTimesCompletedWrapper(
                TotalTimesCompletedResponse(
                    WorkoutTotalTimesCompleted(
                        totalTimesCompleted = workoutsCompleted,
                        datesOfCompletion = datesOfCompletion
                    )
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
            .sortedByDescending { it.performanceMetrics.date }
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

    //Most common muscle group for all workouts
    override suspend fun getMostTrainedMuscleGroup(): Flow<ResultWrapper<MuscleGroupWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val allExerciseSets = doWorkoutExerciseSetDao.getAllSets()
            val catalogExercises =
                exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)

            val exerciseIdToMuscleGroup =
                catalogExercises.associateBy({ it.exerciseId }, { it.muscleGroup })

            val exerciseCount = allExerciseSets.groupingBy { it.exerciseId }.eachCount()

            val mostTrainedExerciseId = exerciseCount.maxByOrNull { it.value }?.key

            val mostTrainedMuscleGroup =
                mostTrainedExerciseId?.let { exerciseIdToMuscleGroup[it] } ?: MuscleGroup.NONE

            val result = MuscleGroupWrapper(
                MuscleGroupResponse(muscleGroup = mostTrainedMuscleGroup)
            )

            emit(ResultWrapper.Success(result))
        }

    //Most common exercise for all workouts
    override suspend fun getMostTrainedExercise(): Flow<ResultWrapper<ExerciseWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val allExerciseSets = doWorkoutExerciseSetDao.getAllSets()

        val catalogExercises =
            exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)

        val exerciseIdCount = mutableMapOf<Int, Int>()
        for (exerciseSet in allExerciseSets) {
            exerciseIdCount[exerciseSet.exerciseId] =
                exerciseIdCount.getOrDefault(exerciseSet.exerciseId, 0) + 1
        }

        val mostTrainedExerciseId = exerciseIdCount.maxByOrNull { it.value }?.key

        val mostTrainedExercise = mostTrainedExerciseId?.let { exerciseId ->
            catalogExercises.find { it.exerciseId == exerciseId }
        }

        val result = ExerciseWrapper(
            ExerciseResponse(
                exercise = mostTrainedExercise?.toDto(emptyList()) ?: ExerciseDto()
            )
        )

        emit(ResultWrapper.Success(result))
    }

    //Total weight lifted for all workouts
    override suspend fun getTotalWeightLifted(): Flow<ResultWrapper<TotalWeightLiftedWrapper>> =
        flow {
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
    override suspend fun getStrongestMuscleGroup(): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val allExerciseSets =
                doWorkoutExerciseSetDao.getAllSets()

            val catalogExercises =
                exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)
            val exerciseIdToMuscleGroup =
                catalogExercises.associateBy({ it.exerciseId }, { it.muscleGroup })

            val maxWeightLifted = mutableMapOf<MuscleGroup, Float>()
            for (exerciseSet in allExerciseSets) {
                val muscleGroup =
                    exerciseIdToMuscleGroup[exerciseSet.exerciseId] ?: continue  //Skip if not found

                maxWeightLifted[muscleGroup] = maxOf(
                    maxWeightLifted.getOrDefault(muscleGroup, 0f),
                    exerciseSet.weight ?: 0.0f
                )
                Logger.getLogger("GeneralStatisticsLocalDataSource")
                    .info("maxWeightLifted: $maxWeightLifted, muscleGroup: $muscleGroup")
            }

            val strongestMuscleGroup =
                maxWeightLifted.maxByOrNull { it.value }?.key ?: MuscleGroup.NONE
            Logger.getLogger("GeneralStatisticsLocalDataSource")
                .info("strongestMuscleGroup: $strongestMuscleGroup")

            val result = StrongestMuscleGroupWrapper(
                StrongestMuscleGroupResponse(
                    data = MuscleGroupMaxWeight(
                        muscleGroup = strongestMuscleGroup,
                        maxWeight = maxWeightLifted[strongestMuscleGroup] ?: 0f
                    )
                )
            )
            emit(ResultWrapper.Success(result))
        }

    //Strongest muscle group with its total weight lifted for all workouts
    override suspend fun getMuscleGroupTotalWeightLifted(selectedMuscleGroup: MuscleGroup): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val allExerciseSets =
                doWorkoutExerciseSetDao.getAllSets()

            val catalogExercises =
                exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)
            val exerciseIdToMuscleGroup =
                catalogExercises.associateBy({ it.exerciseId }, { it.muscleGroup })

            val totalWeightLifted = mutableMapOf<MuscleGroup, Float>()
            for (exerciseSet in allExerciseSets) {
                val muscleGroup =
                    exerciseIdToMuscleGroup[exerciseSet.exerciseId] ?: continue  //Skip if not found
                val weightLifted = (exerciseSet.weight ?: 0f) * exerciseSet.reps.toFloat()

                totalWeightLifted[muscleGroup] =
                    totalWeightLifted.getOrDefault(muscleGroup, 0f) + weightLifted
            }

            val result = StrongestMuscleGroupWrapper(
                StrongestMuscleGroupResponse(
                    data = MuscleGroupMaxWeight(
                        muscleGroup = selectedMuscleGroup,
                        maxWeight = totalWeightLifted[selectedMuscleGroup] ?: 0f
                    )
                )
            )
            emit(ResultWrapper.Success(result))
        }
}