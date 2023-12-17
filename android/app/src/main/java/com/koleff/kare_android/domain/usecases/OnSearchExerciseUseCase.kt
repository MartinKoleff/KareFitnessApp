package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.event.OnSearchExerciseEvent
import com.koleff.kare_android.data.model.event.OnSearchWorkoutEvent
import com.koleff.kare_android.data.model.state.ExerciseState
import com.koleff.kare_android.data.model.state.ExercisesState
import com.koleff.kare_android.data.model.state.WorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnSearchExerciseUseCase() {

    suspend operator fun invoke(event: OnSearchExerciseEvent): Flow<ExercisesState> =
        flow {
            when (event) {
                is OnSearchExerciseEvent.OnToggleSearch -> {
                    val isSearching = event.isSearching

                    if (!isSearching) {
                        invoke(
                            OnSearchExerciseEvent.OnSearchTextChange(
                                searchText = "",
                                exercises = event.exercises
                            )
                        )
                    }
                }

                is OnSearchExerciseEvent.OnSearchTextChange -> {

                    //Search filter
                    emit(
                        ExercisesState(
                            exerciseList = event.exercises.filter {

                                //Custom search filter...
                                it.name.contains(event.searchText, ignoreCase = true)
                            }
                        )
                    )
                }
            }
        }
}

