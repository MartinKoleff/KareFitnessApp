package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.wrapper.GetDashboardWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    suspend fun getDashboard(): Flow<ResultWrapper<GetDashboardWrapper>>
}