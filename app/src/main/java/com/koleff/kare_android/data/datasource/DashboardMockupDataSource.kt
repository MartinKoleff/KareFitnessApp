package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.R
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.GetDashboardResponse
import com.koleff.kare_android.domain.wrapper.DashboardWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DashboardMockupDataSource : DashboardDataSource {
    override suspend fun getDashboard(): Flow<ResultWrapper<DashboardWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)
            val dashboardList = generateDashboardList()

            val mockupResult = DashboardWrapper(
                GetDashboardResponse(
                    dashboardList
                )
            )

            emit(ResultWrapper.Success(mockupResult))
        }

    private fun generateDashboardList(): List<MuscleGroup> = MuscleGroup.getSupportedMuscleGroups()

}
