package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnWorkoutScreenSwitchEvent
import com.koleff.kare_android.ui.state.WorkoutListState
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.HasUpdated
import com.koleff.kare_android.ui.state.SelectedWorkoutState
import com.koleff.kare_android.ui.state.WorkoutState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutUseCases: WorkoutUseCases,
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    private val savedStateHandle: SavedStateHandle,
    val hasUpdated: HasUpdated,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController), MainScreenNavigation {

    private var _state: MutableStateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())
    val state: StateFlow<WorkoutListState>
        get() = _state

    private var _deleteWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val deleteWorkoutState: StateFlow<BaseState>
        get() = _deleteWorkoutState

    private var _selectWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val selectWorkoutState: StateFlow<BaseState>
        get() = _selectWorkoutState

    private var _getSelectedWorkoutState: MutableStateFlow<SelectedWorkoutState> =
        MutableStateFlow(SelectedWorkoutState())
    val getSelectedWorkoutState: StateFlow<SelectedWorkoutState>
        get() = _getSelectedWorkoutState

    private var _updateWorkoutState: MutableStateFlow<WorkoutState> =
        MutableStateFlow(WorkoutState())
    val updateWorkoutState: StateFlow<WorkoutState>
        get() = _updateWorkoutState

//    private var _createWorkoutState: MutableStateFlow<WorkoutState> =
//        MutableStateFlow(WorkoutState())
//    val createWorkoutState: StateFlow<WorkoutState>
//        get() = _createWorkoutState


    val isRefreshing by mutableStateOf(state.value.isLoading)

    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()

    private val hasLoadedFromCache = mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.Main) {
            preferences.loadSelectedWorkout()?.let { selectedWorkout ->
                _state.value = WorkoutListState(
                    isSuccessful = true,
                    workoutList = listOf(selectedWorkout)
                )
                hasLoadedFromCache.value = true
            } ?: run {
                hasLoadedFromCache.value = false
            }

            getWorkouts()
        }
    }

    fun onWorkoutFilterEvent(event: OnWorkoutScreenSwitchEvent) {
        viewModelScope.launch(dispatcher) {
            _state.value = WorkoutListState(
                isLoading = true
            )
            delay(Constants.fakeDelay)

            when (event) {
                OnWorkoutScreenSwitchEvent.AllWorkouts -> {
                    _state.value = state.value.copy(
                        workoutList = originalWorkoutList,
                        isMyWorkoutScreen = false,
                        isLoading = false
                    )
                }

                OnWorkoutScreenSwitchEvent.SelectedWorkout -> {
                    _state.value = state.value.copy(
                        workoutList = listOfNotNull(
                            originalWorkoutList.firstOrNull {
                                it.isSelected
                            }
                        ),
                        isMyWorkoutScreen = true,
                        isLoading = false
                    ).also {
                        if (it.workoutList.isNotEmpty()) {
                            preferences.saveSelectedWorkout(it.workoutList.first())
                        }
                    }
                }
            }
        }
    }

    fun getWorkout(workoutId: Int): WorkoutDto {
        return state.value.workoutList.single { workout ->
            workout.workoutId == workoutId
        }
    }

//    fun getWorkout(workoutId: Int): WorkoutDto {
//        viewModelScope.launch(dispatcher) {
//            workoutUseCases.getWorkoutUseCase(workoutId).collect { workoutState ->
//                _state.value = workoutState
//            }
//        }
//    }

    fun deleteWorkout(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.deleteWorkoutUseCase(workoutId).collect { deleteWorkoutState ->
                _deleteWorkoutState.value = deleteWorkoutState

                //Update workout list
                if (deleteWorkoutState.isSuccessful) {
                    val updatedList =
                        state.value.workoutList.filterNot { it.workoutId == workoutId }
                    _state.value = _state.value.copy(workoutList = updatedList)

                    originalWorkoutList =
                        originalWorkoutList.filterNot { it.workoutId == workoutId }
                }
            }
        }
    }

    fun selectWorkout(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.selectWorkoutUseCase(workoutId).collect { selectWorkoutState ->
                _selectWorkoutState.value = selectWorkoutState

                //Update workout list
                if (selectWorkoutState.isSuccessful) {
                    getWorkouts()
                }
            }
        }
    }

    fun getSelectWorkout() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getSelectedWorkoutUseCase().collect { getSelectedWorkoutState ->
                _getSelectedWorkoutState.value = getSelectedWorkoutState

                //Update selected workout
                if (getSelectedWorkoutState.isSuccessful) {
                    val selectedWorkout = getSelectedWorkoutState.selectedWorkout ?: return@collect

                    val updatedList =
                        state.value.workoutList.filterNot { it.workoutId == selectedWorkout.workoutId } as MutableList
                    updatedList.add(selectedWorkout)
                    updatedList.sortBy { it.name }

                    _state.value = _state.value.copy(workoutList = updatedList)
                    originalWorkoutList = updatedList
                }
            }
        }
    }

    fun updateWorkout(workoutDto: WorkoutDto) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.updateWorkoutUseCase(workoutDto).collect { updateWorkoutState ->
                _updateWorkoutState.value = updateWorkoutState

                //Update workout list
                if (updateWorkoutState.isSuccessful) {
                    val selectedWorkout = updateWorkoutState.workout

                    val updatedList =
                        state.value.workoutList.filterNot { it.workoutId == selectedWorkout.workoutId } as MutableList
                    updatedList.add(selectedWorkout)
                    updatedList.sortBy { it.name }

                    _state.value = _state.value.copy(workoutList = updatedList)
                    originalWorkoutList = updatedList
                }
            }
        }
    }

