package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.ui.event.OnFilterExercisesEvent
import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.ui.state.ExercisesState
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseListViewModel @AssistedInject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    @Assisted private val muscleGroupId: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<ExercisesState> = MutableStateFlow(ExercisesState())
    val state: StateFlow<ExercisesState>
        get() = _state

    private var originalExerciseList: List<ExerciseDto> = mutableListOf()

    init {
        getExercises(muscleGroupId)
    }


    fun onFilterExercisesEvent(machineType: MachineType) {
        when (machineType) {
            MachineType.DUMBBELL -> {
                filterExercises(OnFilterExercisesEvent.DumbbellFilter(exercises = originalExerciseList))
            }
            MachineType.BARBELL -> {
                filterExercises(OnFilterExercisesEvent.BarbellFilter(exercises = originalExerciseList))
            }

            MachineType.MACHINE -> {
                filterExercises(OnFilterExercisesEvent.MachineFilter(exercises = originalExerciseList))
            }

            MachineType.CALISTHENICS -> {
                filterExercises(OnFilterExercisesEvent.CalisthenicsFilter(exercises = originalExerciseList))
            }

            MachineType.NONE -> {
                filterExercises(OnFilterExercisesEvent.NoFilter(exercises = originalExerciseList))
            }
        }
    }

    private fun filterExercises(event: OnFilterExercisesEvent) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.onFilterExercisesUseCase(event).collect{ exerciseState ->
                _state.value = exerciseState
            }
        }
    }

    private fun getExercises(muscleGroupId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExercisesUseCase(muscleGroupId).collect{exerciseState ->
                _state.value = exerciseState

                if(_state.value.isSuccessful){
                    originalExerciseList = _state.value.exerciseList
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(muscleGroupId: Int): ExerciseListViewModel
    }

    companion object {
        fun provideExerciseListViewModelFactory(
            factory: Factory,
            muscleGroupId: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(muscleGroupId) as T
            }
        }
    }
}
