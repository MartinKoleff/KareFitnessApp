package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseDetailsViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val savedStateHandle: SavedStateHandle,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val exerciseId: Int = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1
    private val initialMuscleGroupId: Int = savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull() ?: -1
    private val initialMuscleGroup = MuscleGroup.fromId(initialMuscleGroupId)

    private val _state: MutableStateFlow<ExerciseDetailsState> =
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
            exerciseUseCases.getExerciseDetailsUseCase(exerciseId).collect { exerciseDetailsState ->

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
}
