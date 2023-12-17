package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.data.model.event.OnSearchEvent
import com.koleff.kare_android.data.model.state.SearchState
import com.koleff.kare_android.data.model.state.WorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnSearchWorkoutUseCase() {

    suspend operator fun invoke(event: OnSearchEvent): Flow<WorkoutState> =
        flow {
            when (event) {
                is OnSearchEvent.OnToggleSearch -> {
                    val isSearching = event.isSearching

                    if (!isSearching) {
                        invoke(
                            OnSearchEvent.OnSearchTextChange(
                                searchText = "",
                                workouts = event.workouts
                            )
                        )
                    }
                }

                is OnSearchEvent.OnSearchTextChange -> {

                    //Search filter
                    emit(
                        WorkoutState(
                            workoutList = event.workouts.filter {

                                //Custom search filter...
                                it.name.contains(event.searchText, ignoreCase = true)
                            }
                        )
                    )
                }
            }
        }
}

