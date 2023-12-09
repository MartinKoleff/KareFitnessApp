package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.MuscleGroupUI
import com.koleff.kare_android.data.model.response.GetDashboardResponse
import com.koleff.kare_android.data.model.wrapper.GetDashboardWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class DashboardMockupDataSource : DashboardDataSource {
    override suspend fun getDashboard(): Flow<ResultWrapper<GetDashboardWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(5000)
            val dashboardList = generateDashboardList()

            val mockupResult = GetDashboardWrapper(
                GetDashboardResponse(
                    dashboardList
                )
            )

            emit(ResultWrapper.Success(mockupResult))
        }

    private fun generateDashboardList(): List<MuscleGroupUI> = listOf(
        MuscleGroupUI("Chest", "", R.drawable.ic_chest),
        MuscleGroupUI("Back", "", R.drawable.ic_back),
        MuscleGroupUI("Triceps", "", R.drawable.ic_triceps),
        MuscleGroupUI("Biceps", "", R.drawable.ic_biceps),
        MuscleGroupUI("Shoulders", "", R.drawable.ic_shoulder),
        MuscleGroupUI("Legs", "", R.drawable.ic_legs),
//            MuscleGroup("Cardio", "", R.drawable.ic_faceid),
    )
}
