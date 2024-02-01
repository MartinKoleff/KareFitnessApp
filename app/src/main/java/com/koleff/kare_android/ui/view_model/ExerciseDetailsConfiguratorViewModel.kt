package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.UpdateWorkoutState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Qualifier

@HiltViewModel
class ExerciseDetailsConfiguratorViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val workoutUseCases: WorkoutUseCases,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val exerciseId: Int = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

//    private val muscleGroupId = savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull()
//        ?.plus(1)
//        ?: -1
//    val muscleGroup = MuscleGroup.fromId(muscleGroupId)

    private val _exerciseState: MutableStateFlow<ExerciseState> = MutableStateFlow(ExerciseState())
    val exerciseState: StateFlow<ExerciseState>
        get() = _exerciseState

    private val _selectedWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val selectedWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _selectedWorkoutState

    private val _updateWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val updateWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _updateWorkoutState

    init {
        getExercise(exerciseId)
        getWorkoutDetails(workoutId)
    }

    private fun getExercise(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExerciseUseCase(exerciseId).collect { exerciseState ->
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
                selectedWorkout = selectedWorkoutState.value.workout
                selectedWorkout.exercises.remove(event.exercise)
            }

            is OnExerciseUpdateEvent.OnExerciseSubmit -> {
                val newExercises: MutableList<ExerciseDto> =
                    selectedWorkoutState.value.workout.exercises.filterNot { it.exerciseId == exerciseId } as MutableList<ExerciseDto>
                newExercises.add(event.exercise)
                newExercises.sortBy { it.exerciseId }

                selectedWorkout = selectedWorkoutState.value.workout.copy(exercises = newExercises)
            }
        }

        viewModelScope.launch(dispatcher) {
            workoutUseCases.updateWorkoutUseCase.invoke(selectedWorkout)
                .collect { updateWorkoutState ->
                    _updateWorkoutState.value = updateWorkoutState
                }
        }
    }

    fun resetUpdateWorkoutState() {
        _updateWorkoutState.value = WorkoutDetailsState()
    }
}
