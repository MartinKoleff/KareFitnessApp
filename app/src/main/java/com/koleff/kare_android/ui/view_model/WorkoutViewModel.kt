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
import com.koleff.kare_android.data.model.response.base_response.KareError
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
    private val hasUpdated: HasUpdated,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController), MainScreenNavigation {

    val hasUpdatedState
        get() = hasUpdated.getUpdateStatus()

    private var _state: MutableStateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())
    val state: StateFlow<WorkoutListState>
        get() = _state

    private var _deleteWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val deleteWorkoutState: StateFlow<BaseState>
        get() = _deleteWorkoutState

    private var _favoriteWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val favoriteWorkoutState: StateFlow<BaseState>
        get() = _favoriteWorkoutState

    private var _unfavoriteWorkoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val unfavoriteWorkoutState: StateFlow<BaseState>
        get() = _unfavoriteWorkoutState

    private var _getFavoriteWorkoutsState: MutableStateFlow<WorkoutListState> =
        MutableStateFlow(WorkoutListState())
    val getFavoriteWorkoutsState: StateFlow<WorkoutListState>
        get() = _getFavoriteWorkoutsState

    private var _updateWorkoutState: MutableStateFlow<WorkoutState> =
        MutableStateFlow(WorkoutState())
    val updateWorkoutState: StateFlow<WorkoutState>
        get() = _updateWorkoutState

