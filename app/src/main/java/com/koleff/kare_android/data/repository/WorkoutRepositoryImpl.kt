package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.wrapper.DuplicateExercisesWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutConfigurationWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val workoutDataSource: WorkoutDataSource
) : WorkoutRepository {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.selectWorkout(workoutId)
    }

    override suspend fun deselectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.deselectWorkout(workoutId)
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

    override suspend fun deleteMultipleExercises(
        workoutId: Int,
        exerciseIds: List<Int>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.deleteMultipleExercises(workoutId, exerciseIds)
    }

    override suspend fun addExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.addExercise(workoutId, exercise)
    }

    override suspend fun addMultipleExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.addMultipleExercises(workoutId, exerciseList)
    }

    override suspend fun submitExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.submitExercise(workoutId, exercise)
    }

    override suspend fun submitMultipleExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> {
        return workoutDataSource.submitMultipleExercises(workoutId, exerciseList)
    }

    override suspend fun findDuplicateExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<DuplicateExercisesWrapper>> {
        return workoutDataSource.findDuplicateExercises(workoutId, exerciseList)
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

    override suspend fun getWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<WorkoutConfigurationWrapper>> {
        return workoutDataSource.getWorkoutConfiguration(workoutId)
    }

    override suspend fun updateWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.updateWorkoutConfiguration(workoutConfiguration)
    }

    override suspend fun saveWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<WorkoutConfigurationWrapper>> {
        return workoutDataSource.saveWorkoutConfiguration(workoutConfiguration)
    }

    override suspend fun deleteWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> {
        return workoutDataSource.deleteWorkoutConfiguration(workoutId)
    }
}