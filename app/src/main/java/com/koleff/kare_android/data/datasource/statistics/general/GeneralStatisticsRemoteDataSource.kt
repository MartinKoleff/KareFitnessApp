package com.koleff.kare_android.data.datasource.statistics.general

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.request.FetchExercisesByMuscleGroupRequest
import com.koleff.kare_android.data.remote.GeneralStatisticsApi
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.MuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.StrongestMuscleGroupWrapper
import com.koleff.kare_android.domain.wrapper.TotalTimesCompletedWrapper
import com.koleff.kare_android.domain.wrapper.TotalWeightLiftedWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutStreakWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GeneralStatisticsRemoteDataSource @Inject constructor(
    private val generalStatisticsApi: GeneralStatisticsApi,
    private val apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GeneralStatisticsDataSource {
    override suspend fun getWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                TotalTimesCompletedWrapper(
                    generalStatisticsApi.getWorkoutsCompleted()
                )
            }
        )
    }

    override suspend fun getDistinctWorkoutsCompleted(): Flow<ResultWrapper<TotalTimesCompletedWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                TotalTimesCompletedWrapper(
                    generalStatisticsApi.getDistinctWorkoutsCompleted()
                )
            }
        )
    }

    override suspend fun getWorkoutStreak(): Flow<ResultWrapper<WorkoutStreakWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                WorkoutStreakWrapper(
                    generalStatisticsApi.getWorkoutStreak()
                )
            }
        )
    }

    override suspend fun getMostFrequentWorkout(): Flow<ResultWrapper<WorkoutWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                WorkoutWrapper(
                    generalStatisticsApi.getMostFrequentWorkout()
                )
            }
        )
    }

    override suspend fun getMostTrainedMuscleGroup(): Flow<ResultWrapper<MuscleGroupWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                MuscleGroupWrapper(
                    generalStatisticsApi.getMostTrainedMuscleGroup()
                )
            }
        )
    }

    override suspend fun getMostTrainedExercise(): Flow<ResultWrapper<ExerciseWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                ExerciseWrapper(
                    generalStatisticsApi.getMostTrainedExercise()
                )
            }
        )
    }

    override suspend fun getTotalWeightLifted(): Flow<ResultWrapper<TotalWeightLiftedWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                TotalWeightLiftedWrapper(
                    generalStatisticsApi.getTotalWeightLifted()
                )
            }
        )
    }

    override suspend fun getStrongestMuscleGroup(): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> {
        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                StrongestMuscleGroupWrapper(
                    generalStatisticsApi.getStrongestMuscleGroup()
                )
            }
        )
    }

    override suspend fun getMuscleGroupTotalWeightLifted(selectedMuscleGroup: MuscleGroup): Flow<ResultWrapper<StrongestMuscleGroupWrapper>> {
      val body = FetchExercisesByMuscleGroupRequest(selectedMuscleGroup.muscleGroupId)

        return apiAuthorizationCallWrapper.executeApiCall(
            dispatcher, {
                StrongestMuscleGroupWrapper(
                    generalStatisticsApi.getMuscleGroupTotalWeightLifted(body)
                )
            }
        )
    }
}