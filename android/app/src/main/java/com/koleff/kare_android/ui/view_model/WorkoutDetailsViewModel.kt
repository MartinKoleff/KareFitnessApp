package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.event.OnWorkoutDetailsEvent
import com.koleff.kare_android.data.model.state.WorkoutDetailsState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkoutDetailsViewModel @AssistedInject constructor(
    private val workoutUseCases: WorkoutUseCases,
    @Assisted private val workoutId: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val state: StateFlow<WorkoutDetailsState>
        get() = _state

    init {

        //Invalid id handling
        if (workoutId != -1) {
            getWorkoutDetails(workoutId)
        }
    }

    fun onEvent(onWorkoutDetailsEvent: OnWorkoutDetailsEvent) {
        when (onWorkoutDetailsEvent) {
            is OnWorkoutDetailsEvent.OnExerciseDelete -> {
                val workout = state.value.workout
                workout.exercises.remove(
                    onWorkoutDetailsEvent.exercise
                )

                //TODO: call repo...
            }
            is OnWorkoutDetailsEvent.OnExerciseSubmit -> {
                val workout = state.value.workout
                workout.exercises.add(
                    onWorkoutDetailsEvent.exercise
                )

                //TODO: call repo...
            }
        }
    }

    private fun getWorkoutDetails(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutDetailsUseCase(workoutId).collect { workoutDetailsState ->
                _state.value = workoutDetailsState
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
