package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.domain.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.GetExerciseWrapper
import com.koleff.kare_android.domain.wrapper.GetExercisesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) : ExerciseRepository {
    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>> {
       return exerciseDataSource.getExercises(muscleGroupId)
    }

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<GetExerciseWrapper>> {
        return exerciseDataSource.getExercise(exerciseId)
    }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>> {
        return exerciseDataSource.getExerciseDetails(exerciseId)
    }
}