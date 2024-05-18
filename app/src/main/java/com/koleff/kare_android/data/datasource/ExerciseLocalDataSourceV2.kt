package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class ExerciseLocalDataSourceV2 @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val exerciseDetailsDao: ExerciseDetailsDao,
    private val exerciseSetDao: ExerciseSetDao
) : ExerciseDataSource {

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
                    ExerciseResponse(data.toDto())
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
                    ExerciseResponse(data.toDto())
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
                exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)
            } else {
                exerciseDao.getCatalogExercises(
                    workoutId = Constants.CATALOG_EXERCISE_ID,
                    muscleGroup = MuscleGroup.fromId(muscleGroupId)
                )
            }

            val result = ExerciseListWrapper(
                GetExercisesResponse(
                    data.map {
                        it.toDto(
                            sets = emptyList()
                        )
                    }
                )
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

            val exerciseDetails =
                exerciseDetailsDao.getExerciseDetailsByExerciseAndWorkoutId(exerciseId, workoutId)

            val result = ExerciseDetailsWrapper(
                ExerciseDetailsResponse(
                    exerciseDetails.toDto()
                )
            )

            emit(ResultWrapper.Success(result))
        }


    override suspend fun deleteExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {

            //Update exercise
            try {
                val exerciseWithSets =
                    exerciseDao.getExerciseWithSets(
                        exerciseId = exerciseId,
                        workoutId = workoutId
                    )
                        .toDto()
                        .copy(sets = currentSets)

                val updatedSets = exerciseWithSets.sets.toMutableList()
                updatedSets.removeAll { it.setId == setId }

                //Update set numbers sequence
                updatedSets.forEachIndexed { index, _ ->
                    val updatedSet = updatedSets[index].copy(number = index + 1)

                    updatedSets[index] =
                        updatedSet //Update only the list. To update the DB use submitExercise.
                }

                val result = ExerciseWrapper(
                    ExerciseResponse(
                        exercise = exerciseWithSets.copy(sets = updatedSets)
                    )
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

    override suspend fun addNewExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            try {
                val nextSetNumber =
                    currentSets
                        .map { it.number }
                        .maxOf { it } + 1 //Find last set number

                val defaultReps = 12
                val defaultWeight = 0.0f
                val newSet = ExerciseSet(
                    setId = UUID.randomUUID(),
                    exerciseId = exerciseId,
                    workoutId = workoutId,
                    number = nextSetNumber,
                    reps = defaultReps,
                    weight = defaultWeight
                )

                val updatedSets = currentSets.toMutableList()
                updatedSets.add(newSet.toDto())

                //Update exercise
                val updatedExercise = exerciseDao.getExerciseWithSets(
                    exerciseId = exerciseId,
                    workoutId = workoutId
                )
                    .toDto()
                    .copy(
                        sets = updatedSets
                    )

                val result = ExerciseWrapper(
                    ExerciseResponse(
                        exercise = updatedExercise
                    )
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
}