//package com.koleff.kare_android.data.datasource
//
//import android.util.Log
//import com.koleff.kare_android.common.Constants
//import com.koleff.kare_android.data.model.dto.ExerciseDto
//import com.koleff.kare_android.data.model.dto.ExerciseSetDto
//import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
//import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
//import com.koleff.kare_android.data.model.dto.WorkoutDto
//import com.koleff.kare_android.data.model.response.WorkoutDetailsListResponse
//import com.koleff.kare_android.data.model.response.WorkoutsListResponse
//import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
//import com.koleff.kare_android.data.model.response.WorkoutResponse
//import com.koleff.kare_android.data.model.response.SelectedWorkoutResponse
//import com.koleff.kare_android.data.model.response.WorkoutConfigurationResponse
//import com.koleff.kare_android.data.model.response.base_response.BaseResponse
//import com.koleff.kare_android.data.model.response.base_response.KareError
//import com.koleff.kare_android.domain.wrapper.WorkoutListWrapper
//import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
//import com.koleff.kare_android.domain.wrapper.WorkoutWrapper
//import com.koleff.kare_android.domain.wrapper.ResultWrapper
//import com.koleff.kare_android.domain.wrapper.ServerResponseData
//import com.koleff.kare_android.data.room.dao.ExerciseDao
//import com.koleff.kare_android.data.room.dao.ExerciseSetDao
//import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
//import com.koleff.kare_android.data.room.dao.WorkoutDao
//import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
//import com.koleff.kare_android.data.room.entity.ExerciseSet
//import com.koleff.kare_android.data.room.entity.Workout
//import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
//import com.koleff.kare_android.data.room.entity.ExerciseWithSets
//import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
//import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef
//import com.koleff.kare_android.domain.wrapper.WorkoutDetailsListWrapper
//import com.koleff.kare_android.domain.wrapper.SelectedWorkoutWrapper
//import com.koleff.kare_android.domain.wrapper.WorkoutConfigurationWrapper
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.collect
//import kotlinx.coroutines.flow.flow
//import java.util.UUID
//import javax.inject.Inject
//
//@Deprecated("Cross refs version. Entities updated to automatically set up cross refs.")
//class WorkoutLocalDataSource @Inject constructor(
//    private val workoutDao: WorkoutDao,
//    private val exerciseDao: ExerciseDao,
//    private val workoutDetailsDao: WorkoutDetailsDao,
//    private val exerciseSetDao: ExerciseSetDao,
//    private val workoutConfigurationDao: WorkoutConfigurationDao
//) : WorkoutDataSource {
//    override suspend fun selectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            //Deselect current selected workout
//            val selectedWorkoutInDB = workoutDao.getWorkoutByIsSelected()?.copy(
//                isSelected = false
//            )
//
//            //If you are trying to select the current selected workout
//            if (selectedWorkoutInDB?.workoutId == workoutId) {
//                emit(ResultWrapper.Success(result))
//                return@flow
//            }
//
//            //Update DB
//            selectedWorkoutInDB?.let {
//                updateWorkout(it.toDto()).collect()
//            }
//
//            workoutDao.selectWorkoutById(workoutId)
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun deselectWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            //Deselect workout
//            val workout = workoutDao.getWorkoutById(workoutId).copy(
//                isSelected = false
//            )
//
//            //Update DB
//            updateWorkout(workout.toDto()).collect()
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun getSelectedWorkout(): Flow<ResultWrapper<SelectedWorkoutWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val data = workoutDao.getWorkoutByIsSelected()
//
//            val result = SelectedWorkoutWrapper(
//                SelectedWorkoutResponse(data?.toDto())
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun getWorkout(workoutId: Int): Flow<ResultWrapper<WorkoutWrapper>> = flow {
//        emit(ResultWrapper.Loading())
//        delay(Constants.fakeDelay)
//
//        try {
//            val data = workoutDao.getWorkoutById(workoutId)
//            val result = WorkoutWrapper(
//                WorkoutResponse(data.toDto())
//            )
//
//            emit(ResultWrapper.Success(result))
//        } catch (e: NoSuchElementException) {
//            emit(
//                ResultWrapper.ApiError(
//                    error = KareError.WORKOUT_NOT_FOUND
//                )
//            )
//        }
//    }
//
//    override suspend fun getAllWorkouts(): Flow<ResultWrapper<WorkoutListWrapper>> = flow {
//        emit(ResultWrapper.Loading())
//        delay(Constants.fakeDelay)
//
//        val data = workoutDao.getWorkoutsOrderedById()
//
//        val result = WorkoutListWrapper(
//            WorkoutsListResponse(data.map(Workout::toDto))
//        )
//
//        emit(ResultWrapper.Success(result))
//    }
//
//    override suspend fun getAllWorkoutDetails(): Flow<ResultWrapper<WorkoutDetailsListWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val data = workoutDetailsDao.getWorkoutDetailsOrderedById()
//
//            val updatedData = data.map { workoutDetailsWitExercises ->
//
//                //Null safety
//                workoutDetailsWitExercises.exercises ?: return@flow
//
//                val exercises =
//                    workoutDetailsWitExercises.exercises.map { exercise ->
//                        val sets = exerciseDao.getSetsForExercise(
//                            exerciseId = exercise.exerciseId,
//                            workoutId = workoutDetailsWitExercises.workoutDetails.workoutDetailsId
//                        )
//
//                        exercise.toDto(sets)
//                    } as MutableList
//
//                val configuration =
//                    workoutDetailsWitExercises.configuration?.toDto() ?: WorkoutConfigurationDto(
//                        workoutId = workoutDetailsWitExercises.workoutDetails.workoutDetailsId
//                    )
//
//                workoutDetailsWitExercises.workoutDetails.toDto(
//                    exercises = exercises,
//                    configuration = configuration
//                )
//            }
//
//            val result = WorkoutDetailsListWrapper(
//                WorkoutDetailsListResponse(updatedData)
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun getWorkoutDetails(workoutId: Int): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)
//            data ?: run {
//                emit(ResultWrapper.ApiError())
//                return@flow
//            }
//
//            //Add sets from DB relations
//            val exercisesWithSetsList = mutableListOf<ExerciseWithSets>()
//
//            for (exercise in data.safeExercises) {
//                val exercisesWithSet = exerciseDao.getExerciseWithSets(
//                    exerciseId = exercise.exerciseId,
//                    workoutId = data.workoutDetails.workoutDetailsId
//                )
//
//                exercisesWithSetsList.add(exercisesWithSet)
//            }
//
//            val exercisesWithSetsDto: MutableList<ExerciseDto> =
//                exercisesWithSetsList.map(ExerciseWithSets::toDto).toMutableList()
//            val configuration = data.configuration?.toDto() ?: WorkoutConfigurationDto(
//                workoutId = data.workoutDetails.workoutDetailsId
//            )
//            val workout = data.workoutDetails.toDto(
//                exercises = exercisesWithSetsDto,
//                configuration = configuration
//            )
//
//            val result = WorkoutDetailsWrapper(
//                WorkoutDetailsResponse(workout)
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    private suspend fun getWorkoutExercises(workoutId: Int): List<ExerciseDto> {
//        val workoutDetailsWitExercises = workoutDetailsDao.getWorkoutDetailsById(workoutId)
//        workoutDetailsWitExercises ?: run {
//            return emptyList()
//        }
//
//        val exercises = workoutDetailsWitExercises.safeExercises
//
//        val exercisesWithSetsList = mutableListOf<ExerciseWithSets>()
//        for (exercise in exercises) {
//            val exercisesWithSet = exerciseDao.getExerciseWithSets(
//                exerciseId = exercise.exerciseId,
//                workoutId = workoutDetailsWitExercises.workoutDetails.workoutDetailsId
//            )
//
//            exercisesWithSetsList.add(exercisesWithSet)
//        }
//        val exercisesWithSetsDto: MutableList<ExerciseDto> =
//            exercisesWithSetsList.map(ExerciseWithSets::toDto).toMutableList()
//
//        return exercisesWithSetsDto
//    }
//
//    //No need for separate deleteWorkoutDetails functionality. When workout is deleted the workout details should be deleted also.
//    override suspend fun deleteWorkout(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            workoutDao.deleteWorkout(workoutId)
//            workoutDetailsDao.deleteWorkoutDetails(workoutId)
//            workoutConfigurationDao.deleteWorkoutConfiguration(workoutId)
//
//            //TODO: [Test] if exerciseSet - workoutDetails cross ref is also deleted...
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun updateWorkoutDetails(workoutDetails: WorkoutDetailsDto): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val workoutId = workoutDetails.workoutId
//            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)
//
//            //If no WorkoutDetails found -> return error
//            data ?: run {
//                emit(ResultWrapper.ApiError())
//                return@flow
//            }
//
//            //Contains different exercises
//            val currentDB =
//                data.safeExercises.map { exercise ->
//
//                    val sets = try {
//                        exerciseDao.getSetsForExercise(
//                            exerciseId = exercise.exerciseId,
//                            workoutId = data.workoutDetails.workoutDetailsId
//                        )
//                    } catch (e: NoSuchElementException) {
//                        emptyList()
//                    }
//                    exercise.toDto(sets)
//                }
//
//            //WorkoutDetails -> Exercise cross refs
//            if (currentDB.size <= workoutDetails.exercises.size) {
//                val newExercises =
//                    workoutDetails.exercises
//                        .filterNot { currentDB.contains(it) }
//                        .distinct()
//                Log.d("UpdateWorkoutDetails", "New exercises: $newExercises")
//
//                val exerciseIds = newExercises.map { it.exerciseId }
//
//                setupWorkoutDetailsExerciseCrossRefs(workoutId, exerciseIds)
//
//                //Exercise -> Exercise sets cross refs
//                setupExerciseSetCrossRef(workoutDetails)
//            }
//
//            //Update total exercises, name, muscle group and isSelected
//            val workoutEntry = workoutDao.getWorkoutById(workoutId).copy(
//                name = workoutDetails.name,
//                totalExercises = workoutDetails.exercises.size,
//                muscleGroup = workoutDetails.muscleGroup,
//                isSelected = workoutDetails.isSelected,
//            )
//            workoutDao.updateWorkout(workoutEntry) //if update is not working -> invalid id is provided
//
//            workoutDetailsDao.updateWorkoutDetails(workoutDetails.toEntity())
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    /**
//     * Helping functions
//     */
//
//    private suspend fun setupWorkoutDetailsExerciseCrossRefs(
//        workoutId: Int,
//        exerciseIds: List<Int>
//    ) {
//        val crossRefs = exerciseIds.map { exerciseId ->
//            WorkoutDetailsExerciseCrossRef(
//                workoutDetailsId = workoutId,
//                exerciseId = exerciseId
//            )
//        }
//        workoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(crossRefs)
//    }
//
//    private suspend fun setupExerciseSetCrossRef(workoutDetails: WorkoutDetailsDto) {
//        val exerciseSetCrossRefs: List<ExerciseSetCrossRef> =
//            workoutDetails.exercises.flatMap { exercise ->
//                exercise.sets.map { set ->
//                    val exerciseSet = set.toEntity()
//
//                    if (set.setId == null) {
//
//                        //New set -> insert it
//                        val newSetId = UUID.randomUUID()
//                        exerciseSet.setId = newSetId
//
//                        exerciseSetDao.saveSet(exerciseSet)
//                    } else {
//
//                        //Existing set -> update it
//                        Log.d(
//                            "UpdateWorkoutDetails",
//                            "Exercise set with setId ${exerciseSet.setId} added. Data: $exerciseSet"
//                        )
//
//                        //Trying to add set with already generated id
//                        try {
//                            exerciseSetDao.getSetById(exerciseSet.setId) //Checking if entry is in DB -> update
//
//                            exerciseSetDao.updateSet(exerciseSet)
//                        } catch (e: NoSuchElementException) {
//                            exerciseSetDao.saveSet(exerciseSet) //Entry is not in DB -> save
//                        }
//                    }
//
//                    ExerciseSetCrossRef(
//                        exerciseId = exercise.exerciseId,
//                        workoutId = workoutDetails.workoutId,
//                        setId = exerciseSet.setId
//                    )
//                }
//            }
//
//        exerciseDao.insertAllExerciseSetCrossRef(exerciseSetCrossRefs)
//    }
//
//    private suspend fun setupWorkoutDetailsWorkoutCrossRef(workoutId: Int, workoutDetailsId: Int) {
//        Log.d(
//            "CreateWorkout",
//            "WorkoutId: $workoutId, WorkoutDetailsId: $workoutDetailsId"
//        )
//        val crossRef = WorkoutDetailsWorkoutCrossRef(
//            workoutDetailsId = workoutId,
//            workoutId = workoutDetailsId
//        )
//        workoutDao.insertWorkoutDetailsWorkoutCrossRef(crossRef)
//    }
//
//    override suspend fun updateWorkout(
//        workout: WorkoutDto
//    ): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            workoutDao.updateWorkout(workout.toEntity())
//
//            //Update duplicate data between Workout and WorkoutDetails...
//            val workoutDetailsWithExercises =
//                workoutDetailsDao.getWorkoutDetailsById(workout.workoutId)
//
//            //If no WorkoutDetails found -> return error
//            workoutDetailsWithExercises ?: emit(ResultWrapper.ApiError())
//
//            val updatedWorkoutDetails = workoutDetailsWithExercises!!.workoutDetails.copy(
//                workoutDetailsId = workout.workoutId,
//                name = workout.name,
//                description = workoutDetailsWithExercises.workoutDetails.description,
//                muscleGroup = workout.muscleGroup,
//                isSelected = workout.isSelected
//            )
//
//            workoutDetailsDao.updateWorkoutDetails(updatedWorkoutDetails)
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun createNewWorkout(): Flow<ResultWrapper<WorkoutWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val workout = WorkoutDto()
//
//            val workoutId = workoutDao.insertWorkout(workout.toEntity()) //Get workout id
//            val workoutName = "Workout $workoutId"
//
//            val workoutUpdatedName =
//                workout.copy(workoutId = workoutId.toInt(), name = workoutName).toEntity()
//            workoutDao.updateWorkout(workoutUpdatedName) //Update workout name
//
//            val workoutDetails =
//                WorkoutDetailsDto().copy(workoutId = workoutId.toInt(), name = workoutName)
//            val workoutDetailsId =
//                workoutDetailsDao.insertWorkoutDetails(workoutDetails.toEntity()) //returns 0
//
//            //WorkoutDetails - Workout cross refs
//            setupWorkoutDetailsWorkoutCrossRef(workoutId.toInt(), workoutDetailsId.toInt())
//
//            val result = WorkoutWrapper(
//                WorkoutResponse(
//                    workout.copy(workoutId = workoutId.toInt(), name = workoutName)
//                )
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun createCustomWorkout(workoutDto: WorkoutDto): Flow<ResultWrapper<WorkoutWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val workoutId = workoutDao.insertWorkout(workoutDto.toEntity()) //Get workout id
//
//            //Create duplicate WorkoutDetails with no exercises and sets...
//            val workoutDetailsDto =
//                WorkoutDetailsDto().copy(
//                    workoutId = workoutId.toInt(),
//                    name = workoutDto.name,
//                    description = "",
//                    muscleGroup = workoutDto.muscleGroup,
//                    isSelected = workoutDto.isSelected,
//                )
//            val workoutDetailsId =
//                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity()) //returns 0
//
//            //WorkoutDetails - Workout cross refs
//            setupWorkoutDetailsWorkoutCrossRef(workoutId.toInt(), workoutDetailsId.toInt())
//
//            workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity())
//
//            //Select
//            if (workoutDto.isSelected) {
//                selectWorkout(workoutDto.workoutId).collect() //Await...
//            }
//
//            val result = WorkoutWrapper(
//                WorkoutResponse(
//                    workoutDto.copy(workoutId = workoutId.toInt())
//                )
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun createCustomWorkoutDetails(workoutDetailsDto: WorkoutDetailsDto): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val workoutDetailsId =
//                workoutDetailsDao.insertWorkoutDetails(workoutDetailsDto.toEntity()) //Get workout details id
//
//            //Create Workout for the WorkoutDetails...
//            val workoutDto =
//                WorkoutDto().copy(
//                    workoutId = workoutDetailsId.toInt(),
//                    name = workoutDetailsDto.name,
//                    muscleGroup = workoutDetailsDto.muscleGroup,
//                    isSelected = workoutDetailsDto.isSelected,
//                    totalExercises = workoutDetailsDto.exercises.size,
//                    snapshot = "snapshot $workoutDetailsId.png"
//                )
//            val workoutId = workoutDao.insertWorkout(workoutDto.toEntity()) //returns 0
//
//            //WorkoutDetails - Workout cross refs
//            setupWorkoutDetailsWorkoutCrossRef(workoutId.toInt(), workoutDetailsId.toInt())
//
//            //WorkoutDetails -> Exercise cross refs
//            val exercises =
//                workoutDetailsDto.exercises.distinct()
//            Log.d("CreateCustomWorkoutDetails", "Exercises: $exercises")
//
//            val exerciseIds = exercises.map { it.exerciseId }
//
//            setupWorkoutDetailsExerciseCrossRefs(workoutDetailsId.toInt(), exerciseIds)
//
//            //Exercise - ExerciseSet cross refs
//            setupExerciseSetCrossRef(workoutDetailsDto)
//
//            //Add exercises
//            exercises.forEach { exercise ->
//                exerciseDao.insertExercise(exercise.toEntity())
//            }
//
//            //Select
//            if (workoutDetailsDto.isSelected) {
//                selectWorkout(workoutDetailsDto.workoutId).collect()
//            }
//
//            val result = WorkoutDetailsWrapper(
//                WorkoutDetailsResponse(
//                    workoutDetailsDto.copy(workoutId = workoutDetailsId.toInt())
//                )
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun deleteExercise(
//        workoutId: Int,
//        exerciseId: Int
//    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)
//
//            data ?: run {
//                emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
//                return@flow
//            }
//
//            //Delete exercise - set cross ref
//            val deletedExercise = exerciseDao.getExerciseWithSets(
//                exerciseId = exerciseId,
//                workoutId = workoutId
//            )
//            for (set in deletedExercise.sets) {
//                val exerciseSetCrossRef =
//                    ExerciseSetCrossRef(
//                        exerciseId = exerciseId,
//                        workoutId = workoutId,
//                        setId = set.setId
//                    )
//                exerciseDao.deleteExerciseSetCrossRef(exerciseSetCrossRef)
//                exerciseSetDao.deleteSet(set)
//            }
//
//            //Delete exercise
//            exerciseDao.deleteExercise(deletedExercise.exercise)
//
//            //Delete workout details - exercise cross ref
//            val workoutDetailsExerciseCrossRef =
//                WorkoutDetailsExerciseCrossRef(
//                    workoutDetailsId = workoutId,
//                    exerciseId = exerciseId
//                )
//            workoutDetailsDao.deleteWorkoutDetailsExerciseCrossRef(workoutDetailsExerciseCrossRef)
//
//            //Update exercise list
//            val exerciseEntityList =
//                data.safeExercises.filterNot { exercise -> exercise.exerciseId == exerciseId }
//
//            val exercisesDtoList: MutableList<ExerciseDto> =
//                exerciseEntityList.map { exercise ->
//                    val sets =
//                        exerciseDao.getSetsForExercise(
//                            exerciseId = exercise.exerciseId,
//                            workoutId = data.workoutDetails.workoutDetailsId
//                        )
//
//                    exercise.toDto(sets)
//                } as MutableList
//
//            val updatedWorkout = data.copy(exercises = exerciseEntityList)
//            val configuration = data.configuration?.toDto() ?: WorkoutConfigurationDto(
//                workoutId = data.workoutDetails.workoutDetailsId
//            )
//            val updatedWorkoutDto =
//                updatedWorkout.workoutDetails.toDto(
//                    exercises = exercisesDtoList,
//                    configuration = configuration
//                )
//
//            //Update total exercises
//            val workout = workoutDao.getWorkoutById(workoutId)
//            workout.totalExercises = updatedWorkout.safeExercises.size
//            workoutDao.updateWorkout(workout) //if update is not working -> invalid id is provided
//
//            val result = WorkoutDetailsWrapper(
//                WorkoutDetailsResponse(updatedWorkoutDto)
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun addExercise(
//        workoutId: Int,
//        exercise: ExerciseDto
//    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//            val updatedExercise = exercise.copy(workoutId = workoutId)
//
//            val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)
//
//            data ?: run {
//                emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
//                return@flow
//            }
//
//            val exerciseDtoList =
//                data.safeExercises.map { exercise ->
//
//                    val sets = try {
//                        exerciseDao.getSetsForExercise(
//                            exerciseId = exercise.exerciseId,
//                            workoutId = exercise.workoutId
//                        )
//                    } catch (e: NoSuchElementException) {
//                        emptyList()
//                    }
//                    exercise.toDto(sets)
//                } as MutableList
//
//            //Add new exercise
//            exerciseDtoList.add(updatedExercise)
//
//            val exerciseEntityList = exerciseDtoList.map { it.toEntity() }
//
//            val updatedWorkout = data.copy(exercises = exerciseEntityList)
//            val configuration = data.configuration?.toDto() ?: WorkoutConfigurationDto(
//                workoutId = data.workoutDetails.workoutDetailsId
//            )
//            val updatedWorkoutDto =
//                updatedWorkout.workoutDetails.toDto(
//                    exercises = exerciseDtoList,
//                    configuration = configuration
//                )
//
//            //Create workout details - exercise cross ref
//            val workoutDetailsExerciseCrossRef =
//                WorkoutDetailsExerciseCrossRef(
//                    workoutDetailsId = workoutId,
//                    exerciseId = exercise.exerciseId
//                )
//            workoutDetailsDao.insertWorkoutDetailsExerciseCrossRef(workoutDetailsExerciseCrossRef)
//
//            //Create exercise - set cross ref
//            for (set in exercise.sets) {
//                val exerciseSet = set.toEntity()
//                exerciseSetDao.saveSet(exerciseSet)
//
//                val exerciseSetCrossRef =
//                    ExerciseSetCrossRef(
//                        exerciseId = exercise.exerciseId,
//                        workoutId = workoutId,
//                        setId = exerciseSet.setId
//                    )
//                exerciseDao.insertExerciseSetCrossRef(exerciseSetCrossRef)
//            }
//
//            //Insert exercise
//            exerciseDao.insertExercise(exercise.toEntity())
//
//            //Update total exercises
//            val workout = workoutDao.getWorkoutById(workoutId)
//            workout.totalExercises = updatedWorkout.safeExercises.size
//            workoutDao.updateWorkout(workout) //if update is not working -> invalid id is provided
//
//            val result = WorkoutDetailsWrapper(
//                WorkoutDetailsResponse(updatedWorkoutDto)
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun submitExercise(
//        workoutId: Int,
//        exercise: ExerciseDto
//    ): Flow<ResultWrapper<WorkoutDetailsWrapper>> = flow {
//        emit(ResultWrapper.Loading())
//        delay(Constants.fakeDelay)
//        val updatedExercise = exercise.copy(workoutId = workoutId)
//
//        val data = workoutDetailsDao.getWorkoutDetailsById(workoutId)
//
//        data ?: run {
//            emit(ResultWrapper.ApiError(error = KareError.WORKOUT_NOT_FOUND))
//            return@flow
//        }
//
//        val exerciseDtoList =
//            data.safeExercises.map { exercise ->
//
//                val sets = try {
//                    exerciseDao.getSetsForExercise(
//                        exerciseId = exercise.exerciseId,
//                        workoutId = exercise.workoutId
//                    )
//                } catch (e: NoSuchElementException) {
//                    emptyList()
//                }
//                exercise.toDto(sets)
//            } as MutableList
//
//        //Remove exercise current entry
//        val exerciseDbEntry = exerciseDtoList.firstOrNull { it.exerciseId == exercise.exerciseId }
//        val deletedSets = findMissingSets(exerciseDbEntry?.sets ?: emptyList(), exercise.sets) //Sets to delete...
//
//        val positionOldEntry = exerciseDtoList.indexOfFirst{ it.exerciseId == exercise.exerciseId } //Get position of removed exercise...
//        exerciseDtoList.removeAll { it.exerciseId == exercise.exerciseId }
//
//        //Delete Exercise - ExerciseSet cross refs of deleted sets
//        for (set in deletedSets) {
//            val exerciseSetCrossRef =
//                ExerciseSetCrossRef(
//                    exerciseId = exercise.exerciseId,
//                    workoutId = workoutId,
//                    setId = set.setId ?: continue
//                )
//            exerciseDao.deleteExerciseSetCrossRef(exerciseSetCrossRef)
//            exerciseSetDao.deleteSet(set.toEntity())
//        }
//
//        //Add new exercise
//        exerciseDtoList.add(positionOldEntry, updatedExercise) //Add new exercise to deleted position...
//
//        val exerciseEntityList = exerciseDtoList.map { it.toEntity() }
//
//        val updatedWorkout = data.copy(exercises = exerciseEntityList)
//        val configuration = data.configuration?.toDto() ?: WorkoutConfigurationDto(
//            workoutId = data.workoutDetails.workoutDetailsId
//        )
//        val updatedWorkoutDto =
//            updatedWorkout.workoutDetails.toDto(
//                exercises = exerciseDtoList,
//                configuration = configuration
//            )
//
//        //Insert exercise
//        exerciseDao.insertExercise(exercise.toEntity())
//
//        //Create workout details - exercise cross ref
//        val workoutDetailsExerciseCrossRef =
//            WorkoutDetailsExerciseCrossRef(
//                workoutDetailsId = workoutId,
//                exerciseId = exercise.exerciseId
//            )
//        workoutDetailsDao.insertWorkoutDetailsExerciseCrossRef(workoutDetailsExerciseCrossRef)
//
//        //Create exercise - set cross ref
//        for (set in exercise.sets) {
//            val exerciseSet = set.toEntity()
//            exerciseSetDao.saveSet(exerciseSet)
//
//            val exerciseSetCrossRef =
//                ExerciseSetCrossRef(
//                    exerciseId = exercise.exerciseId,
//                    workoutId = workoutId,
//                    setId = exerciseSet.setId
//                )
//            exerciseDao.insertExerciseSetCrossRef(exerciseSetCrossRef)
//        }
//
//        //Update total exercises -> if new exercise was provided
//        val workout = workoutDao.getWorkoutById(workoutId)
//        workout.totalExercises = updatedWorkout.safeExercises.size
//        workoutDao.updateWorkout(workout) //if update is not working -> invalid id is provided
//
//        val result = WorkoutDetailsWrapper(
//            WorkoutDetailsResponse(updatedWorkoutDto)
//        )
//
//        emit(ResultWrapper.Success(result))
//    }
//
//    private fun findMissingSets(oldSets: List<ExerciseSetDto>, newSets: List<ExerciseSetDto>): List<ExerciseSetDto> {
//        val oldSetIds = oldSets.map { it.setId }.toSet()  //Convert to set for faster lookup
//        return newSets.filter { it.setId !in oldSetIds }  //Filter new sets to find ones not in old set ids
//    }
//
//    override suspend fun getWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<WorkoutConfigurationWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val data = workoutConfigurationDao.getWorkoutConfiguration(workoutId)
//            data ?: run {
//                emit(ResultWrapper.ApiError(KareError.WORKOUT_CONFIGURATION_NOT_FOUND))
//                return@flow
//            }
//
//            val result = WorkoutConfigurationWrapper(
//                WorkoutConfigurationResponse(data.toDto())
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//
//    override suspend fun updateWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            //Check for entry
//            try {
//
//                //Checking if entry is in DB -> update
//                workoutConfigurationDao.getWorkoutConfiguration(workoutConfiguration.workoutId)
//                    ?: throw NoSuchElementException("Workout configuration not found in DB.")
//
//                workoutConfigurationDao.updateWorkoutConfiguration(workoutConfiguration.toEntity())
//            } catch (e: NoSuchElementException) {
//                workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity()) //Entry is not in DB -> save
//            }
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun saveWorkoutConfiguration(workoutConfiguration: WorkoutConfigurationDto): Flow<ResultWrapper<WorkoutConfigurationWrapper>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            val id =
//                workoutConfigurationDao.insertWorkoutConfiguration(workoutConfiguration.toEntity())
//                    .toInt()
//
//            val result = WorkoutConfigurationWrapper(
//                WorkoutConfigurationResponse(workoutConfiguration)
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//
//    override suspend fun deleteWorkoutConfiguration(workoutId: Int): Flow<ResultWrapper<ServerResponseData>> =
//        flow {
//            emit(ResultWrapper.Loading())
//            delay(Constants.fakeDelay)
//
//            workoutConfigurationDao.deleteWorkoutConfiguration(workoutId)
//
//            val result = ServerResponseData(
//                BaseResponse()
//            )
//
//            emit(ResultWrapper.Success(result))
//        }
//}