package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.SelectedWorkoutResponse
import com.koleff.kare_android.data.model.response.WorkoutConfigurationResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsListResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.WorkoutResponse
import com.koleff.kare_android.data.model.response.WorkoutsListResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.WorkoutConfigurationWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WorkoutLocalDataSourceV2 @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val exerciseDao: ExerciseDao,
    private val workoutDetailsDao: WorkoutDetailsDao,
    private val exerciseSetDao: ExerciseSetDao,
    private val workoutConfigurationDao: WorkoutConfigurationDao
) : WorkoutDataSource {
    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Deselect current selected workout
            val selectedWorkoutInDB = workoutDao.getWorkoutByIsSelected()

            //Workout not in DB
            selectedWorkoutInDB ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_NOT_FOUND))
                return@flow
            }

            val updatedWorkout = selectedWorkoutInDB.copy(
                isSelected = false
            )

            //Selecting workout that is not selected
            if (updatedWorkout.workoutId != workoutId) {

                //Update DB
                updateWorkout(
                    updatedWorkout.toDto()
                ).collect()
                workoutDao.selectWorkoutById(workoutId)
            }

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun deselectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Deselect workout
            val workout = workoutDao.getWorkoutById(workoutId)

            //Workout not in DB
            workout ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_NOT_FOUND))
                return@flow
            }

            val updatedWorkout = workout.copy(
                isSelected = false
            )

            //Update DB
            updateWorkout(
                updatedWorkout.toDto()
            ).collect()

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
                SelectedWorkoutResponse(data?.toDto())
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        try {
            val workout = workoutDao.getWorkoutById(workoutId)

            //Workout not in DB
            workout ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_NOT_FOUND))
                return@flow
            }

            val result = WorkoutWrapper(
                WorkoutResponse(workout.toDto())
            )

            emit(ResultWrapper.Success(result))
        } catch (e: NoSuchElementException) {
            emit(
                ResultWrapper.ApiError(
                    error = KareError.WORKOUT_NOT_FOUND
                )
            )
        }
    }

    override suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        val data = workoutDao.getWorkoutsOrderedById()

        val result = WorkoutListWrapper(
            WorkoutsListResponse(
                data.map(Workout::toDto)
            )
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDetailsDao.getWorkoutDetailsOrderedById()

            val result = WorkoutDetailsListWrapper(
                WorkoutDetailsListResponse(
                    data.map(WorkoutDetailsWithExercises::toDto)
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutDetailsWithExercises = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            workoutDetailsWithExercises ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_DETAILS_NOT_FOUND))
                return@flow
            }

            //Add sets to all exercises
            val workoutDetails = workoutDetailsWithExercises.toDto()
            val updatedExercisesWithSets = workoutDetails.exercises.map { exercise ->
                exerciseDao.getExerciseWithSets(
                    exerciseId = exercise.exerciseId,
                    workoutId = exercise.workoutId
                ).toDto()
            }
            val updatedWorkoutDetails = workoutDetails.copy(exercises = updatedExercisesWithSets)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetails)
            )

            emit(ResultWrapper.Success(result))
        }


    //No need for separate deleteWorkoutDetails functionality.
    //When workout is deleted the workout details should be deleted also.
    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.deleteWorkout(workoutId)
