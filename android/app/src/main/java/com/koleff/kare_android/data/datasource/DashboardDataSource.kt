package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.data.model.wrapper.GetDashboardWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface DashboardDataSource {
    suspend fun getDashboard(): Flow<ResultWrapper<GetDashboardWrapper>>
}