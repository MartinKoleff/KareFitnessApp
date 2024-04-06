package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.response.WorkoutDetailsResponse
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
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
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

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> =
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
                GetExercisesResponse(data.map(ExerciseWithSet::toExerciseDto))
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = exerciseDao.getExerciseById(exerciseId)

            val result = ExerciseWrapper(
                ExerciseResponse(data.toExerciseDto())
            )

            emit(ResultWrapper.Success(result))
        }


    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<ExerciseDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Temporary setting the same description and videoUrl to all ExerciseDetails entities.
            val data = exerciseDetailsDao.getExerciseDetailsById(exerciseId)

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
        setId: UUID
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeSmallDelay)

            //Update Exercise - ExerciseSet cross ref
            val crossRef = ExerciseSetCrossRef(
                exerciseId,
                setId
            )
            exerciseDao.deleteExerciseSetCrossRef(crossRef)

            //Update exercise
            val selectedExerciseWitSets = exerciseDao.getExerciseById(exerciseId)
            val updatedSets = selectedExerciseWitSets.sets as MutableList
            updatedSets.removeAll {it.setId == setId}

            val selectedExerciseDto = selectedExerciseWitSets.exercise.toExerciseDto(updatedSets)
            val result = ExerciseWrapper(
                ExerciseResponse(
                    exercise = selectedExerciseDto
                )
            )

            emit(ResultWrapper.Success(result))
        }

    override suspend fun addNewExerciseSet(
        exerciseId: Int
    ): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeSmallDelay)

            val defaultReps = 12
            val defaultWeight = 0.0f

            val selectedExerciseWitSets = exerciseDao.getExerciseById(exerciseId)
            val nextSetNumber =
                selectedExerciseWitSets.sets.map { it.number }.maxOf { it } //Find last set number...
            val newSet = ExerciseSet(
                setId = UUID.randomUUID(),
                number = nextSetNumber,
                reps = defaultReps,
                weight = defaultWeight
            )

            //Save new generated set
            exerciseSetDao.saveSet(newSet)

            //Update Exercise - ExerciseSet cross ref
            val crossRef = ExerciseSetCrossRef(
                exerciseId,
                newSet.setId
            )
            exerciseDao.insertExerciseSetCrossRef(crossRef)

            //Update exercise
            val updatedSets = selectedExerciseWitSets.sets as MutableList
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