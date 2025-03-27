package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.event.OnSearchLanguageEvent
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.ui.state.SupportedLanguagesState
import com.koleff.kare_android.ui.state.WorkoutListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class OnSearchLanguageUseCase() {

    suspend operator fun invoke(event: OnSearchLanguageEvent): Flow<SupportedLanguagesState> =
        flow {
            when (event) {
                is OnSearchLanguageEvent.OnToggleSearch -> {
                    val isSearching = event.isSearching

                    if (!isSearching) {
                        invoke(
                            OnSearchLanguageEvent.OnSearchTextChange(
                                searchText = "",
                                languages = event.languages
                            )
                        )
                    }
                }

                is OnSearchLanguageEvent.OnSearchTextChange -> {

                    //Search filter
                    emit(
                        SupportedLanguagesState(
                            supportedLanguages = event.languages.filter {

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

