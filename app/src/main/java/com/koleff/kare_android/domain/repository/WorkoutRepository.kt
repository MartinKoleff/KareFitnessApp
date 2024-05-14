package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.DuplicateExercisesWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutConfigurationWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

    suspend fun deselectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>

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

    suspend fun deleteMultipleExercises(
        workoutId: Int,
        exerciseIds: List<Int>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun addExercise(workoutId: Int, exercise: ExerciseDto): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun addMultipleExercises(workoutId: Int, exerciseList: List<ExerciseDto>): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun submitExercise(workoutId: Int, exercise: ExerciseDto): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun submitMultipleExercises(workoutId: Int, exerciseList: List<ExerciseDto>): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun findDuplicateExercises(workoutId: Int, exerciseList: List<ExerciseDto>): Flow<ResultWrapper<DuplicateExercisesWrapper>>

    suspend fun updateWorkoutDetails(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun updateWorkout(workout: WorkoutDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>>

    suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>>

    suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>>

    suspend fun getWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<WorkoutConfigurationWrapper>>

    suspend fun updateWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun saveWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<WorkoutConfigurationWrapper>>

    suspend fun deleteWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<ServerResponseData>>
}