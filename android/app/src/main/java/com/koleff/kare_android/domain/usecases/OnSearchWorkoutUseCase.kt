package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.WorkoutState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnSearchWorkoutUseCase() {

    suspend operator fun invoke(event: OnSearchWorkoutEvent): Flow<WorkoutState> =
        flow {
            when (event) {
                is OnSearchWorkoutEvent.OnToggleSearch -> {
                    val isSearching = event.isSearching

                    if (!isSearching) {
                        invoke(
                            OnSearchWorkoutEvent.OnSearchTextChange(
                                searchText = "",
                                workouts = event.workouts
                            )
                        )
                    }
                }

                is OnSearchWorkoutEvent.OnSearchTextChange -> {

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

