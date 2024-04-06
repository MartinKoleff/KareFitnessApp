package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.domain.wrapper.ExerciseDetailsWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseWrapper
import com.koleff.kare_android.domain.wrapper.ExerciseListWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import kotlinx.coroutines.flow.Flow
import java.util.UUID
import javax.inject.Inject

class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) : ExerciseRepository {
    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> {
       return exerciseDataSource.getExercises(muscleGroupId)
    }

    override suspend fun getExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.getExercise(exerciseId)
    }

    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<ExerciseDetailsWrapper>> {
        return exerciseDataSource.getExerciseDetails(exerciseId)
    }

    override suspend fun deleteExerciseSet(exerciseId: Int, setId: UUID): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.deleteExerciseSet(exerciseId, setId)
    }

    override suspend fun addNewExerciseSet(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.addNewExerciseSet(exerciseId)
    }
}