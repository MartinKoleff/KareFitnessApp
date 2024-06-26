package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailsViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val navigationController: NavigationController,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private val exerciseId: Int = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1
    private val initialMuscleGroupId: Int =
        savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull() ?: -1
    private val initialMuscleGroup = MuscleGroup.fromId(initialMuscleGroupId)

    private var _state: MutableStateFlow<ExerciseDetailsState> =
        MutableStateFlow(ExerciseDetailsState())
    val state: StateFlow<ExerciseDetailsState>
        get() = _state

    init {
        Log.d("ExerciseDetailsViewModel", initialMuscleGroup.toString())
        _state.value = state.value.copy(
            exercise = ExerciseDetailsDto(
                muscleGroup = initialMuscleGroup
            )
        )
        getExerciseDetails(exerciseId)
    }

    private fun getExerciseDetails(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExerciseDetailsUseCase(exerciseId, Constants.CATALOG_EXERCISE_ID)
                .collect { exerciseDetailsState ->

                    //Don't clear the exercise initial muscle group data...
                    if (exerciseDetailsState.isLoading) {
                        _state.value = state.value.copy(
                            isLoading = true
                        )
                    } else {
                        _state.value = exerciseDetailsState
                    }
                }
        }
    }

    //Navigation
    fun navigateToSearchWorkout() {
        if (state.value.exercise.id == -1) {
            _state.value = ExerciseDetailsState(
                isError = true,
                error = KareError.INVALID_EXERCISE
            )
        } else {
            super.onNavigationEvent(
                NavigationEvent.NavigateTo(
                    Destination.SearchWorkoutsScreen(
                        state.value.exercise.id,
                        Constants.CATALOG_EXERCISE_ID
                    )
                )
            )
        }
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = ExerciseDetailsState()
        }
    }
}
