package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.GetExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetExercisesWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ExerciseMockupDataSource() : ExerciseDataSource {

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercisesList = generateExerciseList()

            val mockupResult = GetExercisesWrapper(
                GetExercisesResponse(
                    mockupExercisesList
                )
            )

            emit(ResultWrapper.Success(mockupResult))
        }

    private fun generateExerciseList(): List<ExerciseDto> {
        val n = 10
        val exercisesList: MutableList<ExerciseDto> = mutableListOf()

        repeat(n) { index ->
            val currentExercise =
                ExerciseDto(
                    index,
                    "BARBELL BENCH PRESS $index",
                    MuscleGroup.CHEST,
                    MachineType.BARBELL,
                    ""
                )
            exercisesList.add(currentExercise)
        }

        return exercisesList
    }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val mockupExercise = generateExercise()

            val mockupResult = GetExerciseDetailsWrapper(
                GetExerciseDetailsResponse(
                    mockupExercise
                )
            )

            emit(ResultWrapper.Success(mockupResult))
        }

    private fun generateExercise(): ExerciseDetailsDto {
        return ExerciseDetailsDto(
            name = "BARBELL BENCH PRESS",
            description = "",
            muscleGroup = MuscleGroup.CHEST,
            machineType = MachineType.BARBELL,
            videoUrl = ""
        )
    }
}