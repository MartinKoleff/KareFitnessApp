package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

typealias DeleteExerciseState = WorkoutDetailsState

@HiltViewModel
class WorkoutDetailsViewModel @Inject constructor(
    private val workoutUseCases: WorkoutUseCases,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

    private val _getWorkoutDetailsState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val getWorkoutDetailsState: StateFlow<WorkoutDetailsState>
        get() = _getWorkoutDetailsState

    private val _deleteExerciseState: MutableStateFlow<DeleteExerciseState> =
        MutableStateFlow(DeleteExerciseState())

    val deleteExerciseState: StateFlow<DeleteExerciseState>
        get() = _deleteExerciseState

    val isRefreshing by mutableStateOf(getWorkoutDetailsState.value.isLoading)

    init {
        Log.d("WorkoutDetailsViewModel", "WorkoutId: $workoutId")

        //Invalid id handling
        if (workoutId != -1) {
            getWorkoutDetails(workoutId)
        }
    }

    fun getWorkoutDetails(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutDetailsUseCase(workoutId).collect { workoutDetailsState ->
                _getWorkoutDetailsState.value = workoutDetailsState
            }
        }
    }

    fun deleteExercise(workoutId: Int, exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.deleteExerciseUseCase(workoutId, exerciseId).collect { deleteExerciseState ->
                _deleteExerciseState.value = deleteExerciseState
            }
        }
    }
}