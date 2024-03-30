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
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.SelectedWorkoutState
import com.koleff.kare_android.ui.state.WorkoutState
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
) : BaseViewModel(navigationController), MainScreenNavigation {
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

    private var _getWorkoutDetailsState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())

    val getWorkoutDetailsState: StateFlow<WorkoutDetailsState>
        get() = _getWorkoutDetailsState

    private var _deleteExerciseState: MutableStateFlow<DeleteExerciseState> =
        MutableStateFlow(DeleteExerciseState())

    val deleteExerciseState: StateFlow<DeleteExerciseState>
        get() = _deleteExerciseState

    private var _deleteWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val deleteWorkoutState: StateFlow<BaseState>
        get() = _deleteWorkoutState

    private var _updateWorkoutDetailsState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val updateWorkoutDetailsState: StateFlow<WorkoutDetailsState>
        get() = _updateWorkoutDetailsState

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
    private fun navigateToSearchExercises(workoutId: Int) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.SearchExercisesScreen(workoutId)
            )
        )
    }

    //Deprecated
    fun navigateToExerciseDetailsConfigurator(exerciseId: Int, workoutId: Int, muscleGroupId: Int) {
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
        if (getWorkoutDetailsState.value.isError) {
            _getWorkoutDetailsState.value = WorkoutDetailsState()
        }

        if (deleteExerciseState.value.isError) {
            _deleteExerciseState.value = DeleteExerciseState()
        }

        if(deleteWorkoutState.value.isError){
            _deleteWorkoutState.value = BaseState()
        }

        if(updateWorkoutDetailsState.value.isError){
            _updateWorkoutDetailsState.value = WorkoutDetailsState()
        }
    }

    fun startWorkout() {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.DoWorkoutScreen(workoutId = workoutId)
            )
        )
    }

    fun addExercise() {
        navigateToSearchExercises(
            workoutId = workoutId
        )
    }

    fun deleteWorkout() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.deleteWorkoutUseCase(workoutId).collect { deleteWorkoutState ->
                _deleteWorkoutState.value = deleteWorkoutState

                //Clear backstack and navigate to dashboard
                if (deleteWorkoutState.isSuccessful) {
                    super.onNavigationEvent(NavigationEvent.ClearBackstackAndNavigateTo(Destination.Dashboard))
                }
            }
        }
    }

    fun updateWorkout(workoutDetailsDto: WorkoutDetailsDto) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.updateWorkoutDetailsUseCase(workoutDetailsDto)
                .collect { updateWorkoutState ->
                    _updateWorkoutDetailsState.value = updateWorkoutState

                    //Update workout name
                    if (updateWorkoutState.isSuccessful) {
                        _getWorkoutDetailsState.value = _getWorkoutDetailsState.value.copy(
                            workoutDetails = updateWorkoutState.workoutDetails
                        )
                    }
                }
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
