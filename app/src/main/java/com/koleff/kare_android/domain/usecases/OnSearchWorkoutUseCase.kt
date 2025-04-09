package com.koleff.kare_android.domain.usecases

import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.WorkoutListState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow

class OnSearchWorkoutUseCase() {

    operator fun invoke(event: OnSearchWorkoutEvent): Flow<WorkoutListState> =
        flow {
            when (event) {
                is OnSearchWorkoutEvent.OnToggleSearch -> {
                    emit(WorkoutListState(isLoading = true))
                    delay(Constants.fakeSmallDelay)

                    val isSearching = event.isSearching

                    if (!isSearching) {
                        emitAll(
                            invoke(
                                OnSearchWorkoutEvent.OnSearchTextChange(
                                    searchText = "",
                                    workouts = event.workouts
                                )
                            )
                        )
                    }
                }

                is OnSearchWorkoutEvent.OnSearchTextChange -> {
                    emit(WorkoutListState(isLoading = true))
                    delay(Constants.fakeDelay)

                    val workouts = if (event.searchText.isEmpty()) {
                        emptyList()
                    } else event.workouts.filter {

                        //Custom search filter...
                        it.name.contains(event.searchText, ignoreCase = true)
                    }

                    //Search filter
                    emit(
                        WorkoutListState(
                            workoutList = workouts,
                            isSuccessful = true
                        )
                    )
                }
            }
        }
}

