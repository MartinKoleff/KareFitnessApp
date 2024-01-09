package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.GetDashboardWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface DashboardRepository {
    suspend fun getDashboard(): Flow<ResultWrapper<GetDashboardWrapper>>
}