package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.currentBackStackEntryAsState
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
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
    private val navigationController: NavigationController,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

    private var _getWorkoutDetailsState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val getWorkoutDetailsState: StateFlow<WorkoutDetailsState>
        get() = _getWorkoutDetailsState

    private var _deleteExerciseState: MutableStateFlow<DeleteExerciseState> =
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
            workoutUseCases.deleteExerciseUseCase(workoutId, exerciseId)
                .collect { deleteExerciseState ->
                    _deleteExerciseState.value = deleteExerciseState
                }
        }

        savedStateHandle["hasUpdated"] = true
        Log.d("ExerciseDetailsConfiguratorViewModel", "hasUpdated set to true.")
    }

    //Navigation
    fun openSearchExercisesScreen(workoutId: Int) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.SearchExercisesScreen(workoutId)
            )
        )
    }

    fun openExerciseDetailsConfiguratorScreen(exerciseId: Int, workoutId: Int, muscleGroupId: Int) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.ExerciseDetailsConfigurator(
                    exerciseId = exerciseId,
                    workoutId = workoutId,
                    muscleGroupId = muscleGroupId
                )
            )
        )
    }

    override fun clearError() {
       if(getWorkoutDetailsState.value.isError){
           _getWorkoutDetailsState.value = WorkoutDetailsState()
       }

        if(deleteExerciseState.value.isError){
            _deleteExerciseState.value = DeleteExerciseState()
        }
    }
}
