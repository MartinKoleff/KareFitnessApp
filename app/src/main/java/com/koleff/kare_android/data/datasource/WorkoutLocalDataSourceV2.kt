package com.koleff.kare_android.data.datasource

import android.util.Log
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.GetDuplicateExercisesResponse
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
import com.koleff.kare_android.domain.wrapper.DuplicateExercisesWrapper
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
    override suspend fun favoriteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.favoriteWorkoutById(workoutId)
            workoutDetailsDao.favoriteWorkoutDetailsById(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun unfavoriteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            workoutDao.unfavoriteWorkoutById(workoutId)
            workoutDetailsDao.unfavoriteWorkoutDetailsById(workoutId)

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getFavoriteWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = workoutDao.getWorkoutByIsFavorite()

            val result = WorkoutListWrapper(
                WorkoutsListResponse(data.map { it.toDto() })
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

        val workouts = workoutDao.getWorkoutsOrderedById().map { it.toDto() }
        val updatedWorkouts = removeCatalogWorkout(workouts)

        val result = WorkoutListWrapper(
            WorkoutsListResponse(
                updatedWorkouts
            )
        )

        emit(ResultWrapper.Success(result))
    }

    private fun removeCatalogWorkout(workouts: List<WorkoutDto>): List<WorkoutDto> {
        return workouts.filter { it.workoutId != Constants.CATALOG_EXERCISE_ID }
    }

    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutDetailsList =
                workoutDetailsDao.getWorkoutDetailsOrderedById().map { it.toDto() }
            val updatedWorkoutDetailsList = removeCatalogWorkoutDetails(workoutDetailsList)

            val result = WorkoutDetailsListWrapper(
                WorkoutDetailsListResponse(
                    updatedWorkoutDetailsList
                )
            )

            emit(ResultWrapper.Success(result))
        }

    private fun removeCatalogWorkoutDetails(workoutDetailsList: List<WorkoutDetailsDto>): List<WorkoutDetailsDto> {
        return workoutDetailsList.filter { it.workoutId != Constants.CATALOG_EXERCISE_ID }
    }


    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            workoutDetails ?: run {
                emit(ResultWrapper.ApiError(KareError.WORKOUT_DETAILS_NOT_FOUND))
                return@flow
            }

            //Add sets to all exercises
            val updatedWorkoutDetails = addSetsToWorkout(workoutDetails)

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

            val result = ServerResponseData(
                BaseResponse()
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
        flow {
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
                isFavorite = workoutDetails.isFavorite,
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

    @Throws(IllegalArgumentException::class)
    private suspend fun updateExercises(exercises: List<ExerciseDto>, workoutId: Int) {

        //Fetch exercises from DB
        val exercisesWithSetsInDB = workoutDetailsDao.getWorkoutDetailsById(workoutId)?.exercises
        exercisesWithSetsInDB ?: throw IllegalArgumentException("Invalid workout")

        exercises.forEach { exercise ->
            val existsInDB = exercisesWithSetsInDB.any { exerciseWithSetsInDB ->
                exerciseWithSetsInDB.exercise.exerciseId == exercise.exerciseId &&
                        exerciseWithSetsInDB.exercise.workoutId == exercise.workoutId
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

//        //Remove sets that are not in newSets
//        val setsToRemove = findMissingSets(
//            newSets,
//            setsInDB
//        )

        setsInDB.forEach { set ->
            exerciseSetDao.deleteSet(set)
        }

        //Add all newSets
        exerciseSetDao.insertAllExerciseSets(newSets)
    }

    @Throws(IllegalArgumentException::class)
    private suspend fun insertExercise(exercise: Exercise, sets: List<ExerciseSet>) {
        exerciseDao.insertExercise(exercise)
        exerciseSetDao.insertAllExerciseSets(sets)

        updateTotalExercises(exercise.workoutId)
    }

    private fun getTotalExercisesCount(workoutId: Int): Int {
        return workoutDetailsDao.getWorkoutDetailsById(workoutId)?.exercises?.size ?: 0
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
                isFavorite = workout.isFavorite
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
            delay(Constants.fakeDelay)

            val workout = WorkoutDto()

            val workoutId = workoutDao.insertWorkout(workout.toEntity()) //Get workout id
            val workoutName = "Workout $workoutId"

            val workoutUpdatedName =
                workout.copy(workoutId = workoutId.toInt(), name = workoutName).toEntity()
            workoutDao.updateWorkout(workoutUpdatedName) //Update workout name

            val workoutDetails =
                WorkoutDetailsDto(workoutId = workoutId.toInt(), name = workoutName)
            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetails.toEntity()) //returns 0

            //Create workout configuration
            val workoutConfiguration = WorkoutConfigurationDto(workoutId = workoutId.toInt())
            workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity())

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

            val workoutId = workoutDao.insertWorkout(workoutDto.toEntity()) //Get workout id

            //Create duplicate WorkoutDetails with no exercises and sets...
            val workoutDetailsDto =
                WorkoutDetailsDto().copy(
                    workoutId = workoutId.toInt(),
                    name = workoutDto.name,
                    description = "",
                    muscleGroup = workoutDto.muscleGroup,
                    isFavorite = workoutDto.isFavorite,
                )
            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity()) //returns 0

            workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity())

            //Create workout configuration
            val workoutConfiguration = WorkoutConfigurationDto(workoutId = workoutId.toInt())
            workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity())

            //Select
            if (workoutDto.isFavorite) {
                favoriteWorkout(workoutDto.workoutId).collect() //Await...
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
            delay(Constants.fakeDelay)

            val workoutDetailsId =
                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity()) //Get workout details id

            //Create Workout for the WorkoutDetails
            val workoutDto =
                WorkoutDto().copy(
                    workoutId = workoutDetailsId.toInt(),
                    name = workoutDetailsDto.name,
                    muscleGroup = workoutDetailsDto.muscleGroup,
                    isFavorite = workoutDetailsDto.isFavorite,
                    totalExercises = workoutDetailsDto.exercises.size,
                    snapshot = "snapshot $workoutDetailsId.png"
                )
            val workoutId = workoutDao.insertWorkout(workoutDto.toEntity()) //returns 0

            //Insert all exercises and sets
            workoutDetailsDto.exercises.forEach { exercise ->
                try {
                    insertExercise(
                        exercise = exercise.toEntity(),
                        sets = exercise.sets.map { set ->
                            set.toEntity()
                        }
                    )
                } catch (e: IllegalArgumentException) {

                    //Invalid workoutId
                    emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
                    return@flow
                }
            }

            //Create workout configuration
            val workoutConfiguration = WorkoutConfigurationDto(workoutId = workoutDetailsId.toInt())
            workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity())

            //Favorite
            if (workoutDetailsDto.isFavorite) {
                favoriteWorkout(workoutDetailsDto.workoutId).collect()
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
                val deletedExerciseWithSets = exerciseDao.getExerciseWithSets(
                    exerciseId = exerciseId,
                    workoutId = workoutId
                )

                exerciseDao.deleteExercise(deletedExerciseWithSets.exercise)
//                deletedExerciseWithSets.sets.forEach { exerciseSet ->
//                    exerciseSetDao.deleteSet(exerciseSet)
//                }

                //Update workout totalExercises
                updateTotalExercises(workoutId)
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
            val updatedWorkoutDetailsWithSets = addSetsToWorkout(workoutDetails)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun deleteMultipleExercises(
        workoutId: Int,
        exerciseIds: List<Int>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val exercisesWithSets = exerciseIds.map { exerciseId ->
                    exerciseDao.getExerciseWithSets(
                        exerciseId = exerciseId,
                        workoutId = workoutId
                    )
                }

                exercisesWithSets.forEach { exerciseWithSets ->
                    exerciseDao.deleteExercise(exerciseWithSets.exercise)
                }

                //Update workout totalExercises
                updateTotalExercises(workoutId)
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
            val updatedWorkoutDetailsWithSets = addSetsToWorkout(workoutDetails)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
            )

            emit(ResultWrapper.Success(result))
        }

    private suspend fun updateTotalExercises(workoutId: Int) {
        val totalExercisesForWorkout = getTotalExercisesCount(workoutId)
        val workout = workoutDao.getWorkoutById(workoutId)
            ?: throw IllegalArgumentException("Workout not found for workoutId $workoutId")

        val updatedWorkout = workout.copy(
            totalExercises = totalExercisesForWorkout
        )
        workoutDao.updateWorkout(updatedWorkout)
    }

    override suspend fun addExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)
            val updatedExercise = exercise.copy(workoutId = workoutId)

            //Validation
            if (exercise.workoutId != workoutId) emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))

            try {
                insertExercise(
                    exercise = updatedExercise.toEntity(),
                    sets = updatedExercise.sets.map { set ->
                        set.toEntity()
                    }
                )
            } catch (e: IllegalArgumentException) {

                //Invalid workoutId
                emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
                return@flow
            }

            //Fetch workout details after exercise was inserted
            val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            workoutDetails ?: run {
                emit(ResultWrapper.ApiError(error = KareError.WORKOUT_DETAILS_NOT_FOUND))
                return@flow
            }

            //Add sets to all exercises
            val updatedWorkoutDetailsWithSets = addSetsToWorkout(workoutDetails)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun addMultipleExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Validation
            exerciseList.forEach { exercise ->
                if (exercise.workoutId != workoutId) emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
            }

            val updatedExerciseList = exerciseList.map { exercise ->
                exercise.copy(workoutId = workoutId)
            }

            updatedExerciseList.forEach { updatedExercise ->
                try {

                    insertExercise(
                        exercise = updatedExercise.toEntity(),
                        sets = updatedExercise.sets.map { set ->
                            set.toEntity()
                        }
                    )
                } catch (e: IllegalArgumentException) {

                    //Invalid workoutId
                    emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
                    return@flow
                }
            }

            //Fetch workout details after exercise was inserted
            val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

            workoutDetails ?: run {
                emit(ResultWrapper.ApiError(error = KareError.WORKOUT_DETAILS_NOT_FOUND))
                return@flow
            }

            //Add sets to all exercises
            val updatedWorkoutDetailsWithSets = addSetsToWorkout(workoutDetails)

            val result = WorkoutDetailsWrapper(
                WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
            )

            emit(ResultWrapper.Success(result))
        }

    private suspend fun addSetsToWorkout(workoutDetails: WorkoutDetailsWithExercises): WorkoutDetailsDto {
        val updatedWorkoutDetails = workoutDetails.toDto()
        val updatedExercisesWithSets = updatedWorkoutDetails.exercises.map { exercise ->
            exerciseDao.getExerciseWithSets(
                exerciseId = exercise.exerciseId,
                workoutId = exercise.workoutId
            ).toDto()
        }
        val updatedWorkoutDetailsWithSets =
            updatedWorkoutDetails.copy(exercises = updatedExercisesWithSets)

        return updatedWorkoutDetailsWithSets
    }

    override suspend fun submitExercise(
        workoutId: Int,
        exercise: ExerciseDto
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        if (exercise.workoutId != workoutId) emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))

        //Find current entry
        val workoutDetailsWithExercises = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetailsWithExercises ?: run {
            emit(ResultWrapper.ApiError(KareError.WORKOUT_DETAILS_NOT_FOUND))
            return@flow
        }

        try {

            //Entry in DB exists -> update
            val exerciseWithSetsInDB = exerciseDao.getExerciseWithSets(
                exerciseId = exercise.exerciseId,
                workoutId = workoutId
            )

            updateExercise(
                setsInDB = exerciseWithSetsInDB.sets,
                newExercise = exercise.toEntity(),
                newSets = exercise.sets.map { set ->
                    set.toEntity()
                }
            )
        } catch (e: NoSuchElementException) {
//            emit(ResultWrapper.ApiError(KareError.EXERCISE_NOT_FOUND.apply {
//                extraMessage = e.message ?: ""
//            }))
//            return@flow

            //Exercise not found -> new entry...
            try {
                insertExercise(
                    exercise = exercise.toEntity(),
                    sets = exercise.sets.map { set ->
                        set.toEntity()
                    })
            } catch (e: IllegalArgumentException) {

                //Invalid workoutId
                emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
                return@flow
            }
        }

        val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetails ?: run {
            emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
            return@flow
        }

        //Add sets to all exercises
        val updatedWorkoutDetailsWithSets = addSetsToWorkout(workoutDetails)

        val result = WorkoutDetailsWrapper(
            WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun submitMultipleExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> = flow {
        emit(ResultWrapper.Loading())
        delay(Constants.fakeDelay)

        //Validation
        exerciseList.forEach { exercise ->
            if (exercise.workoutId != workoutId) emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
        }

        //Find current entry
        val workoutDetailsWithExercises = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetailsWithExercises ?: run {
            emit(ResultWrapper.ApiError(KareError.WORKOUT_DETAILS_NOT_FOUND))
            return@flow
        }

        exerciseList.forEach { exercise ->
            try {

                //Entry in DB exists -> update
                val exerciseWithSetsInDB = exerciseDao.getExerciseWithSets(
                    exerciseId = exercise.exerciseId,
                    workoutId = workoutId
                )

                updateExercise(
                    setsInDB = exerciseWithSetsInDB.sets,
                    newExercise = exercise.toEntity(),
                    newSets = exercise.sets.map { set ->
                        set.toEntity()
                    }
                )
            } catch (e: NoSuchElementException) {

                //Exercise not found -> new entry...
                try {
                    insertExercise(
                        exercise = exercise.toEntity(),
                        sets = exercise.sets.map { set ->
                            set.toEntity()
                        })
                } catch (e: IllegalArgumentException) {

                    //Invalid workoutId
                    emit(ResultWrapper.ApiError(KareError.INVALID_EXERCISE))
                    return@flow
                }
            }
        }

        val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetails ?: run {
            emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
            return@flow
        }

        //Add sets to all exercises
        val updatedWorkoutDetailsWithSets = addSetsToWorkout(workoutDetails)

        val result = WorkoutDetailsWrapper(
            WorkoutDetailsResponse(updatedWorkoutDetailsWithSets)
        )

        emit(ResultWrapper.Success(result))
    }

    override suspend fun findDuplicateExercises(
        workoutId: Int,
        exerciseList: List<ExerciseDto>
    ): Flow<ResultWrapper<DuplicateExercisesWrapper>> = flow {
        val workoutDetails = workoutDetailsDao.getWorkoutDetailsById(workoutId)

        workoutDetails ?: run {
            emit(ResultWrapper.ApiError(KareError.WORKOUT_DETAILS_NOT_FOUND))
            return@flow
        }

        val updatedWorkoutDetails = addSetsToWorkout(workoutDetails)

        val containsDuplicates: Boolean = findDuplicateExercises(
            workoutExerciseList = updatedWorkoutDetails.exercises,
            submittedExerciseList = exerciseList
        )

        val result = DuplicateExercisesWrapper(
            GetDuplicateExercisesResponse(
                containsDuplicates
            )
        )

        emit(ResultWrapper.Success(result))
    }

    private fun findDuplicateExercises(
        workoutExerciseList: List<ExerciseDto>,
        submittedExerciseList: List<ExerciseDto>
    ): Boolean {
        val workoutExerciseIds = workoutExerciseList
            .map { it.exerciseId }
            .toSet()

        return submittedExerciseList.any {
            workoutExerciseIds.contains(it.exerciseId)
        }
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