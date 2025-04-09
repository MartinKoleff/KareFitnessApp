package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.ui.state.ExerciseListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class OnSearchExerciseUseCase() {

    suspend operator fun invoke(event: OnSearchExerciseEvent): Flow<ExerciseListState> =
        flow {
            when (event) {
                is OnSearchExerciseEvent.OnToggleSearch -> {
                    val isSearching = event.isSearching

                    if (!isSearching) {
                        emitAll(
                            invoke(
                                OnSearchExerciseEvent.OnSearchTextChange(
                                    searchText = "",
                                    exercises = event.exercises,
                                )
                            )
                        )
                    }
                }

                is OnSearchExerciseEvent.OnSearchTextChange -> {

                    //Search filter
                    emit(
                        ExerciseListState(
                            exerciseList = event.exercises.filter {

                                //Custom search filter...
                                it.name.contains(event.searchText, ignoreCase = true)
                            },
                            isSuccessful = true
                        )
                    )
                }
            }
        }
}

