package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.DoWorkoutDataSource
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.domain.repository.DoWorkoutRepository
import com.koleff.kare_android.domain.wrapper.DoWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.ui.state.DoWorkoutData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DoWorkoutRepositoryImpl @Inject constructor(
    private val doWorkoutDataSource: DoWorkoutDataSource
): DoWorkoutRepository {

    override suspend fun initialSetup(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<DoWorkoutWrapper>> {
        return doWorkoutDataSource.initialSetup(workoutDetails)
    }

    override suspend fun selectNextExercise(
        currentDoWorkoutData: DoWorkoutData
    ): Flow<ResultWrapper<DoWorkoutWrapper>> {
        return doWorkoutDataSource.selectNextExercise(currentDoWorkoutData)
    }

    override suspend fun skipNextExercise(
        currentDoWorkoutData: DoWorkoutData
    ): Flow<ResultWrapper<DoWorkoutWrapper>> {
        return doWorkoutDataSource.skipNextExercise(currentDoWorkoutData)
    }

    override suspend fun updateExerciseSetsAfterTimerUseCase(currentDoWorkoutData: DoWorkoutData): Flow<ResultWrapper<DoWorkoutWrapper>> {
        return doWorkoutDataSource.updateExerciseSetsAfterTimerUseCase(currentDoWorkoutData)
    }
}