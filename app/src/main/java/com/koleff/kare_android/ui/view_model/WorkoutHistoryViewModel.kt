package com.koleff.kare_android.ui.view_model

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.domain.usecases.DoWorkoutPerformanceMetricsUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.WorkoutPerformanceMetricsListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.koleff.kare_android.data.model.dto.WorkoutDto
import kotlinx.coroutines.launch
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import javax.inject.Inject
import java.time.LocalDate
import java.time.ZoneId

@HiltViewModel
class WorkoutHistoryViewModel @Inject constructor(
    private val doWorkoutPerformanceMetricsUseCases: DoWorkoutPerformanceMetricsUseCases,
//    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(navigationController = navigationController), MainScreenNavigation {

    private var _getPerformanceMetricListState: MutableStateFlow<WorkoutPerformanceMetricsListState> =
        MutableStateFlow(WorkoutPerformanceMetricsListState())
    val getPerformanceMetricListState: StateFlow<WorkoutPerformanceMetricsListState>
        get() = _getPerformanceMetricListState

    private var originalList: List<DoWorkoutPerformanceMetricsDto> = mutableListOf()

//    private var _getWorkoutState: MutableStateFlow<WorkoutState> =
//        MutableStateFlow(WorkoutState())
//    val getWorkoutState: StateFlow<WorkoutState>
//        get() = _getWorkoutState


    init {
        getWorkoutPerformanceMetrics()
    }

    //TODO: Fetch workout based on workoutId?
    private fun getWorkoutPerformanceMetrics() {
        viewModelScope.launch(dispatcher) {
            doWorkoutPerformanceMetricsUseCases.getAllDoWorkoutPerformanceMetricsUseCase()
                .collect { state ->
                    _getPerformanceMetricListState.value = state
                    originalList = state.doWorkoutPerformanceMetricsList

                    Log.d(
                        "WorkoutHistoryViewModel",
                        "Get all workout performance metrics state: $state"
                    )
                }
        }
    }

//    fun getWorkout(workoutId: Int) {
//        viewModelScope.launch(dispatcher) {
//            workoutUseCases.getWorkoutUseCase(workoutId)
//                .collect { state ->
//                    _getWorkoutState.value = state
//
//                    Log.d("WorkoutHistoryViewModel", "Workout with workoutId $workoutId fetched successfully!")
//                }
//        }
//    }

    override fun clearError() {
        if (_getPerformanceMetricListState.value.isError) {
            _getPerformanceMetricListState.value = WorkoutPerformanceMetricsListState()
        }

//        if(_getWorkoutState.value.isError){
//            _getWorkoutState.value = WorkoutState()
//        }
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

    override fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun filterByDate(fromDate: LocalDate?, toDate: LocalDate?) {
        val filteredList = originalList.filter {
            val localDate = it.date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

            localDate.isBefore(toDate) && localDate.isAfter(fromDate)
        }

        _getPerformanceMetricListState.value = getPerformanceMetricListState.value.copy(
            doWorkoutPerformanceMetricsList = filteredList
        )
    }
}
