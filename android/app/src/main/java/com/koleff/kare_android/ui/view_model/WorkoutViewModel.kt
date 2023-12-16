package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.event.OnSearchEvent
import com.koleff.kare_android.data.model.event.OnWorkoutScreenSwitchEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.state.SearchState
import com.koleff.kare_android.data.model.state.WorkoutState
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val getWorkoutsUseCase: GetWorkoutsUseCase,
    private val getWorkoutUseCase: GetWorkoutUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<WorkoutState> = MutableStateFlow(WorkoutState())
    val state: StateFlow<WorkoutState>
        get() = _state

    private var originalWorkoutList: List<WorkoutDto> = mutableListOf()

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState

//    init {
//        getWorkouts()
//    }

    fun onSearchEvent(event: OnSearchEvent) {
        when (event) {
            is OnSearchEvent.OnToggleSearch -> {
                val isSearching = _searchState.value.isSearching
                _searchState.value = searchState.value.copy(
                    isSearching = !isSearching
                )

                if (!isSearching) {
                    onSearchEvent(OnSearchEvent.OnSearchTextChange(""))
                }
            }

            is OnSearchEvent.OnSearchTextChange -> {
                _searchState.value = searchState.value.copy(
                    searchText = event.searchText
                )

                //Search filter
                _state.value = state.value.copy(
                    workoutList = originalWorkoutList.filter {

                        //Custom search filter...
                        it.name.contains(event.searchText, ignoreCase = true)
                    }
                )
            }
        }
    }

    fun onEvent(event: OnWorkoutScreenSwitchEvent) {
        viewModelScope.launch(dispatcher) {
            _state.value = WorkoutState(
                isLoading = true
            )
            delay(Constants.fakeSmallDelay)

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
                        workoutList = originalWorkoutList.filter {
                            it.isSelected
                        },
                        isMyWorkoutScreen = true,
                        isLoading = false
                    )
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
//        viewModelScope.launch {
//            getWorkoutUseCase(workoutId).collect { workoutState ->
//                _state.value = workoutState
//            }
//        }
//    }

    fun getWorkouts() {
        viewModelScope.launch {
            getWorkoutsUseCase().collect { workoutState ->
                _state.value = workoutState

                if (workoutState.isSuccessful) {
                    originalWorkoutList = workoutState.workoutList ?: emptyList()
                }
            }
        }
    }
}
