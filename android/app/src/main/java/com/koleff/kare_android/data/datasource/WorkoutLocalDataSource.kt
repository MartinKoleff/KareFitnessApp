package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.SaveWorkoutDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.GetAllWorkoutsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.GetWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.domain.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutLocalDataSource @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val workoutDetailsDao: WorkoutDetailsDao
) : WorkoutDataSource {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.selectWorkoutById(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutByIsSelected()

        val result = GetWorkoutWrapper(
            GetWorkoutResponse(data.toWorkoutDto())
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<GetWorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutById(workoutId)

        val result = GetWorkoutWrapper(
            GetWorkoutResponse(data.toWorkoutDto())
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<GetAllWorkoutsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutsOrderedById()

        val result = GetAllWorkoutsWrapper(
            GetAllWorkoutsResponse(data.map(Workout::toWorkoutDto))
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            //Add sets from DB relations
            val exercisesWithSetsList = mutableListOf<ExerciseWithSet>()
            for(exercise in data.exercises) {
                val exercisesWithSet = exerciseDao.getExerciseById(exercise.exerciseId)

                exercisesWithSetsList.add(exercisesWithSet)
            }
            val exercisesWithSetsDto: MutableList<ExerciseDto> =
                exercisesWithSetsList.map(ExerciseWithSet::toExerciseDto) as MutableList<ExerciseDto>
            val workout = data.workoutDetails.toWorkoutDetailsDto(exercisesWithSetsDto)

            val result = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(workout)
            )

            emit(ResultWrapper.Success(result))
        }

    private fun getWorkoutExercises(workoutId: Int): List<ExerciseDto> {
        val data = workoutDetailsDao.getWorkoutDetailsById(workoutId).exercises

        val exercisesWithSetsList = mutableListOf<ExerciseWithSet>()
        for(exercise in data) {
            val exercisesWithSet = exerciseDao.getExerciseById(exercise.exerciseId)

            exercisesWithSetsList.add(exercisesWithSet)
        }
        val exercisesWithSetsDto: MutableList<ExerciseDto> =
            exercisesWithSetsList.map(ExerciseWithSet::toExerciseDto) as MutableList<ExerciseDto>

        return exercisesWithSetsList.map(ExerciseWithSet::toExerciseDto)
    }

    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.deleteWorkout(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun saveWorkout(workout: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutId = workout.workoutId

            //Contains different exercises -> setup cross ref
            val currentEntryInDB: List<ExerciseDto> =
                workoutDetailsDao.getWorkoutDetailsById(workoutId) //TODO: handle null
                    .exercises
                    .map { it.toExerciseDto() }

            if (currentEntryInDB.size <= workout.exercises.size) {
                val newExercises = workout.exercises.filterNot { currentEntryInDB.contains(it) }.distinct()
                val exerciseIds = newExercises.map { it.exerciseId }

                //Wire new exercises ids to workout id
                val crossRefs: List<WorkoutDetailsExerciseCrossRef> = exerciseIds.map { exerciseId ->
                    WorkoutDetailsExerciseCrossRef(workoutDetailsId = workoutId, exerciseId = exerciseId)
                }

                workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(crossRefs)
            }

            //Update total exercises
            val workoutDto = workoutDao.getWorkoutById(workoutId)
            workoutDto.totalExercises = workout.exercises.size
            workoutDao.updateWorkout(workoutDto)

            workoutDetailsDao.insertWorkoutDetails(workout.toWorkoutDetails())

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }
}