//            workoutDetailsDao.deleteWorkoutDetails(workoutId)
//            workoutConfigurationDao.deleteWorkoutConfiguration(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDetailsDao.updateWorkoutDetails(workoutDetails.toEntity())

            //Update duplicate data between WorkoutDetails and Workout
            val workout = workoutDao.getWorkoutById(workoutDetails.workoutId)

            //Workout not in DB
            workout ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_NOT_FOUND))
                return@flow
            }

            val updatedWorkout = Workout(
                workoutId = workoutDetails.workoutId,
                name = workoutDetails.name,
                muscleGroup = workoutDetails.muscleGroup,
                isSelected = workoutDetails.isSelected,
                snapshot = workout.snapshot,
                totalExercises = workoutDetails.exercises.size
            )
            workoutDao.updateWorkout(updatedWorkout)

            //Update exercises
            try {
                updateExercises(workoutDetails.exercises, workoutDetails.workoutId)
            } catch (e: IllegalArgumentException) {

                //Workout not found
                emit(ResultWrapper.ApiError(KareError.INVALID_WORKOUT))
                return@flow
            } catch (e: NoSuchElementException) {

                //Exercise not found
                emit(ResultWrapper.ApiError(KareError.EXERCISE_NOT_FOUND))
                return@flow
            }

            val result = ServerResponseData(
                BaseResponse()
            )
            emit(ResultWrapper.Success(result))
        }

    private suspend fun updateExercises(exercises: List<ExerciseDto>, workoutId: Int) {

        //Fetch exercises from DB
        val exercisesInDB = workoutDetailsDao.getWorkoutDetailsById(workoutId)?.exercises
        exercisesInDB ?: throw IllegalArgumentException("Invalid workout")

        exercises.forEach { exercise ->
            val existsInDB = exercisesInDB.any { exerciseInDB ->
                exerciseInDB.exerciseId == exercise.exerciseId &&
                        exerciseInDB.workoutId == exercise.workoutId
            }

            //Exercise in DB -> update
            if (existsInDB) {

                //Fetch DB entry
                try {
                    val exerciseInDB =
                        exerciseDao.getExerciseWithSets(exercise.exerciseId, exercise.workoutId)
                            .toDto()

                    updateExercise(
                        setsInDB = exerciseInDB.sets.map { set -> set.toEntity() },
                        newExercise = exercise.toEntity(),
                        newSets = exercise.sets.map { set -> set.toEntity() }
                    )
                } catch (e: NoSuchElementException) {
                    throw NoSuchElementException("Exercise not found")
                }
            } else {

                insertExercise(
                    exercise = exercise.toEntity(),
                    sets = exercise.sets.map { set -> set.toEntity() }
                )
            }
        }
    }

    private suspend fun updateExercise(
        setsInDB: List<ExerciseSet>,
        newExercise: Exercise,
        newSets: List<ExerciseSet>
    ) {
        exerciseDao.updateExercise(newExercise)

        //Remove sets that are not in newSets
        val setsToRemove = findMissingSets(
            newSets,
            setsInDB
        ) //TODO: test if setsInDB should be on top...

        setsToRemove.forEach { set ->
            exerciseSetDao.deleteSet(set)
        }

        //Add all newSets
        exerciseSetDao.insertAllExerciseSets(newSets)
    }

    private suspend fun insertExercise(exercise: Exercise, sets: List<ExerciseSet>) {
        exerciseDao.insertExercise(exercise)
        exerciseSetDao.insertAllExerciseSets(sets)
    }


    override suspend fun updateWorkout(
        workout: WorkoutDto
    ): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.updateWorkout(workout.toEntity())

            //Update duplicate data between Workout and WorkoutDetails...
            val workoutDetailsWithExercises =
                workoutDetailsDao.getWorkoutDetailsById(workout.workoutId)

            //If no WorkoutDetails is found -> return error
            workoutDetailsWithExercises ?: run {
                emit(
                    ResultWrapper.ApiError(
                        KareError.WORKOUT_DETAILS_NOT_FOUND
                    )
                )

                return@flow
            }

            val updatedWorkoutDetails = workoutDetailsWithExercises.workoutDetails.copy(
                workoutDetailsId = workout.workoutId,
                name = workout.name,
                description = workoutDetailsWithExercises.workoutDetails.description,
                muscleGroup = workout.muscleGroup,
                isSelected = workout.isSelected
            )

            workoutDetailsDao.updateWorkoutDetails(updatedWorkoutDetails)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeSmallDelay)

            val workout = WorkoutDto()

            val workoutId = workoutDao.insertWorkout(workout.toEntity()) //Get workout id
            val workoutName = "Workout $workoutId"

            val workoutUpdatedName =
                workout.copy(workoutId = workoutId.toInt(), name = workoutName).toEntity()
            workoutDao.updateWorkout(workoutUpdatedName) //Update workout name

            val workoutDetails =
                WorkoutDetailsDto().copy(workoutId = workoutId.toInt(), name = workoutName)
            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetails.toEntity()) //returns 0

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
            delay(Constants.fakeSmallDelay)

            val workoutId = workoutDao.insertWorkout(workoutDto.toEntity()) //Get workout id

            //Create duplicate WorkoutDetails with no exercises and sets...
            val workoutDetailsDto =
                WorkoutDetailsDto().copy(
                    workoutId = workoutId.toInt(),
                    name = workoutDto.name,
                    description = "",
                    muscleGroup = workoutDto.muscleGroup,
                    isSelected = workoutDto.isSelected,
                )
            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity()) //returns 0

            workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity())

            //Select
            if (workoutDto.isSelected) {
                selectWorkout(workoutDto.workoutId).collect() //Await...
            }

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
            delay(Constants.fakeSmallDelay)

            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity()) //Get workout details id

            //Create Workout for the WorkoutDetails
            val workoutDto =
                WorkoutDto().copy(
                    workoutId = workoutDetailsId.toInt(),
                    name = workoutDetailsDto.name,
                    muscleGroup = workoutDetailsDto.muscleGroup,
                    isSelected = workoutDetailsDto.isSelected,
                    totalExercises = workoutDetailsDto.exercises.size,
                    snapshot = "snapshot $workoutDetailsId.png"
                )
            val workoutId = workoutDao.insertWorkout(workoutDto.toEntity()) //returns 0

            //Insert all exercises and sets
            workoutDetailsDto.exercises.forEach { exercise ->
                insertExercise(
                    exercise = exercise.toEntity(),
                    sets = exercise.sets.map { set ->
                        set.toEntity()
                    }
                )
            }

            //Select
            if (workoutDetailsDto.isSelected) {
                selectWorkout(workoutDetailsDto.workoutId).collect()
            }

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

            try {
                val deletedExercise = exerciseDao.getExerciseWithSets(
                    exerciseId = exerciseId,
                    workoutId = workoutId
                )

                exerciseDao.deleteExercise(deletedExercise.exercise)
            } catch (e: NoSuchElementException) {

                //Exercise not found
                emit(ResultWrapper.ApiError(KareError.EXERCISE_NOT_FOUND))
                return@flow
            }

            //Fetch workout details after exercise was deleted
            val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            workoutDetails ?: run {
                emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
                return@flow
            }

            //Add sets to all exercises
            val updatedWorkoutDetails = workoutDetails.toDto()
            val updatedExercisesWithSets = updatedWorkoutDetails.exercises.map { exercise ->
                exerciseDao.getExerciseWithSets(
                    exerciseId = exercise.exerciseId,
                    workoutId = exercise.workoutId
                ).toDto()
            }
            val updatedWorkoutDetailsWithSets = updatedWorkoutDetails.copy(exercises = updatedExercisesWithSets)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
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
            val updatedExercise = exercise.copy(workoutId = workoutId)

            insertExercise(
                exercise = updatedExercise.toEntity(),
                sets = updatedExercise.sets.map { set ->
                    set.toEntity()
                }
            )

            //Fetch workout details after exercise was inserted
            val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            workoutDetails ?: run {
                emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
                return@flow
            }

            //Add sets to all exercises
            val updatedWorkoutDetails = workoutDetails.toDto()
            val updatedExercisesWithSets = updatedWorkoutDetails.exercises.map { exercise ->
                exerciseDao.getExerciseWithSets(
                    exerciseId = exercise.exerciseId,
                    workoutId = exercise.workoutId
                ).toDto()
            }
            val updatedWorkoutDetailsWithSets = updatedWorkoutDetails.copy(exercises = updatedExercisesWithSets)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun submitExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        //Find current entry
        val workoutDetailsWithExercises = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetailsWithExercises ?: run {
            emit(ResultWrapper.ApiError(KareError.WORKOUT_DETAILS_NOT_FOUND))
            return@flow
        }

