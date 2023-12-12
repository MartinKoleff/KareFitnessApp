package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.event.OnWorkoutDetailsEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.state.ExerciseDetailsState
import com.koleff.kare_android.data.model.state.WorkoutDetailsState
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel @AssistedInject constructor(
    private val workoutRepository: WorkoutRepository,
    @Assisted private val workoutId: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val state: StateFlow<WorkoutDetailsState>
        get() = _state

    init {
        getWorkoutDetails(workoutId)
    }

    fun onEvent(onWorkoutDetailsEvent: OnWorkoutDetailsEvent) {
        when (onWorkoutDetailsEvent) {
            is OnWorkoutDetailsEvent.OnExerciseDelete -> {
                val workout = state.value.workout
                workout.exercises.remove(
                    onWorkoutDetailsEvent.exercise
                )
            }
            is OnWorkoutDetailsEvent.OnExerciseSubmit -> {
                val workout = state.value.workout
                workout.exercises.add(
                    onWorkoutDetailsEvent.exercise
                )
            }
        }
    }

    private fun getWorkoutDetails(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutRepository.getWorkoutDetails(workoutId).collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        _state.value = WorkoutDetailsState(
                            isError = true,
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    }

                    is ResultWrapper.Loading -> {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultWrapper.Success -> {
                        Log.d("WorkoutDetailsViewModel", "Flow received.")

                        _state.value = WorkoutDetailsState(
                            isSuccessful = true,
                            workout = apiResult.data.workoutDetails
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(workoutId: Int): WorkoutDetailsViewModel
    }

    companion object {
        fun provideWorkoutDetailsViewModelFactory(
            factory: Factory,
            workoutId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(workoutId) as T
            }
        }
    }
}
