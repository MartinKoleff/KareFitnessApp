package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.DashboardState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.DashboardRepository
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.MuscleGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository,
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(navigationController = navigationController), MainScreenNavigation {

    private var _state: MutableStateFlow<DashboardState> =
        MutableStateFlow(DashboardState(muscleGroupList = preferences.loadDashboardMuscleGroupList()))
    val state: StateFlow<DashboardState>
        get() = _state

    init {
        getDashboard()
    }

    private fun getDashboard() {
        viewModelScope.launch(dispatcher) {
            dashboardRepository.getDashboard().collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        _state.value = DashboardState(
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    }

                    is ResultWrapper.Loading -> { //Don't delete cache...
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultWrapper.Success -> {
                        Log.d("DashboardViewModel", "Flow received.")

                        _state.value = DashboardState(
                            isSuccessful = true,
                            muscleGroupList = apiResult.data.muscleGroupList
                        ).also {
                            preferences.saveDashboardMuscleGroupList(muscleGroupList = it.muscleGroupList)
                        }
                    }
                }
            }
        }
    }


    //Navigation
    fun navigateToMuscleGroupDetails(muscleGroupId: Int) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.MuscleGroupExercisesList(
                    muscleGroupId
                )
            )
        )
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = DashboardState(muscleGroupList = preferences.loadDashboardMuscleGroupList())
        }
    }

    override fun onNavigateToDashboard() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }

    override fun onNavigateToWorkouts() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }

    override fun onNavigateToSettings() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }

    override fun onNavigateBack()  {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }
}
