package com.koleff.kare_android.exercise.data

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.model.response.GetExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.GetExerciseResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.domain.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetExerciseWrapper
import com.koleff.kare_android.domain.wrapper.GetExercisesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExerciseMockupDataSource(private val isError: Boolean = false) : ExerciseDataSource {

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercisesList = MockupDataGenerator.generateExerciseList(muscleGroupId)

            val mockupResult = GetExercisesWrapper(
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

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<GetExerciseWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercise = MockupDataGenerator.generateExercise().copy(
                exerciseId = exerciseId
            )

            val mockupResult = GetExerciseWrapper(
                GetExerciseResponse(
                    mockupExercise
                )
            )

            if (isError) {
                emit(ResultWrapper.ApiError())
            } else {
                emit(ResultWrapper.Success(mockupResult))
            }
        }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercise = MockupDataGenerator.generateExerciseDetails().copy(
                id = exerciseId
            )

            val mockupResult = GetExerciseDetailsWrapper(
                GetExerciseDetailsResponse(
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