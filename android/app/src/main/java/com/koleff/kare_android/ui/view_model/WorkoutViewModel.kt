package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.event.OnWorkoutScreenSwitchEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.state.WorkoutState
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutState> = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState>
        get() = _state

    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()

//    init {
//        getWorkouts()
//    }

    fun onEvent(event: OnWorkoutScreenSwitchEvent) {
        when (event) {
            OnWorkoutScreenSwitchEvent.AllWorkouts -> {
                _state.value = state.value.copy(
                    workoutList = originalWorkoutList
                )
            }

            OnWorkoutScreenSwitchEvent.SelectedWorkout -> {
                _state.value = state.value.copy(
                    workoutList = originalWorkoutList.filter {
                        it.isSelected
                    }
                )
            }
        }
    }

     fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutRepository.getAllWorkouts().collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        _state.value = WorkoutState(
                            isError = true,
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    }

                    is ResultWrapper.Loading -> {
                        _state.value = WorkoutState(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        Log.d("WorkoutViewModel", "Flow received.")

                        _state.value = WorkoutState(
                            isSuccessful = true,
                            workoutList = apiResult.data.workouts
                        ).also {
                            originalWorkoutList = it.workoutList
                        }
                    }
                }
            }
        }
    }
}
