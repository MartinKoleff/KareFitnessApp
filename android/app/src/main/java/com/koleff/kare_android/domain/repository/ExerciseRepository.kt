package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.GetExercisesWrapper
import com.koleff.kare_android.data.model.response.base_response.ServerResponseData
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {

    suspend fun getExercises(): Flow<ResultWrapper<GetExercisesWrapper>>
}