package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.HasUpdated
import com.koleff.kare_android.ui.state.WorkoutConfigurationState
import com.koleff.kare_android.ui.state.WorkoutState
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
    private val hasUpdated: HasUpdated,
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


    private var _startWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val startWorkoutState: StateFlow<BaseState>
        get() = _startWorkoutState

    private var _createWorkoutState: MutableStateFlow<WorkoutState> =
        MutableStateFlow(WorkoutState())
    val createWorkoutState: StateFlow<WorkoutState>
        get() = _createWorkoutState

    private var _updateWorkoutConfigurationState: MutableStateFlow<WorkoutConfigurationState> =
        MutableStateFlow(WorkoutConfigurationState())
    val updateWorkoutConfigurationState: StateFlow<BaseState>
        get() = _updateWorkoutConfigurationState

    val isRefreshing by mutableStateOf(getWorkoutDetailsState.value.isLoading)

    private val isNewWorkout = savedStateHandle.get<String>("is_new_workout").toBoolean()

    init {
        Log.d("WorkoutDetailsViewModel", "isNewWorkout: $isNewWorkout")
        Log.d("WorkoutDetailsViewModel", "WorkoutId: $workoutId")

        //Invalid id handling
        if (workoutId != -1) {
            getWorkoutDetails(workoutId)
        } else if (isNewWorkout) {
            createNewWorkout()
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

                    //Update main state
                    _getWorkoutDetailsState.value = _getWorkoutDetailsState.value.copy(
                        workoutDetails = deleteExerciseState.workoutDetails
                    )
                }

            Log.d("WorkoutDetailsViewModel", "hasUpdated set to true.")
            hasUpdated.notifyUpdate(true)
        }
    }

    //Navigation
    private fun navigateToSearchExercises(workoutId: Int) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.SearchExercisesScreen(workoutId)
            )
        )
    }

    fun navigateToExerciseDetailsConfigurator(exercise: ExerciseDto) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.ExerciseDetailsConfigurator(
                    exerciseId = exercise.exerciseId,
                    workoutId = workoutId,
                    muscleGroupId = exercise.muscleGroup.muscleGroupId
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

        if (deleteWorkoutState.value.isError) {
            _deleteWorkoutState.value = BaseState()
        }

        if (updateWorkoutDetailsState.value.isError) {
            _updateWorkoutDetailsState.value = WorkoutDetailsState()
        }

        if (startWorkoutState.value.isError) {
            _startWorkoutState.value = BaseState()
        }

        if (createWorkoutState.value.isError) {
            _createWorkoutState.value = WorkoutState()
        }

        if (updateWorkoutConfigurationState.value.isError) {
            _updateWorkoutConfigurationState.value = WorkoutConfigurationState()
        }
    }

    fun startWorkout() {

        //Empty exercise list
        if (getWorkoutDetailsState.value.workoutDetails.exercises.isEmpty()) {
            _startWorkoutState.value = BaseState(
                isError = true,
                error = KareError.WORKOUT_HAS_NO_EXERCISES
            )
        } else {
            _startWorkoutState.value = BaseState(isSuccessful = true)

            super.onNavigationEvent(
                NavigationEvent.NavigateTo(
                    Destination.DoWorkoutScreen(workoutId = workoutId)
                )
            )
        }
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

    fun createNewWorkout() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.createNewWorkoutUseCase().collect { createWorkoutState ->
                _createWorkoutState.value = createWorkoutState

                if (createWorkoutState.isSuccessful) {
                    val createdWorkout = createWorkoutState.workout

                    Log.d(
                        "WorkoutDetailsViewModel",
                        "Create workout with id ${createdWorkout.workoutId}"
                    )

                    getWorkoutDetails(createdWorkout.workoutId) //TODO: convert workout to workout details to skip fetch?
                    Log.d(
                        "WorkoutDetailsViewModel",
                        "Fetching workout details for the newly created workout."
                    )
                }
            }
        }

        //Reset state
        savedStateHandle["isNewWorkout"] = false
    }


    fun updateWorkoutConfiguration(configuration: WorkoutConfigurationDto) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.updateWorkoutConfigurationUseCase(configuration).collect { result ->
                _updateWorkoutConfigurationState.value = result

                //Update workout configuration in workout details
                if (result.isSuccessful) {
                    val updatedWorkoutDetails = _getWorkoutDetailsState.value.workoutDetails.copy(
                        configuration = result.workoutConfiguration
                    )

                    _getWorkoutDetailsState.value =
                        _getWorkoutDetailsState.value.copy(workoutDetails = updatedWorkoutDetails)
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

    override fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }
}
