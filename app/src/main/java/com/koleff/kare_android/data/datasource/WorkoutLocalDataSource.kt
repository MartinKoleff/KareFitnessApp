package com.koleff.kare_android.data.datasource

import android.util.Log
import androidx.room.EmptyResultSetException
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.WorkoutDetailsListResponse
import com.koleff.kare_android.data.model.response.WorkoutsListResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.SelectedWorkoutResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
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

    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<SelectedWorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDao.getWorkoutByIsSelected()

            val result = SelectedWorkoutWrapper(
                SelectedWorkoutResponse(data?.toWorkoutDto())
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutById(workoutId)

        val result = WorkoutWrapper(
            WorkoutResponse(data.toWorkoutDto())
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutsOrderedById()

        val result = WorkoutListWrapper(
            WorkoutsListResponse(data.map(Workout::toWorkoutDto))
        )

        emit(ResultWrapper.Success(result))
    }

    //Exercises don't return ExerciseSets
    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDetailsDao.getWorkoutDetailsOrderedById()

            val result = WorkoutDetailsListWrapper(
                WorkoutDetailsListResponse(data.map { workoutDetailsWitExercises ->

                    //Null safety
                    workoutDetailsWitExercises.exercises ?: return@flow

                    val exercises =
                        workoutDetailsWitExercises.exercises.map { exercise ->
                            val sets = exerciseDao.getExerciseById(exercise.exerciseId).sets

                            exercise.toExerciseDto(sets)
                        } as MutableList

                    workoutDetailsWitExercises.workoutDetails.toWorkoutDetailsDto(
                        exercises
                    )
                })
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
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

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(workout)
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

    //No need for separate deleteWorkoutDetails functionality. When workout is deleted the workout details should be deleted also.
    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.deleteWorkout(workoutId)
            workoutDetailsDao.deleteWorkoutDetails(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutId = workoutDetails.workoutId
            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            //If no WorkoutDetails found -> return error
            data ?: run {
                emit(ResultWrapper.ApiError())
                return@flow
            }

            //Contains different exercises
            val currentEntryInDB: List<ExerciseDto> =
                data.safeExercises.map { exercise ->
                    val sets = exerciseDao.getExerciseById(exercise.exerciseId).sets

                    exercise.toExerciseDto(sets)
                }

            if (currentEntryInDB.size <= workoutDetails.exercises.size) {
                val newExercises =
                    workoutDetails.exercises.filterNot { currentEntryInDB.contains(it) }.distinct()
                Log.d("UpdateWorkoutDetails-LocalDataSource", "New exercises: $newExercises")

                val exerciseIds = newExercises.map { it.exerciseId }

                //Wire new exercises ids to workoutDetails id -> setup cross refs
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
                workoutDetails.exercises.flatMap { exercise ->
                    exercise.sets.map { set ->
                        val exerciseSet = set.toExerciseSetDto()

                        if (set.setId == null) {

                            //New set -> insert it
                            val newSetId = UUID.randomUUID()
                            exerciseSet.setId = newSetId

                            exerciseSetDao.saveSet(exerciseSet)
                        } else {

                            //Existing set -> update it
                            Log.d(
                                "UpdateWorkoutDetails-LocalDatasource",
                                "Exercise set with setId ${exerciseSet.setId} added. Data: $exerciseSet"
                            )

                            //Trying to add set with already generated id
                            try{
                                exerciseSetDao.getSetById(exerciseSet.setId) //Checking if entry is in DB -> update

                                exerciseSetDao.updateSet(exerciseSet)
                            }catch (e: EmptyResultSetException){
                                exerciseSetDao.saveSet(exerciseSet) //Entry is not in DB -> save
                            }
                        }

                        ExerciseSetCrossRef(
                            exerciseId = exercise.exerciseId,
                            setId = exerciseSet.setId
                        )
                    }
                }

            exerciseDao.insertAllExerciseSetCrossRef(exerciseSetCrossRefs)


            //Update total exercises, name, muscle group and isSelected
            val workoutEntry = workoutDao.getWorkoutById(workoutId).copy(
                name = workoutDetails.name,
                totalExercises = workoutDetails.exercises.size,
                muscleGroup = workoutDetails.muscleGroup,
                isSelected = workoutDetails.isSelected,
            )
            workoutDao.updateWorkout(workoutEntry) //if update is not working -> invalid id is provided

            workoutDetailsDao.updateWorkoutDetails(workoutDetails.toWorkoutDetails())

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

            //If no WorkoutDetails found -> return error
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

    override suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workout = WorkoutDto()

            val workoutId = workoutDao.insertWorkout(workout.toWorkout()) //Get workout id
            val workoutName = "Workout $workoutId"

            val workoutUpdatedName =
                workout.copy(workoutId = workoutId.toInt(), name = workoutName).toWorkout()
            workoutDao.updateWorkout(workoutUpdatedName) //Update workout name

            val workoutDetails =
                WorkoutDetailsDto().copy(workoutId = workoutId.toInt(), name = workoutName)
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

            val result = WorkoutWrapper(
                WorkoutResponse(
                    workout.copy(workoutId = workoutId.toInt(), name = workoutName)
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutId = workoutDao.insertWorkout(workoutDto.toWorkout()) //Get workout id

            //Create duplicate WorkoutDetails with no exercises and sets...
            val workoutDetailsDto =
                WorkoutDetailsDto().copy(
                    workoutId = workoutId.toInt(),
                    name = workoutDto.name,
                    muscleGroup = workoutDto.muscleGroup,
                    isSelected = workoutDto.isSelected
                )
            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toWorkoutDetails()) //returns 0

            //Setup cross refs
            Log.d(
                "WorkoutLocalDataSource-CreateCustomWorkout",
                "WorkoutId: $workoutId, WorkoutDetailsId: $workoutDetailsId"
            )
            val crossRef = WorkoutDetailsWorkoutCrossRef(
                workoutDetailsId = workoutId.toInt(),
                workoutId = workoutDetailsId.toInt()
            )
            workoutDao.insertWorkoutDetailsWorkoutCrossRef(crossRef)

            val result = WorkoutWrapper(
                WorkoutResponse(
                    workoutDto.copy(workoutId = workoutId.toInt())
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toWorkoutDetails()) //Get workout details id

            //Create Workout for the WorkoutDetails...
            val workoutDto =
                WorkoutDto().copy(
                    workoutId = workoutDetailsId.toInt(),
                    name = workoutDetailsDto.name,
                    muscleGroup = workoutDetailsDto.muscleGroup,
                    isSelected = workoutDetailsDto.isSelected,
                    totalExercises = workoutDetailsDto.exercises.size,
                    snapshot = "snapshot $workoutDetailsId.png"
                )
            val workoutId = workoutDao.insertWorkout(workoutDto.toWorkout()) //returns 0

            //Workout - Workout Details cross ref
            Log.d(
                "WorkoutLocalDataSource-CreateCustomWorkoutDetails",
                "WorkoutId: $workoutId, WorkoutDetailsId: $workoutDetailsId"
            )
            val crossRef = WorkoutDetailsWorkoutCrossRef(
                workoutDetailsId = workoutId.toInt(),
                workoutId = workoutDetailsId.toInt()
            )
            workoutDao.insertWorkoutDetailsWorkoutCrossRef(crossRef)

            //Exercise - WorkoutDetails cross refs
            val exercises =
                workoutDetailsDto.exercises.distinct()
            Log.d("WorkoutLocalDataSource-CreateCustomWorkoutDetails", "Exercises: $exercises")

            val exerciseIds = exercises.map { it.exerciseId }

            //Wire exercises ids to workoutDetails id -> setup cross refs
            val crossRefs: List<WorkoutDetailsExerciseCrossRef> =
                exerciseIds.map { exerciseId ->
                    WorkoutDetailsExerciseCrossRef(
                        workoutDetailsId = workoutDetailsId.toInt(),
                        exerciseId = exerciseId
                    )
                }

            workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(crossRefs)

            //Exercise - ExerciseSet cross refs
            val exerciseSetCrossRefs: List<ExerciseSetCrossRef> =
                exercises.flatMap { exercise ->
                    exercise.sets.map { set ->
                        val exerciseSet = set.toExerciseSetDto()

                        if (set.setId == null) {

                            //New set -> insert it
                            val newSetId = UUID.randomUUID()
                            exerciseSet.setId = newSetId

                            exerciseSetDao.saveSet(exerciseSet)
                        } else {

                            //Existing set -> update it
                            Log.d(
                                "WorkoutLocalDataSource-CreateCustomWorkoutDetails",
                                "Exercise set with setId ${exerciseSet.setId} added. Data: $exerciseSet"
                            )

                            //Trying to add set with already generated id
                            try{
                                exerciseSetDao.getSetById(exerciseSet.setId) //Checking if entry is in DB -> update

                                exerciseSetDao.updateSet(exerciseSet)
                            }catch (e: EmptyResultSetException){
                                exerciseSetDao.saveSet(exerciseSet) //Entry is not in DB -> save
                            }
                        }

                        ExerciseSetCrossRef(
                            exerciseId = exercise.exerciseId,
                            setId = exerciseSet.setId
                        )
                    }
                }

            exerciseDao.insertAllExerciseSetCrossRef(exerciseSetCrossRefs)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(
                    workoutDetailsDto.copy(workoutId = workoutDetailsId.toInt())
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun deleteExercise(
        workoutId: Int,
        exerciseId: Int
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
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
                filteredExercises.map { exercise ->
                    val sets = exerciseDao.getExerciseById(exercise.exerciseId).sets

                    exercise.toExerciseDto(sets)
                } as MutableList

            val updatedWorkout = selectedWorkout.copy(exercises = filteredExercises)
            val updatedWorkoutDto =
                updatedWorkout.workoutDetails.toWorkoutDetailsDto(exercisesDto)

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
                exerciseSetDao.deleteSet(set)
            }

            //Update total exercises
            val workout = workoutDao.getWorkoutById(workoutId)
            workout.totalExercises = updatedWorkout.safeExercises.size
            workoutDao.updateWorkout(workout) //if update is not working -> invalid id is provided

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDto)
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun addExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val selectedWorkout = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            selectedWorkout ?: run {
                emit(ResultWrapper.ApiError())
                return@flow
            }

            val filteredExercises =
                ArrayList(selectedWorkout.safeExercises).apply {
                    add(exercise.toExercise())
                }

            val exercisesDto: MutableList<ExerciseDto> =
                filteredExercises.map { exercise ->
                    val sets = exerciseDao.getExerciseById(exercise.exerciseId).sets

                    exercise.toExerciseDto(sets)
                } as MutableList

            val updatedWorkout = selectedWorkout.copy(exercises = filteredExercises)
            val updatedWorkoutDto =
                updatedWorkout.workoutDetails.toWorkoutDetailsDto(exercisesDto)

            //Create workout details - exercise cross ref
            val workoutDetailsExerciseCrossRef =
                WorkoutDetailsExerciseCrossRef(
                    workoutDetailsId = workoutId,
                    exerciseId = exercise.exerciseId
                )
            workoutDetailsDao.insertWorkoutDetailsExerciseCrossRef(workoutDetailsExerciseCrossRef)

            //Create exercise - set cross ref
            for (set in exercise.sets) {
                val exerciseSetCrossRef =
                    ExerciseSetCrossRef(
                        exerciseId = exercise.exerciseId,
                        setId = set.setId ?: UUID.randomUUID()
                    )
                exerciseDao.insertExerciseSetCrossRef(exerciseSetCrossRef)
                exerciseSetDao.saveSet(set.toExerciseSetDto())
            }

            //Update total exercises
            val workout = workoutDao.getWorkoutById(workoutId)
            workout.totalExercises = updatedWorkout.safeExercises.size
            workoutDao.updateWorkout(workout) //if update is not working -> invalid id is provided

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDto)
            )

            emit(ResultWrapper.Success(result))
        }
}