package com.koleff.kare_android.ui.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnWorkoutScreenSwitchEvent
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutUseCases: WorkoutUseCases,
    private val preferences: Preferences,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutState> = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState>
        get() = _state

    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()

    private val hasLoadedFromCache = mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.Main) {
            preferences.loadSelectedWorkout()?.let { selectedWorkout ->
                _state.value = WorkoutState(
                    isSuccessful = true,
                    workoutList = listOf(selectedWorkout)
                )
                hasLoadedFromCache.value = true
            } ?: run {
                hasLoadedFromCache.value = false
            }

            getWorkouts()
        }
    }

    fun onEvent(event: OnWorkoutScreenSwitchEvent) {
        viewModelScope.launch(dispatcher) {
            _state.value = WorkoutState(
                isLoading = true
            )
            delay(Constants.fakeSmallDelay)

            when (event) {
                OnWorkoutScreenSwitchEvent.AllWorkouts -> {
                    _state.value = state.value.copy(
                        workoutList = originalWorkoutList,
                        isMyWorkoutScreen = false,
                        isLoading = false
                    )
                }

                OnWorkoutScreenSwitchEvent.SelectedWorkout -> {
                    _state.value = state.value.copy(
                        workoutList = listOfNotNull(
                            originalWorkoutList.firstOrNull {
                                it.isSelected
                            }
                        ),
                        isMyWorkoutScreen = true,
                        isLoading = false
                    ).also {
                        if (it.workoutList.isNotEmpty()) {
                            preferences.saveSelectedWorkout(it.workoutList.first())
                        }
                    }
                }
            }
        }
    }

    fun getWorkout(workoutId: Int): WorkoutDto {
        return state.value.workoutList.single { workout ->
            workout.workoutId == workoutId
        }
    }

//    fun getWorkout(workoutId: Int): WorkoutDto {
//        viewModelScope.launch(dispatcher) {
//            workoutUseCases.getWorkoutUseCase(workoutId).collect { workoutState ->
//                _state.value = workoutState
//            }
//        }
//    }

    private fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutsUseCase(hasLoadedFromCache.value).collect { workoutState ->
                _state.value = workoutState

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList ?: emptyList()
                }
            }
        }
    }
}
