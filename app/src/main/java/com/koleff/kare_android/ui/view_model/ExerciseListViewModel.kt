package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.event.OnFilterExercisesEvent
import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.ui.state.SearchState
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
class ExerciseListViewModel @Inject constructor(
    private val exerciseUseCases: ExerciseUseCases,
    private val savedStateHandle: SavedStateHandle,
    private val navigationController: NavigationController,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController), MainScreenNavigation {

    private val muscleGroupId = savedStateHandle.get<String>("muscle_group_id")?.toIntOrNull()
        ?.plus(1)
        ?: -1
    val muscleGroup = MuscleGroup.fromId(muscleGroupId)

    private var _state: MutableStateFlow<ExerciseListState> = MutableStateFlow(ExerciseListState())
    val state: StateFlow<ExerciseListState>
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
            exerciseUseCases.onFilterExercisesUseCase(event).collect { exerciseState ->
                _state.value = exerciseState
            }
        }
    }

    private fun getExercises(muscleGroupId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.getExercisesUseCase(muscleGroupId).collect { exerciseState ->
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
