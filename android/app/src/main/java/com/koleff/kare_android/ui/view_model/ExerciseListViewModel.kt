package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants.fakeSmallDelay
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.event.OnFilterExercisesEvent
import com.koleff.kare_android.data.model.event.OnSearchExerciseEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.state.ExercisesState
import com.koleff.kare_android.data.model.state.SearchState
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
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

    fun onTextChange(searchText: String) {
        _searchState.value = searchState.value.copy(
            searchText = searchText
        )

        val event = OnSearchExerciseEvent.OnSearchTextChange(
            searchText = _searchState.value.searchText,
            exercises = originalExerciseList
        )

        onSearchEvent(event)
    }

    fun onToggleSearch() {
        val isSearching = searchState.value.isSearching
        _searchState.value = searchState.value.copy(
            isSearching = !isSearching
        )

        val event = OnSearchExerciseEvent.OnToggleSearch(
            isSearching = searchState.value.isSearching,
            exercises = originalExerciseList
        )

        onSearchEvent(event)
    }

    private fun onSearchEvent(event: OnSearchExerciseEvent) {
        viewModelScope.launch(dispatcher) {
            exerciseUseCases.onSearchExerciseUseCase(event).collect { exerciseState ->
                _state.value = exerciseState
            }
        }
    }

    fun OnFilterExercisesEvent(machineType: MachineType) {
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
