package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDataSource: WorkoutDataSource
) : WorkoutRepository {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.selectWorkout(workoutId)
    }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return workoutDataSource.getSelectedWorkout()
    }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return workoutDataSource.getWorkout(workoutId)
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>> {
        return workoutDataSource.getAllWorkouts()
    }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> {
        return workoutDataSource.getWorkoutDetails(workoutId)
    }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.deleteWorkout(workoutId)
    }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> {
        return workoutDataSource.deleteExercise(workoutId, exerciseId)
    }

    override suspend fun saveWorkout(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.saveWorkout(workout)
    }

    override suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.updateWorkout(workout)
    }

    override suspend fun createWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return workoutDataSource.createWorkout()
    }
}