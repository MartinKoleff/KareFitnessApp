package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.response.UserResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSets
import com.koleff.kare_android.domain.wrapper.UserWrapper
import com.koleff.kare_android.domain.wrapper.WorkoutDetailsWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class ExerciseLocalDataSource @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao
) : ExerciseDataSource {

    override suspend fun getExercises(workoutId: Int): Flow<ResultWrapper<ExerciseListWrapper>> =
        flow {
            //TODO: wire with workoutDao or remove...
        }

    override suspend fun getExercise(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data = exerciseDao.getExerciseWithSets(exerciseId, workoutId)

                val result = ExerciseWrapper(
                    ExerciseResponse(data.toExerciseDto())
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.EXERCISE_NOT_FOUND
                    )
                )
            }
        }

    override suspend fun getCatalogExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data = exerciseDao.getExerciseWithSets(
                    exerciseId,
                    Constants.CATALOG_EXERCISE_ID
                )

                val result = ExerciseWrapper(
                    ExerciseResponse(data.toExerciseDto())
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.EXERCISE_NOT_FOUND
                    )
                )
            }
        }

    override suspend fun getCatalogExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Fetch all exercises
            val data = if (MuscleGroup.fromId(muscleGroupId) == MuscleGroup.ALL) {
                exerciseDao.getAllExercises()
            } else {
                exerciseDao.getExercisesOrderedById(
                    MuscleGroup.fromId(muscleGroupId)
                )
            }

            val result = ExerciseListWrapper(
                GetExercisesResponse(data.map { it.toExerciseDto(sets = emptyList()) })
            )

            emit(ResultWrapper.Success(result))
        }


    override suspend fun getExerciseDetails(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Temporary setting the same description and videoUrl to all ExerciseDetails entities.
            //TODO: returns all sets for all exercises with same exerciseId...
            val data =
                exerciseDetailsDao.getExerciseDetailsByExerciseAndWorkoutId(exerciseId, workoutId)

            val result = ExerciseDetailsWrapper(
                ExerciseDetailsResponse(
                    ExerciseDetailsDto(
                        id = data.exerciseDetailsId,
                        name = data.name,
                        description = data.description,
                        muscleGroup = data.muscleGroup,
                        machineType = data.machineType,
                        videoUrl = data.videoUrl
                    )
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun deleteExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeSmallDelay)

            //Update exercise
            val selectedExerciseWitSets =
                exerciseDao.getExerciseWithSets(exerciseId, workoutId)
            val updatedSets = selectedExerciseWitSets.sets.toMutableList()
            updatedSets.removeAll { it.setId == setId }

            //Update Exercise - ExerciseSet cross ref
            val crossRef = ExerciseSetCrossRef(
                exerciseId = exerciseId,
                workoutId = workoutId,
                setId = setId
            )
            exerciseDao.deleteExerciseSetCrossRef(crossRef)

            val selectedExerciseDto = selectedExerciseWitSets.exercise.toExerciseDto(updatedSets)
            val result = ExerciseWrapper(
                ExerciseResponse(
                    exercise = selectedExerciseDto
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun addNewExerciseSet(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeSmallDelay)

            val defaultReps = 12
            val defaultWeight = 0.0f

            val selectedExerciseWitSets =
                exerciseDao.getExerciseWithSets(exerciseId, workoutId)
            val nextSetNumber =
                selectedExerciseWitSets.sets.map { it.number }
                    .maxOf { it } //Find last set number...
            val newSet = ExerciseSet(
                setId = UUID.randomUUID(),
                workoutId = selectedExerciseWitSets.exercise.workoutId,
                exerciseId = selectedExerciseWitSets.exercise.exerciseId,
                number = nextSetNumber,
                reps = defaultReps,
                weight = defaultWeight
            )

            //Save new generated set
            exerciseSetDao.saveSet(newSet)

            //Update Exercise - ExerciseSet cross ref
            val crossRef = ExerciseSetCrossRef(
                exerciseId = exerciseId,
                workoutId = workoutId,
                setId = newSet.setId
            )
            exerciseDao.insertExerciseSetCrossRef(crossRef)

            //Update exercise
            val updatedSets = selectedExerciseWitSets.sets.toMutableList()
            updatedSets.add(newSet)
            val selectedExerciseDto = selectedExerciseWitSets.exercise.toExerciseDto(updatedSets)
            val result = ExerciseWrapper(
                ExerciseResponse(
                    exercise = selectedExerciseDto
                )
            )

            emit(ResultWrapper.Success(result))
        }
}