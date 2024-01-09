package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.domain.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetExerciseWrapper
import com.koleff.kare_android.domain.wrapper.GetExercisesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseDataSource {
    suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>>
    suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<GetExerciseWrapper>>
    suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>>
}