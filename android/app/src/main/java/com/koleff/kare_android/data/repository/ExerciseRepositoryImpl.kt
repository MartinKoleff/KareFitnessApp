package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.model.dto.GetExercisesWrapper
import com.koleff.kare_android.data.model.response.base_response.ServerResponseData
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@AndroidEntryPoint
class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseDataSource: ExerciseDataSource
) : ExerciseRepository {

    override suspend fun getExercises(): Flow<ResultWrapper<GetExercisesWrapper>> {
        return exerciseDataSource.getExercises()
    }
}