//    private var _createWorkoutState: MutableStateFlow<WorkoutState> =
//        MutableStateFlow(WorkoutState())
//    val createWorkoutState: StateFlow<WorkoutState>
//        get() = _createWorkoutState

    var _shownWorkoutList = mutableListOf<WorkoutDto>()
    val shownWorkoutList: List<WorkoutDto>
        get() = _shownWorkoutList

    var _shownFavoriteWorkoutList = mutableListOf<WorkoutDto>()
    val shownFavoriteWorkoutList: List<WorkoutDto>
        get() = _shownFavoriteWorkoutList

    val selectedWorkoutList: List<WorkoutDto>
        get() = if (state.value.isFavoriteWorkoutsScreen) shownFavoriteWorkoutList else shownWorkoutList

    val isRefreshing by mutableStateOf(state.value.isLoading)

    private val hasLoadedFromCache = mutableStateOf(false)

    init {
        viewModelScope.launch(Dispatchers.Main) {
            val cacheFavoriteWorkouts = preferences.loadFavoriteWorkouts()

            _state.value = WorkoutListState(
                isSuccessful = true,
                workoutList = cacheFavoriteWorkouts
            )
            hasLoadedFromCache.value = cacheFavoriteWorkouts.isNotEmpty()
        }

        getWorkouts()
    }

    fun onWorkoutFilterEvent(event: OnWorkoutScreenSwitchEvent) {
        viewModelScope.launch(dispatcher) {
            _state.value = WorkoutListState(
                isLoading = true
            )
            delay(Constants.fakeDelay)

            when (event) {
                OnWorkoutScreenSwitchEvent.AllWorkouts -> {
                    _state.value = WorkoutListState(
                        workoutList = shownWorkoutList,
                        isSuccessful = true,
                        isFavoriteWorkoutsScreen = false,
                    )
                }

                OnWorkoutScreenSwitchEvent.FavoriteWorkouts -> {
                    _state.value = WorkoutListState(
                        workoutList = shownFavoriteWorkoutList,
                        isSuccessful = true,
                        isFavoriteWorkoutsScreen = true,
                    ).also {
                        if (it.workoutList.isNotEmpty()) {
                            preferences.saveFavoriteWorkouts(it.workoutList)
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
                    removeWorkoutFromShownWorkoutList(workoutId)
                }
            }
        }
    }


    //TODO: migrate to use case...
    private fun removeWorkoutFromShownWorkoutList(workoutId: Int) {
        _shownWorkoutList.removeAll {
            it.workoutId == workoutId
        }
        _shownFavoriteWorkoutList.removeAll {
            it.workoutId == workoutId
        }
        _state.value = _state.value.copy(workoutList = selectedWorkoutList)
    }

    fun favoriteWorkout(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.favoriteWorkoutUseCase(workoutId).collect { favoriteWorkoutState ->
                _favoriteWorkoutState.value = favoriteWorkoutState

                //Update workout list
                if (favoriteWorkoutState.isSuccessful) {
                    updateShownWorkoutList(workoutId, isFavorite = true)

//                    if (state.value.isFavoriteWorkoutsScreen) getFavoriteWorkouts() else getWorkouts()
                }
            }
        }
    }

    fun unfavoriteWorkout(workoutId: Int) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.unfavoriteWorkoutUseCase(workoutId).collect { unfavoriteWorkoutState ->
                _unfavoriteWorkoutState.value = unfavoriteWorkoutState

                //Update workout list
                if (unfavoriteWorkoutState.isSuccessful) {
                    updateShownWorkoutList(workoutId, isFavorite = false)

//                    if (state.value.isFavoriteWorkoutsScreen) getFavoriteWorkouts() else getWorkouts()
                }
            }
        }
    }

    //TODO: migrate to use case...
    private fun updateShownWorkoutList(workoutId: Int, isFavorite: Boolean) {

        /**
         * All workout list
         */

        val index = shownWorkoutList.indexOfFirst {
            it.workoutId == workoutId
        }

        //Invalid workout
        if (index == -1) {
            _state.value = WorkoutListState(
                isError = true,
                error = KareError.INVALID_WORKOUT
            )
            return
        }

        val updatedWorkout = shownWorkoutList[index].copy(isFavorite = isFavorite)

        val updatedWorkoutList = shownWorkoutList.toMutableList()
        updatedWorkoutList[index] = updatedWorkout

        _shownWorkoutList = updatedWorkoutList


        /**
         * Favorite workout list
         */
        val index2 = shownFavoriteWorkoutList.indexOfFirst {
            it.workoutId == workoutId
        }

        val updatedFavoriteWorkoutList = shownFavoriteWorkoutList.toMutableList()

        if (index2 == -1) {

            //New workout -> add
            if(isFavorite) {
                updatedFavoriteWorkoutList.add(updatedWorkout)
            }
        } else {

            //Existing workout -> update
            if(isFavorite){
                val updatedFavoriteWorkout =
                    shownFavoriteWorkoutList[index2].copy(isFavorite = true)
                updatedFavoriteWorkoutList[index2] = updatedFavoriteWorkout
            }else{
                updatedFavoriteWorkoutList.removeAt(index2)
            }
        }

        _shownFavoriteWorkoutList = updatedFavoriteWorkoutList

        _state.value = _state.value.copy(workoutList = selectedWorkoutList)
    }

    fun getFavoriteWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getFavoriteWorkoutsUseCase().collect { getFavoriteWorkoutsState ->
                _getFavoriteWorkoutsState.value = getFavoriteWorkoutsState

                //Update favorite workouts
                if (getFavoriteWorkoutsState.isSuccessful) {
                    val favoriteWorkouts = getFavoriteWorkoutsState.workoutList
                    _shownFavoriteWorkoutList = favoriteWorkouts.toMutableList()
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
                    updateShownWorkoutList(selectedWorkout)
                }
            }
        }
    }

    //TODO: migrate to use case...
    private fun updateShownWorkoutList(workout: WorkoutDto) {

        /**
         * All workout list
         */

        val index = shownWorkoutList.indexOfFirst {
            it.workoutId == workout.workoutId
        }

        //Invalid workout
        if (index == -1) {
            _state.value = WorkoutListState(
                isError = true,
                error = KareError.INVALID_WORKOUT
            )
            return
        }

        val updatedWorkoutList = shownWorkoutList.toMutableList()
        updatedWorkoutList[index] = workout

        _shownWorkoutList = updatedWorkoutList


        /**
         * Favorite workout list
         */
        val index2 = shownFavoriteWorkoutList.indexOfFirst {
            it.workoutId == workout.workoutId
        }

        val updatedFavoriteWorkoutList = shownFavoriteWorkoutList.toMutableList()
        if (index2 == -1) {

            //New workout -> add
            updatedFavoriteWorkoutList.add(workout)
        } else {

            //Existing workout -> update
            updatedFavoriteWorkoutList[index2] = workout
        }

        _shownFavoriteWorkoutList = updatedFavoriteWorkoutList

        _state.value = _state.value.copy(workoutList = selectedWorkoutList)
    }

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
                    _shownWorkoutList = workoutState.workoutList.toMutableList()
                    _shownFavoriteWorkoutList = _shownWorkoutList.filter {
                        it.isFavorite
                    }.toMutableList()
                }
            }
        }

        Log.d("WorkoutViewModel", "hasUpdated set to false.")
        hasUpdated.notifyUpdate(false)
    }

    fun onRefresh() {
        if (state.value.isFavoriteWorkoutsScreen) {
            getFavoriteWorkouts()
        } else {
            getWorkouts()
        }
    }

    //Navigation
    fun navigateToSearchWorkout(
        exerciseId: Int,
        workoutId: Int
    ) {   //TODO: test for No workout favorite banner...
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
                Destination.WorkoutDetails(workoutId = workout.workoutId, isNewWorkout = false)
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

        if (favoriteWorkoutState.value.isError) {
            _favoriteWorkoutState.value = BaseState()
        }

        if (unfavoriteWorkoutState.value.isError) {
            _unfavoriteWorkoutState.value = BaseState()
        }

        if (getFavoriteWorkoutsState.value.isError) {
            _getFavoriteWorkoutsState.value = WorkoutListState()
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

/**
 * Moved createNewWorkout logic in WorkoutDetails screen
 */

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
