package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.DashboardDataSource
import com.koleff.kare_android.data.datasource.DashboardMockupDataSource
import com.koleff.kare_android.data.repository.DashboardRepositoryImpl
import com.koleff.kare_android.domain.repository.DashboardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DashboardModule {

    /**
     * Data sources
     */

    @Provides
    @Singleton
    fun provideDashboardDataSource(): DashboardDataSource {
        return DashboardMockupDataSource()
    }

    /**
     * Repositories
     */

    @Provides
    @Singleton
    fun provideDashboardRepository(dashboardDataSource: DashboardDataSource): DashboardRepository {
        return DashboardRepositoryImpl(dashboardDataSource)
    }
}