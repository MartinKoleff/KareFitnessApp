package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDataSource: WorkoutDataSource
) : WorkoutRepository {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.selectWorkout(workoutId)
    }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<SelectedWorkoutWrapper>> {
        return workoutDataSource.getSelectedWorkout()
    }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>> {
        return workoutDataSource.getWorkout(workoutId)
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> {
        return workoutDataSource.getAllWorkouts()
    }

    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>> {
        return workoutDataSource.getAllWorkoutDetails()
    }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.getWorkoutDetails(workoutId)
    }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.deleteWorkout(workoutId)
    }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.deleteExercise(workoutId, exerciseId)
    }

    override suspend fun addExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.addExercise(workoutId, exerciseId)
    }

    override suspend fun updateWorkoutDetails(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.updateWorkoutDetails(workout)
    }

    override suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.updateWorkout(workout)
    }

    override suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>> {
        return workoutDataSource.createNewWorkout()
    }

    override suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>> {
        return workoutDataSource.createCustomWorkout(workoutDto)
    }

    override suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.createCustomWorkoutDetails(workoutDetailsDto)
    }
}