package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.Constants.fakeSmallDelay
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.event.OnFilterEvent
import com.koleff.kare_android.data.model.event.OnSearchEvent
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.data.model.state.ExercisesState
import com.koleff.kare_android.data.model.state.SearchState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExerciseListViewModel @AssistedInject constructor(
    private val exerciseRepository: ExerciseRepository,
    @Assisted private val muscleGroupId: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<ExercisesState> = MutableStateFlow(ExercisesState())
    val state: StateFlow<ExercisesState>
        get() = _state

    private var originalExerciseList: List<ExerciseDto> = mutableListOf()

    private val _searchState: MutableStateFlow<SearchState> = MutableStateFlow(SearchState())
    val searchState: StateFlow<SearchState>
        get() = _searchState

//    val searchState = _searchState
//        .debounce(Constants.fakeSmallDelay)
//        .onEach { exercisesState ->  exercisesState.isSearching = true }
//        .combine(_state) { searchState, exercisesState ->
//            when {
//                searchState.searchText.isNotEmpty() -> exercisesState.exerciseList.filter { exercises ->
//                    exercises.name.contains(searchState.searchText, ignoreCase = true)
//                }
//
//                else -> exercisesState.exerciseList
//            }
//        }.stateIn(
//            scope = viewModelScope,
//            initialValue = _searchState,
//            started = SharingStarted.WhileSubscribed(5000L)
//        )

    init {
        getExercises(muscleGroupId)
    }

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
                    exerciseList = originalExerciseList.filter {
                        it.name.contains(event.searchText)
                    }
                )
            }
        }
    }

    fun onFilterEvent(event: OnFilterEvent) {
        viewModelScope.launch(dispatcher) {
            _state.value = state.value.copy(
                isLoading = true
            )
            delay(fakeSmallDelay)

            when (event) {
                OnFilterEvent.DumbbellFilter -> {
                    _state.value = state.value.copy(
                        exerciseList = originalExerciseList.filter {
                            it.machineType == MachineType.DUMBBELL
                        },
                        isLoading = false
                    )
                }

                OnFilterEvent.BarbellFilter -> {
                    _state.value = state.value.copy(
                        exerciseList = originalExerciseList.filter {
                            it.machineType == MachineType.BARBELL
                        },
                        isLoading = false
                    )
                }

                OnFilterEvent.MachineFilter -> {
                    _state.value = state.value.copy(
                        exerciseList = originalExerciseList.filter {
                            it.machineType == MachineType.MACHINE
                        },
                        isLoading = false
                    )
                }

                OnFilterEvent.CalisthenicsFilter -> {
                    _state.value = state.value.copy(
                        exerciseList = originalExerciseList.filter {
                            it.machineType == MachineType.CALISTHENICS
                        },
                        isLoading = false
                    )
                }

                OnFilterEvent.NoFilter -> {
                    _state.value = state.value.copy(
                        exerciseList = originalExerciseList,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun getExercises(muscleGroupId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseRepository.getExercises(muscleGroupId).collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        _state.value = ExercisesState(
                            isError = true,
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    }

                    is ResultWrapper.Loading -> {
                        _state.value = ExercisesState(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        Log.d("ExerciseListViewModel", "Flow received.")

                        _state.value = ExercisesState(
                            isSuccessful = true,
                            exerciseList = apiResult.data.exercises
                        )

                        originalExerciseList = _state.value.exerciseList
                    }
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
