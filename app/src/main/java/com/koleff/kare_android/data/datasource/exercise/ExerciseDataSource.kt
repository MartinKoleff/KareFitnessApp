package com.koleff.kare_android.data.datasource.exercise

import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ExerciseDataSource {
    suspend fun getExercise(exerciseId: Int, workoutId: Int): Flow<ResultWrapper<ExerciseWrapper>>

    suspend fun getCatalogExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>>

    suspend fun getCatalogExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>>

    suspend fun getExerciseDetails(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseDetailsWrapper>>
}