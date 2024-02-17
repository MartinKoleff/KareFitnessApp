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
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetSelectedWorkoutWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDataSource: WorkoutDataSource
) : WorkoutRepository {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.selectWorkout(workoutId)
    }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetSelectedWorkoutWrapper>> {
        return workoutDataSource.getSelectedWorkout()
    }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return workoutDataSource.getWorkout(workoutId)
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>> {
        return workoutDataSource.getAllWorkouts()
    }

    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<GetAllWorkoutDetailsWrapper>> {
        return workoutDataSource.getAllWorkoutDetails()
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

    override suspend fun addExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> {
        return workoutDataSource.addExercise(workoutId, exerciseId)
    }

    override suspend fun updateWorkoutDetails(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.updateWorkoutDetails(workout)
    }

    override suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.updateWorkout(workout)
    }

    override suspend fun createNewWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return workoutDataSource.createNewWorkout()
    }

    override suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<GetWorkoutWrapper>> {
        return workoutDataSource.createCustomWorkout(workoutDto)
    }

    override suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> {
        return workoutDataSource.createCustomWorkoutDetails(workoutDetailsDto)
    }
}