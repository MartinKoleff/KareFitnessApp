package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetExercisesWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseDataSource {
    suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>>
    suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>>
}