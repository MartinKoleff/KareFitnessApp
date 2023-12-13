package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants.fakeSmallDelay
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.model.event.OnFilterEvent
import com.koleff.kare_android.data.model.state.ExerciseState
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.data.model.state.ExercisesState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExerciseViewModel @AssistedInject constructor(
    private val exerciseRepository: ExerciseRepository,
    @Assisted private val exerciseId: Int,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state: MutableStateFlow<ExerciseState> = MutableStateFlow(ExerciseState())
    val state: StateFlow<ExerciseState>
        get() = _state

    init {
        getExercise(exerciseId)
    }

    private fun getExercise(exerciseId: Int) {
        viewModelScope.launch(dispatcher) {
            exerciseRepository.getExercise(exerciseId).collect { apiResult ->
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
                        Log.d("ExerciseListViewModel", "Flow received.")

                        _state.value = ExerciseState(
                            isSuccessful = true,
                            exercise = apiResult.data.exercise
                        )
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(exerciseId: Int): ExerciseViewModel
    }

    companion object {
        fun provideExerciseViewModelFactory(
            factory: Factory,
            exerciseId: Int,
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return factory.create(exerciseId) as T
            }
        }
    }
}
