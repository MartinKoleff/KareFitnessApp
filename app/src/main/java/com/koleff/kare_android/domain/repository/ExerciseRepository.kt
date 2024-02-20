package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>>
    suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>>
    suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<ExerciseDetailsWrapper>>
}