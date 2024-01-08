package com.koleff.kare_android.data.datasource

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSet
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
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class WorkoutLocalDataSource @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val exerciseSetDao: ExerciseSetDao
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
            data ?: run {
                emit(ResultWrapper.ApiError())
                return@flow
            }

            //Add sets from DB relations
            val exercisesWithSetsList = mutableListOf<ExerciseWithSet>()

            for (exercise in data.safeExercises) {
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
        val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)
        workoutDetails ?: run {
            return emptyList()
        }

        val exercises = workoutDetails.safeExercises

        val exercisesWithSetsList = mutableListOf<ExerciseWithSet>()
        for (exercise in exercises) {
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
            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            data ?: run {
                emit(ResultWrapper.ApiError())
                return@flow
            }

            //Contains different exercises
            val currentEntryInDB: List<ExerciseDto> =
                data.safeExercises.map { it.toExerciseDto() }

            if (currentEntryInDB.size <= workout.exercises.size) {
                val newExercises =
                    workout.exercises.filterNot { currentEntryInDB.contains(it) }.distinct()
                val exerciseIds = newExercises.map { it.exerciseId }

                //Wire new exercises ids to workout id -> setup cross refs
                val crossRefs: List<WorkoutDetailsExerciseCrossRef> =
                    exerciseIds.map { exerciseId ->
                        WorkoutDetailsExerciseCrossRef(
                            workoutDetailsId = workoutId,
                            exerciseId = exerciseId
                        )
                    }

                workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(crossRefs)
            }

            // Exercise sets -> setup cross refs
            val exerciseSetCrossRefs: List<ExerciseSetCrossRef> =
                workout.exercises.flatMap { exercise ->
                    exercise.sets.map { set ->
                        val setEntity = set.toSetEntity()

                        if (set.setId == null) {
                            //New set -> insert it
                            val newSetId = UUID.randomUUID()
                            setEntity.setId = newSetId

                            exerciseSetDao.saveSet(setEntity)
                        } else {
                            //Existing set -> update it
                            Log.d(
                                "SaveWorkout-LocalDatasource",
                                "Exercise set with setId ${setEntity.setId} updated. Data: $setEntity"
                            )
                            exerciseSetDao.updateSet(setEntity)
                        }

                        ExerciseSetCrossRef(
                            exerciseId = exercise.exerciseId,
                            setId = setEntity.setId
                        )
                    }
                }

            exerciseDao.insertAllExerciseSetCrossRef(exerciseSetCrossRefs)


            //Update total exercises
            val workoutEntry = workoutDao.getWorkoutById(workoutId)
            workoutEntry.totalExercises = workout.exercises.size
            workoutDao.updateWorkout(workoutEntry) //if update is not working -> invalid id is provided

            workoutDetailsDao.insertWorkoutDetails(workout.toWorkoutDetails())

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun updateWorkout(
        workout: WorkoutDto
    ): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.updateWorkout(workout.toWorkout())

            //Update duplicate data between Workout and WorkoutDetails...
            val workoutDetailsWithExercises =
                workoutDetailsDao.getWorkoutDetailsById(workout.workoutId)
            workoutDetailsWithExercises ?: emit(ResultWrapper.ApiError())

            val updatedWorkoutDetails = workoutDetailsWithExercises!!.workoutDetails.copy(
                workoutDetailsId = workout.workoutId,
                name = workout.name,
                description = workoutDetailsWithExercises.workoutDetails.description,
                muscleGroup = workout.muscleGroup,
                isSelected = workout.isSelected
            )

//            val updatedWorkoutDetailsWithExercises =
//                workoutDetailsWithExercises.copy(workoutDetails = updatedWorkoutDetails)
//            val exercises: MutableList<ExerciseDto> =
//                updatedWorkoutDetailsWithExercises.exercises.map { Exercise::toExerciseDto } as MutableList<ExerciseDto>
//
//            val workoutDetails = updatedWorkoutDetailsWithExercises.workoutDetails
//                .toWorkoutDetailsDto(exercises)

//            workoutDetailsDao.updateWorkoutDetails(workoutDetails.toWorkoutDetails())

            workoutDetailsDao.updateWorkoutDetails(updatedWorkoutDetails)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun createWorkout(): Flow<ResultWrapper<GetWorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workout = WorkoutDto()

            val workoutId = workoutDao.insertWorkout(workout.toWorkout())

            val workoutDetails = WorkoutDetailsDto().copy(workoutId = workoutId.toInt())
            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetails.toWorkoutDetails()) //returns 0

            //Setup cross refs
            Log.d(
                "WorkoutLocalDataSource-CreateWorkout",
                "WorkoutId: $workoutId, WorkoutDetailsId: $workoutDetailsId"
            )
            val crossRef = WorkoutDetailsWorkoutCrossRef(
                workoutDetailsId = workoutId.toInt(),
                workoutId = workoutDetailsId.toInt()
            )
            workoutDao.insertWorkoutDetailsWorkoutCrossRef(crossRef)

            val result = GetWorkoutWrapper(
                GetWorkoutResponse(
                    workout.copy(workoutId = workoutId.toInt())
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<GetWorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val selectedWorkout = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            selectedWorkout ?: run {
                emit(ResultWrapper.ApiError())
                return@flow
            }

            val filteredExercises =
                selectedWorkout.safeExercises.filter { exercise -> exercise.exerciseId != exerciseId }
            val exercisesDto: MutableList<ExerciseDto> =
                filteredExercises.map(Exercise::toExerciseDto) as MutableList<ExerciseDto>

            val updatedWorkout = selectedWorkout.copy(exercises = filteredExercises)
            val updatedWorkoutDto = updatedWorkout.workoutDetails.toWorkoutDetailsDto(exercisesDto)

            //Delete workout details - exercise cross ref
            val workoutDetailsExerciseCrossRef =
                WorkoutDetailsExerciseCrossRef(
                    workoutDetailsId = workoutId,
                    exerciseId = exerciseId
                )
            workoutDetailsDao.deleteWorkoutDetailsExerciseCrossRef(workoutDetailsExerciseCrossRef)

            //Delete exercise - set cross ref
            val deletedExercise = exerciseDao.getExerciseById(exerciseId)
            for (set in deletedExercise.sets) {
                val exerciseSetCrossRef =
                    ExerciseSetCrossRef(
                        exerciseId = exerciseId,
                        setId = set.setId
                    )
                exerciseDao.deleteExerciseSetCrossRef(exerciseSetCrossRef)
            }

            //Update workout and workout details DAOs
//            workoutDetailsDao.insertWorkoutDetails(updatedWorkoutDto)

            //Update total exercises
            val workout = workoutDao.getWorkoutById(workoutId)
            workout.totalExercises = updatedWorkout.safeExercises.size
            workoutDao.updateWorkout(workout) //if update is not working -> invalid id is provided

            val result = GetWorkoutDetailsWrapper(
                GetWorkoutDetailsResponse(updatedWorkoutDto)
            )

            emit(ResultWrapper.Success(result))
        }
}