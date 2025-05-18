package com.koleff.kare_android.data.datasource.workout

import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.domain.wrapper.DuplicateExercisesWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface WorkoutDataSourceLocalExt {

    suspend fun findDuplicateExercises(workoutId: Int, exerciseList: List<ExerciseDto>): Flow<ResultWrapper<DuplicateExercisesWrapper>>
}