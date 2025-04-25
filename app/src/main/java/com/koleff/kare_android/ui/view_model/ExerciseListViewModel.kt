package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.ui.event.OnFilterExerciseEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExerciseListViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private val muscleGroupId = savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull()
        ?.plus(1)
        ?: -1
    val muscleGroup = MuscleGroup.fromId(muscleGroupId)

    private var _state: MutableStateFlow<ExerciseListState> = MutableStateFlow(ExerciseListState())
    val state: StateFlow<ExerciseListState>
        get() = _state

    private var originalExerciseList: List<ExerciseDto> = mutableListOf()

    init {
        getCatalogExercises(muscleGroupId)
    }


    fun OnFilterExerciseEvent(machineType: MachineType) {
        when (machineType) {
            MachineType.DUMBBELL -> {
                filterExercises(OnFilterExerciseEvent.DumbbellFilter(exercises = originalExerciseList))
            }

            MachineType.BARBELL -> {
                filterExercises(OnFilterExerciseEvent.BarbellFilter(exercises = originalExerciseList))
            }

            MachineType.MACHINE -> {
                filterExercises(OnFilterExerciseEvent.MachineFilter(exercises = originalExerciseList))
            }

            MachineType.CALISTHENICS -> {
                filterExercises(OnFilterExerciseEvent.CalisthenicsFilter(exercises = originalExerciseList))
            }

            MachineType.NONE -> {
                filterExercises(OnFilterExerciseEvent.NoFilter(exercises = originalExerciseList))
            }
        }
    }

    private fun filterExercises(event: OnFilterExerciseEvent) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.onFilterExercisesUseCase(event).collect { exerciseState ->
                _state.value = exerciseState
            }
        }
    }

    private fun getCatalogExercises(muscleGroupId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getCatalogExercisesUseCase(muscleGroupId).collect { exerciseState ->
                _state.value = exerciseState

                if (_state.value.isSuccessful) {
                    originalExerciseList = _state.value.exerciseList
                }
            }
        }
    }

    //Navigation
    fun navigateToExerciseDetails(exerciseId: Int, muscleGroupId: Int) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.ExerciseDetails(
                    exerciseId = exerciseId,
                    muscleGroupId = muscleGroupId
                )
            )
        )
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = ExerciseListState()
        }
    }
}
