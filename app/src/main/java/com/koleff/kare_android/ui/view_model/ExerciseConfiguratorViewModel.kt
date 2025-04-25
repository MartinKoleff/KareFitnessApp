package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.ExerciseState
import com.koleff.kare_android.ui.state.WorkoutDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseConfiguratorViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val workoutUseCases: WorkoutUseCases,
    private val navigationController: NavigationController,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController), MainScreenNavigation {

    private val exerciseId: Int = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1
    private val initialMuscleGroupId: Int =
        savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull() ?: -1
    val initialMuscleGroup = MuscleGroup.fromId(initialMuscleGroupId)

    private var _exerciseState: MutableStateFlow<ExerciseState> = MutableStateFlow(ExerciseState())
    val exerciseState: StateFlow<ExerciseState>
        get() = _exerciseState

    private var _workoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val workoutState: StateFlow<WorkoutDetailsState>
        get() = _workoutState

    private var _updateWorkoutState: MutableStateFlow<WorkoutDetailsState> =
        MutableStateFlow(WorkoutDetailsState())
    val updateWorkoutState: StateFlow<WorkoutDetailsState>
        get() = _updateWorkoutState

    private var _currentEditedSetIndex = MutableStateFlow(1)
    val currentEditedSetIndex: StateFlow<Int>
        get() = _currentEditedSetIndex

    val currentEditedSet: ExerciseSetDto
        get() {
            return try {
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1]
            } catch (e: IndexOutOfBoundsException) {
                ExerciseSetDto(null, 0, 0, 1, 12, 50f)
            }
        }

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
                _workoutState.value = workoutDetailsState
            }
        }
    }

    fun onExerciseUpdateEvent(event: OnExerciseUpdateEvent) {
        val selectedWorkout: WorkoutDetailsDto

        when (event) {
            is OnExerciseUpdateEvent.OnExerciseDelete -> {
                selectedWorkout = workoutState.value.workoutDetails
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
                selectedWorkout = workoutState.value.workoutDetails
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
        if (workoutState.value.isError) {
            _workoutState.value = WorkoutDetailsState()
        }
    }

    fun deleteSet(selectedExerciseSet: ExerciseSetDto) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.deleteExerciseSetUseCase(
                exerciseId = selectedExerciseSet.exerciseId,
                workoutId = selectedExerciseSet.workoutId,
                setId = selectedExerciseSet.setId,
                currentSets = _exerciseState.value.exercise.sets
            ).collect { deleteSetState ->
                _exerciseState.value = deleteSetState
            }
        }
    }

    fun deleteLatestSet() {
        val latestExerciseSet = _exerciseState.value.exercise.sets.last()

        viewModelScope.launch(dispatcher) {
            exerciseUseCases.deleteExerciseSetUseCase(
                exerciseId = latestExerciseSet.exerciseId,
                workoutId = latestExerciseSet.workoutId,
                currentSets = _exerciseState.value.exercise.sets
            ).collect { deleteSetState ->
                _exerciseState.value = deleteSetState

                if (deleteSetState.isSuccessful) {
                    if (_exerciseState.value.exercise.sets.size < _currentEditedSetIndex.value) {
                        _currentEditedSetIndex.value -= 1
                    }
                }
            }
        }
    }

    fun addNewSet() {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.addNewExerciseSetUseCase(
                exerciseId = exerciseId,
                workoutId = workoutId,
                currentSets = _exerciseState.value.exercise.sets
            ).collect { addNewSetState ->
                _exerciseState.value = addNewSetState
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

    fun onDecreaseReps() {
        try {
            val updatedReps =
                if (exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].reps - 1 < 0)
                    0
                else
                    exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].reps - 1

            val updatedSet =
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].copy(
                    reps = updatedReps
                )

            updateSet(updatedSet)
        } catch (e: IndexOutOfBoundsException) {
            Log.d("ExerciseConfiguratorViewModel", "Decrease reps error.")
        }
    }

    fun onIncreaseReps() {
        try {
            val updatedReps =
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].reps + 1

            val updatedSet =
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].copy(
                    reps = updatedReps
                )

            updateSet(updatedSet)
        } catch (e: IndexOutOfBoundsException) {
            Log.d("ExerciseConfiguratorViewModel", "Increase reps error.")
        }
    }

    fun onDecreaseWeight(newWeight: Int) {
        try {
            val updatedWeight =
                if (exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].weight - newWeight < 0)
                    0f
                else
                    exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].weight - newWeight

            val updatedSet =
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].copy(
                    weight = updatedWeight
                )

            updateSet(updatedSet)
        } catch (e: IndexOutOfBoundsException) {
            Log.d("ExerciseConfiguratorViewModel", "Decrease weight error.")
        }
    }

    fun onIncreaseWeight(newWeight: Int) {
        try {
            val updatedWeight =
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].weight + newWeight

            val updatedSet =
                exerciseState.value.exercise.sets[currentEditedSetIndex.value - 1].copy(
                    weight = updatedWeight
                )

            updateSet(updatedSet)
        } catch (e: IndexOutOfBoundsException) {
            Log.d("ExerciseConfiguratorViewModel", "Increase weight error.")
        }
    }


    private fun updateSet(updatedSet: ExerciseSetDto) {
        val updatedSets = exerciseState.value.exercise.sets.toMutableList()
        updatedSets[currentEditedSetIndex.value - 1] = updatedSet

        val updatedExercise = _exerciseState.value.exercise.copy(
            sets = updatedSets
        )
        _exerciseState.value = _exerciseState.value.copy(
            exercise = updatedExercise
        )
    }

    fun onSetChange(isIncrease: Boolean) {
        if (isIncrease && exerciseState.value.exercise.sets.size > _currentEditedSetIndex.value) {
            _currentEditedSetIndex.value += 1
        } else if (currentEditedSetIndex.value - 1 <= 0) {
            _currentEditedSetIndex.value = 1
        } else if (!isIncrease) {
            _currentEditedSetIndex.value -= 1
        }
    }
}
