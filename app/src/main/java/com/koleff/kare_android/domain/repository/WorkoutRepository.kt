package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun getSelectedWorkout(): Flow<ResultWrapper<SelectedWorkoutWrapper>>

    suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>>  //Used for loading list view and refresh

    suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>>

    suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>>  //Used for loading single view

    suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun addExercise(workoutId: Int, exerciseId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun updateWorkoutDetails(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>>

    suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>>

    suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>>

}