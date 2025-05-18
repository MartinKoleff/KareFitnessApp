package com.koleff.kare_android.data.datasource.exercise

import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface ExerciseDataSourceLocalExt {
    suspend fun addNewExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>>

    suspend fun deleteExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>>

    suspend fun deleteLatestExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>>
}