package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>>

    suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>>  //Used for loading list view and refresh

    suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>>  //Used for loading single view

    suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>

    suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun deleteExercise(workoutId: Int, exerciseId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>

    suspend fun saveWorkout(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun createWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>>
}