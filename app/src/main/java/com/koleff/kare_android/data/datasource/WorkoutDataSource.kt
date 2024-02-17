package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetExercisesWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetSelectedWorkoutWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutDataSource {
    suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetSelectedWorkoutWrapper>>

    suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>>

    suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>>

    suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<GetAllWorkoutDetailsWrapper>>

    suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>

    suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun deleteExercise(workoutId: Int, exerciseId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>

    suspend fun addExercise(workoutId: Int, exerciseId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>

    suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun createNewWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>>

    suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<GetWorkoutWrapper>>

    suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<GetWorkoutDetailsWrapper>>
}