//        val exercisesInDB = workoutDetailsWithExercises.toDto().exercises
//        val exerciseDbEntry = exercisesInDB.firstOrNull { it.exerciseId == exercise.exerciseId }
//        exerciseDbEntry ?: run {
//            emit(ResultWrapper.ApiError(KareError.EXERCISE_NOT_FOUND))
//            return@flow
//        }
//
//        //Find the position in list of current exercise DB entry and delete
//        val exercisePosition = exercisesInDB.indexOfFirst{ it.exerciseId == exercise.exerciseId } //Get position of removed exercise...
//        exercisesInDB.removeAll { it.exerciseId == exercise.exerciseId }
//
//        //Add new exercise to deleted position
//        val updatedExercise = exercise.copy(workoutId = workoutId)
//        exercisesInDB.add(exercisePosition, updatedExercise)

        try {
            val exerciseWithSetsInDB = exerciseDao.getExerciseWithSets(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId
            )

            updateExercise(
                setsInDB = exerciseWithSetsInDB.sets,
                newExercise = exercise.toEntity(),
                newSets = exercise.sets.map { set ->
                    set.toEntity()
                }
            )
        } catch (e: NoSuchElementException) {

            //Exercise not found
            emit(ResultWrapper.ApiError(KareError.EXERCISE_NOT_FOUND))
            return@flow
        }

        val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetails ?: run {
            emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
            return@flow
        }

        //Add sets to all exercises
        val updatedWorkoutDetails = workoutDetails.toDto()
        val updatedExercisesWithSets = updatedWorkoutDetails.exercises.map { exercise ->
            exerciseDao.getExerciseWithSets(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId
            ).toDto()
        }
        val updatedWorkoutDetailsWithSets = updatedWorkoutDetails.copy(exercises = updatedExercisesWithSets)

        val result = WorkoutDetailsWrapper(
            WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
        )

        emit(ResultWrapper.Success(result))
    }

    private fun findMissingSets(
        oldSets: List<ExerciseSet>,
        newSets: List<ExerciseSet>
    ): List<ExerciseSet> {
        val oldSetIds = oldSets.map { it.setId }.toSet()  //Convert to set for faster lookup
        return newSets.filter { it.setId !in oldSetIds }  //Filter new sets to find ones not in old set ids
    }

    override suspend fun getWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<WorkoutConfigurationWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutConfigurationDao.getWorkoutConfiguration(workoutId)
            data ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_CONFIGURATION_NOT_FOUND))
                return@flow
            }

            val result = WorkoutConfigurationWrapper(
                WorkoutConfigurationResponse(data.toDto())
            )

            emit(ResultWrapper.Success(result))
        }


    override suspend fun updateWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Check for entry
            try {

                //Checking if entry is in DB -> update
                workoutConfigurationDao.getWorkoutConfiguration(workoutConfiguration.workoutId)
                    ?: throw NoSuchElementException("Workout configuration not found in DB.")

                workoutConfigurationDao.updateWorkoutConfiguration(workoutConfiguration.toEntity())
            } catch (e: NoSuchElementException) {
                workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity()) //Entry is not in DB -> save
            }

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun saveWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<WorkoutConfigurationWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val id =
                workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity())
                    .toInt()

            val result = WorkoutConfigurationWrapper(
                WorkoutConfigurationResponse(workoutConfiguration)
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun deleteWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutConfigurationDao.deleteWorkoutConfiguration(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }
}