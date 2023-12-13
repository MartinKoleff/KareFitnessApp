package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetExerciseWrapper
import com.koleff.kare_android.data.model.wrapper.GetExercisesWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>>
    suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<GetExerciseWrapper>>
    suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>>
}