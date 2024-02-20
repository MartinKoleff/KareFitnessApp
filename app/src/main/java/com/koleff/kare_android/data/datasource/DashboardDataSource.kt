package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.domain.wrapper.DashboardWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface DashboardDataSource {
    suspend fun getDashboard(): Flow<ResultWrapper<DashboardWrapper>>
}