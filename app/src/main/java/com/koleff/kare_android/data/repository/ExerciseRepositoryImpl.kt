package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
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

    override suspend fun getExercise(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.getExercise(exerciseId, workoutId)
    }

    override suspend fun getCatalogExercise(exerciseId: Int): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.getCatalogExercise(exerciseId)
    }

    override suspend fun getCatalogExercises(muscleGroupId: Int): Flow<ResultWrapper<ExerciseListWrapper>> {
        return exerciseDataSource.getCatalogExercises(muscleGroupId)
    }

    override suspend fun getExerciseDetails(
        exerciseId: Int,
        workoutId: Int
    ): Flow<ResultWrapper<ExerciseDetailsWrapper>> {
        return exerciseDataSource.getExerciseDetails(exerciseId, workoutId)
    }

    override suspend fun deleteExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        setId: UUID,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.deleteExerciseSet(exerciseId, workoutId, setId, currentSets)
    }

    override suspend fun addNewExerciseSet(
        exerciseId: Int,
        workoutId: Int,
        currentSets: List<ExerciseSetDto>
    ): Flow<ResultWrapper<ExerciseWrapper>> {
        return exerciseDataSource.addNewExerciseSet(exerciseId, workoutId, currentSets)
    }
}