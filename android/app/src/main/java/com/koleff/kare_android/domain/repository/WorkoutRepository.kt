package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.data.model.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.data.model.wrapper.ServerResponseData
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun selectWorkout(workoutId: String): Flow<ResultWrapper<ServerResponseData>>

    suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>>

    suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>>  //Used for loading list view

    suspend fun getWorkoutDetails(workoutId: String): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>

    suspend fun deleteWorkout(workoutId: String): Flow<ResultWrapper<ServerResponseData>>

    suspend fun saveWorkout(workout: SaveWorkoutDto): Flow<ResultWrapper<ServerResponseData>>
}