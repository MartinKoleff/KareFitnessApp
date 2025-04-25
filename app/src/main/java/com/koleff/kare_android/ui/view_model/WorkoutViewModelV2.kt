package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.event.OnWorkoutScreenSwitchEvent
import com.koleff.kare_android.ui.state.HasUpdated
import com.koleff.kare_android.ui.state.SearchState
import com.koleff.kare_android.ui.state.WorkoutListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModelV2 @Inject constructor(
    private val workoutUseCases: WorkoutUseCases,
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    val hasUpdated: HasUpdated,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController), MainScreenNavigation {

    private var _state: MutableStateFlow<WorkoutListState> = MutableStateFlow(WorkoutListState())
    val state: StateFlow<WorkoutListState>
        get() = _state

    private var _getFavoriteWorkoutsState: MutableStateFlow<WorkoutListState> =
        MutableStateFlow(WorkoutListState())
    val getFavoriteWorkoutsState: StateFlow<WorkoutListState>
        get() = _getFavoriteWorkoutsState

    val selectedWorkoutList
        get() = if (state.value.isFavoriteWorkoutsScreen)
            _state.value.workoutList.filter { it.isFavorite }
        else _state.value.workoutList

    val selectedSearchWorkoutList
        get() = if (state.value.isFavoriteWorkoutsScreen)
            originalFavoriteWorkoutList
        else originalWorkoutList

    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()
    private var originalFavoriteWorkoutList: List<WorkoutDto> = mutableListOf()

    val isRefreshing by mutableStateOf(state.value.isLoading)
    private val hasLoadedFromCache = mutableStateOf(false)

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState

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

    fun onTextChange(searchText: String) {
        Log.d("WorkoutViewModelV2", "onTextChange called: $searchText")
        Log.d("WorkoutViewModelV2", "SelectedWorkoutList: $selectedWorkoutList")

        _searchState.value = searchState.value.copy(
            searchText = searchText
        )

        val event = OnSearchWorkoutEvent.OnSearchTextChange(
            searchText = _searchState.value.searchText,
            workouts = selectedSearchWorkoutList
        )

        onSearchEvent(event)
    }


    fun onToggleSearch() {
        val isSearching = searchState.value.isSearching
        Log.d("WorkoutViewModelV2", "onToggleSearch called: ${!isSearching}")
        Log.d("WorkoutViewModelV2", "SelectedWorkoutList: $selectedWorkoutList")
        Log.d("WorkoutViewModelV2", "SelectedSearchWorkoutList: $selectedSearchWorkoutList")

        _searchState.value = searchState.value.copy(
            isSearching = !isSearching
        )

        val event = OnSearchWorkoutEvent.OnToggleSearch(
            isSearching = searchState.value.isSearching,
            workouts = selectedSearchWorkoutList
        )

        onSearchEvent(event)
    }

    private fun onSearchEvent(event: OnSearchWorkoutEvent) {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.onSearchWorkoutUseCase(event).collect { searchWorkoutState ->
                _state.value =
                    searchWorkoutState.copy(isFavoriteWorkoutsScreen = _state.value.isFavoriteWorkoutsScreen)

                Log.d("WorkoutViewModelV2", "onSearchEvent called. Event: $event")
                Log.d("WorkoutViewModelV2", "Search workout state: $searchWorkoutState")
                Log.d("WorkoutViewModelV2", "SelectedWorkoutList: $selectedWorkoutList")
            }
        }
    }

    suspend fun onWorkoutFilterEvent(event: OnWorkoutScreenSwitchEvent) {
        withContext(dispatcher) {
//            val currentWorkoutList = state.value.workoutList
            _state.value = WorkoutListState(
                isLoading = true
            )
            delay(Constants.fakeDelay)

            when (event) {
                OnWorkoutScreenSwitchEvent.AllWorkouts -> {
                    _state.value = WorkoutListState(
                        workoutList = selectedSearchWorkoutList,
                        isSuccessful = true,
                        isFavoriteWorkoutsScreen = false,
                    )
                }

                OnWorkoutScreenSwitchEvent.FavoriteWorkouts -> {
                    _state.value = WorkoutListState(
                        workoutList = selectedSearchWorkoutList,
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

    fun onWorkoutFilter(searchText: String, event: OnWorkoutScreenSwitchEvent) {
        viewModelScope.launch {

            //No search
            if (!searchState.value.isSearching || searchText.isEmpty()) {
                onWorkoutFilterEvent(event)
                return@launch
            }

            onTextChange(searchText)
        }
    }

    fun getFavoriteWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getFavoriteWorkoutsUseCase().collect { getFavoriteWorkoutsState ->
                _getFavoriteWorkoutsState.value = getFavoriteWorkoutsState

                if (getFavoriteWorkoutsState.isSuccessful) {
                    originalFavoriteWorkoutList = getFavoriteWorkoutsState.workoutList
                }
            }
        }
    }

    fun getWorkouts() {
        viewModelScope.launch(dispatcher) {
            workoutUseCases.getAllWorkoutsUseCase().collect { workoutState ->
                _state.value = workoutState
//                isRefreshing = workoutState.isLoading

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList
                    originalFavoriteWorkoutList = workoutState.workoutList.filter { it.isFavorite }
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

        if (getFavoriteWorkoutsState.value.isError) {
            _getFavoriteWorkoutsState.value = WorkoutListState()
        }
    }
}