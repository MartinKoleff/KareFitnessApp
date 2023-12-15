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
import com.koleff.kare_android.data.model.wrapper.GetAllWorkoutsWrapper
import com.koleff.kare_android.data.model.wrapper.GetWorkoutDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetWorkoutWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.data.model.wrapper.ServerResponseData
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutLocalDataSource @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val workoutDBManager: WorkoutDBManager
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

        //Check if Room DB has data
        if (!workoutDBManager.hasInitializedWorkoutTableRoomDB) {
            workoutDBManager.initializeWorkoutTableRoomDB(workoutDao, workoutDetailsDao)
        }

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
            val exercises: MutableList<ExerciseDto> =
                data.exercises.map(Exercise::toExerciseDto) as MutableList<ExerciseDto>
            val workout = data.workoutDetails.toWorkoutDetailsDto(exercises)

            val result = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(workout)
            )

            emit(ResultWrapper.Success(result))
        }

    private fun getWorkoutExercises(workoutId: Int): List<ExerciseDto> {
        val data = workoutDetailsDao.getWorkoutDetailsById(workoutId).exercises

        return data.map(Exercise::toExerciseDto)
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
                val newExercises = workout.exercises.filterNot { currentEntryInDB.contains(it) }
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