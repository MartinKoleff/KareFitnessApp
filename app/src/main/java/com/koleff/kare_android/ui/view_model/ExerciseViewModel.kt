package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.ui.state.ExerciseState
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
class ExerciseViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {
    private val exerciseId: Int = savedStateHandle.get<String>("exercise_id")?.toIntOrNull() ?: -1
    private val workoutId: Int = savedStateHandle.get<String>("workout_id")?.toIntOrNull() ?: -1

    private val _state: MutableStateFlow<ExerciseState> = MutableStateFlow(ExerciseState())
    val state: StateFlow<ExerciseState>
        get() = _state

    init {
        getExercise(exerciseId, workoutId)
    }

    private fun getExercise(exerciseId: Int, workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExerciseUseCase(exerciseId, workoutId).collect { exerciseState ->
              _state.value = exerciseState
            }
        }
    }

    override fun clearError() {
        if(state.value.isError){
            _state.value = ExerciseState()
        }
    }
}
