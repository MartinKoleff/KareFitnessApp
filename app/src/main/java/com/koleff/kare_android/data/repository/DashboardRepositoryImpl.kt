package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.DashboardDataSource
import com.koleff.kare_android.domain.wrapper.DashboardWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.DashboardRepository
import kotlinx.coroutines.flow.Flow

class DashboardRepositoryImpl(private val dashboardDataSource: DashboardDataSource) : DashboardRepository {

    override suspend fun getDashboard(): Flow<ResultWrapper<DashboardWrapper>> {
        return dashboardDataSource.getDashboard()
    }
}