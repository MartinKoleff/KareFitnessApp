package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.dto.GetExercisesWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseDataSource {
    suspend fun getExercises(): Flow<ResultWrapper<GetExercisesWrapper>>
}