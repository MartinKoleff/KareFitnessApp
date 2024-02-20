package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.model.response.ExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@Deprecated("Unused.")
class ExerciseMockupDataSource(private val isError: Boolean = false) : ExerciseDataSource { //TODO: delete...

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercisesList = MockupDataGenerator.generateExerciseList(muscleGroupId)

            val mockupResult = ExerciseListWrapper(
                GetExercisesResponse(
                    mockupExercisesList
                )
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercise = MockupDataGenerator.generateExercise().copy(
                exerciseId = exerciseId
            )

            val mockupResult = ExerciseWrapper(
                ExerciseResponse(
                    mockupExercise
                )
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<ExerciseDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercise = MockupDataGenerator.generateExerciseDetails().copy(
                id = exerciseId
            )

            val mockupResult = ExerciseDetailsWrapper(
                ExerciseDetailsResponse(
                    mockupExercise
                )
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }
}