//    fun createNewWorkout() {
//        viewModelScope.launch(dispatcher) {
//            workoutUseCases.createNewWorkoutUseCase().collect { createWorkoutState ->
//                _createWorkoutState.value = createWorkoutState
//
//                //Update workout list
//                if (createWorkoutState.isSuccessful) {
//                    val createdWorkout = createWorkoutState.workout
//
//                    val updatedList = state.value.workoutList as MutableList<WorkoutDto>
//                    updatedList.add(createdWorkout)
//                    updatedList.sortBy { it.name }
//
//                    _state.value = _state.value.copy(workoutList = updatedList)
//                    originalWorkoutList = updatedList
//
//                    //Await update workout
//                    Log.d(
//                        "WorkoutViewModel",
//                        "Create workout with id ${createdWorkout.workoutId}"
//                    )
//                    navigateToWorkoutDetails(createdWorkout.workoutId)
//
//                    //Reset create workout state...
//                    resetCreateWorkoutState()
//                    Log.d("WorkoutViewModel", "Resetting create workout state...")
//                }
//            }
//        }
//    }

//    private fun resetCreateWorkoutState() {
//        _createWorkoutState.value =
//            WorkoutState() //Fix infinite loop navigation bug in LaunchedEffect
//    }

    //Directly navigate and skip loading. Call create workout use case in workout details view model
//    fun createNewWorkout() {
//        viewModelScope.launch(dispatcher) {
//            savedStateHandle["isNewWorkout"] = true
//            Log.d("WorkoutViewModel", "isNewWorkout set to true.")
//
//            navigateToWorkoutDetails(-1)
//        }
//    }

    fun createNewWorkout() {
        navigateToWorkoutDetails(-1, isNewWorkout = true)

        Log.d("WorkoutViewModel", "hasUpdated set to true.")
        hasUpdated.notifyUpdate(true)
    }

    fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getAllWorkoutsUseCase().collect { workoutState ->
                _state.value = workoutState

//                isRefreshing = workoutState.isLoading

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList ?: emptyList()
                }
            }
        }

        Log.d("WorkoutViewModel", "hasUpdated set to false.")
        hasUpdated.notifyUpdate(false)
    }

    //Navigation
    fun navigateToSearchWorkout(exerciseId: Int, workoutId: Int) {   //TODO: TEST...
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.SearchWorkoutsScreen(
                    exerciseId = exerciseId,
                    workoutId = workoutId
                )
            )
        )
    }

    fun navigateToWorkoutDetails(workout: WorkoutDto) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.WorkoutDetails(workoutId = workout.workoutId,  isNewWorkout = false)
            )
        )
    }

    fun navigateToWorkoutDetails(workoutId: Int, isNewWorkout: Boolean = false) {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.WorkoutDetails(workoutId = workoutId, isNewWorkout = isNewWorkout)
            )
        )
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = WorkoutListState()
        }
        if (deleteWorkoutState.value.isError) {
            _deleteWorkoutState.value = BaseState()
        }
        if (updateWorkoutState.value.isError) {
            _updateWorkoutState.value = WorkoutState()
        }
        if (selectWorkoutState.value.isError) {
            _selectWorkoutState.value = BaseState()
        }
        if (getSelectedWorkoutState.value.isError) {
            _getSelectedWorkoutState.value = SelectedWorkoutState()
        }
//        if (createWorkoutState.value.isError) {
//            _createWorkoutState.value = WorkoutState()
//        }
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
