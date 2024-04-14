package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.WorkoutState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectIndexed
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class ExerciseDetailsConfiguratorViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private val exerciseId: Int = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1
    private val initialMuscleGroupId =
        savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull() ?: -1
    val initialMuscleGroup = MuscleGroup.fromId(initialMuscleGroupId)

    private var _exerciseState: MutableStateFlow<ExerciseState> = MutableStateFlow(ExerciseState())
    val exerciseState: StateFlow<ExerciseState>
        get() = _exerciseState

    private var _selectedWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val selectedWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _selectedWorkoutState

    private var _updateWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val updateWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _updateWorkoutState

    init {
        getExercise(exerciseId, workoutId)
        getWorkoutDetails(workoutId)
    }

    private fun getExercise(exerciseId: Int, workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExerciseUseCase(exerciseId, workoutId).collect { exerciseState ->
                _exerciseState.value = exerciseState
            }
        }
    }

    private fun getWorkoutDetails(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getWorkoutDetailsUseCase(workoutId).collect { workoutDetailsState ->
                _selectedWorkoutState.value = workoutDetailsState
            }
        }
    }

    fun onExerciseUpdateEvent(event: OnExerciseUpdateEvent) {
        val selectedWorkout: WorkoutDetailsDto

        when (event) {
            is OnExerciseUpdateEvent.OnExerciseDelete -> {
                selectedWorkout = selectedWorkoutState.value.workoutDetails
                val exercise = event.exercise

                viewModelScope.launch(dispatcher) {
                    workoutUseCases.deleteExerciseUseCase(
                        workoutId = selectedWorkout.workoutId,
                        exerciseId = exercise.exerciseId
                    ).collect { updateWorkoutState ->
                        _updateWorkoutState.value = updateWorkoutState
                    }
                }
            }

            is OnExerciseUpdateEvent.OnExerciseSubmit -> {
                selectedWorkout = selectedWorkoutState.value.workoutDetails
                val exercise = event.exercise

                viewModelScope.launch(dispatcher) {
                    workoutUseCases.submitExerciseUseCase(
                        workoutId = selectedWorkout.workoutId,
                        exercise = exercise
                    ).collect { updateWorkoutState ->
                        _updateWorkoutState.value = updateWorkoutState
                    }
                }
            }
        }
    }

    private fun resetUpdateWorkoutState() {
        _updateWorkoutState.value = WorkoutDetailsState()
    }

    //Navigation
    fun navigateToWorkoutDetails(workoutId: Int) {
        super.onNavigationEvent(
            NavigationEvent.PopUpToAndNavigateTo(
                destinationRoute = Destination.WorkoutDetails(
                    workoutId
                ).route,
                popUpToRoute = Destination.Workouts.route,
                inclusive = false
            )
        )

        //Raise a flag to update Workouts screen...
        savedStateHandle["hasUpdated"] = true
        Log.d("ExerciseDetailsConfiguratorViewModel", "hasUpdated set to true.")

        //Reset state
        resetUpdateWorkoutState()
    }

    override fun clearError() {
        if (exerciseState.value.isError) {
            _exerciseState.value = ExerciseState()
        }
        if (updateWorkoutState.value.isError) {
            _updateWorkoutState.value = WorkoutDetailsState()
        }
        if (selectedWorkoutState.value.isError) {
            _selectedWorkoutState.value = WorkoutDetailsState()
        }
    }

    //TODO: wire with dialog...
    fun deleteSet(selectedExerciseSet: ExerciseSetDto) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.deleteExerciseSetUseCase(
                exerciseId = selectedExerciseSet.exerciseId,
                workoutId = selectedExerciseSet.workoutId,
                setId = selectedExerciseSet.setId
            ).collect { deleteSetState ->
                _exerciseState.value = deleteSetState
            }
        }
    }

    fun addNewSet() {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.addNewExerciseSetUseCase(
                exerciseId = exerciseId,
                workoutId = workoutId
            ).collect { addNewSetState ->
                _exerciseState.value = addNewSetState
            }
        }
    }
}
