package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.data.model.dto.ExerciseData
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.event.OnFilterEvent
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.data.model.state.ExerciseState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel @AssistedInject constructor(
    private val exerciseRepository: ExerciseRepository,
    @Assisted private val muscleGroupId: Int,
    @Assisted private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _state: MutableStateFlow<ExerciseState> = MutableStateFlow(ExerciseState())
    val state: StateFlow<ExerciseState>
        get() = _state

    private var originalExerciseList: List<ExerciseData> = mutableListOf()

    init {
        getExercises(muscleGroupId)
    }

    fun onEvent(event: OnFilterEvent) {
        when (event) {
            OnFilterEvent.DumbbellFilter -> {
                _state.value = state.value.copy(
                    exerciseList = originalExerciseList.filter {
                        it.machineType == MachineType.DUMBBELL
                    }
                )
            }

            OnFilterEvent.BarbellFilter -> {
                _state.value = state.value.copy(
                    exerciseList = originalExerciseList.filter {
                        it.machineType == MachineType.BARBELL
                    }
                )
            }

            OnFilterEvent.MachineFilter -> {
                _state.value = state.value.copy(
                    exerciseList = originalExerciseList.filter {
                        it.machineType == MachineType.MACHINE
                    }
                )
            }

            OnFilterEvent.NoFilter -> {
                _state.value = state.value.copy(
                    exerciseList = originalExerciseList
                )
            }
        }
    }

    private fun getExercises(muscleGroupId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseRepository.getExercises(muscleGroupId).collect { apiResult ->
                when (apiResult) {
                    is ResultWrapper.ApiError -> {
                        _state.value = ExerciseState(
                            isError = true,
                            error = apiResult.error ?: KareError.GENERIC
                        )
                    }

                    is ResultWrapper.Loading -> {
                        _state.value = ExerciseState(isLoading = true)
                    }

                    is ResultWrapper.Success -> {
                        Log.d("ExerciseViewModel", "Flow received.")

                        _state.value = ExerciseState(
                            isSuccessful = true,
                            exerciseList = apiResult.data.exercises
                                .map(ExerciseDto::toExerciseData)
                        )

                        originalExerciseList = _state.value.exerciseList
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(muscleGroupId: Int, dispatcher: CoroutineDispatcher): ExerciseViewModel
    }

    companion object {
        fun provideExerciseViewModelFactory(
            factory: Factory,
            muscleGroupId: Int,
            dispatcher: CoroutineDispatcher = Dispatchers.Main
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(muscleGroupId, dispatcher) as T
            }
        }
    }
}
