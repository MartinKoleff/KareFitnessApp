package com.koleff.kare_android.ui.view_model

import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.domain.repository.LanguageRepository
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.ExerciseListState
import com.koleff.kare_android.ui.state.SupportedLanguagesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChangeLanguageViewModel @Inject constructor(
    private val languageRepository: LanguageRepository,
    private val navigationController: NavigationController
) : BaseViewModel(navigationController), MainScreenNavigation {

    private val _getSupportedLanguagesState: MutableStateFlow<SupportedLanguagesState> =
        MutableStateFlow(SupportedLanguagesState())

    val getSupportedLanguagesState: StateFlow<SupportedLanguagesState>
        get() = _getSupportedLanguagesState

    private val _changeLanguageState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())

    val changeLanguageState: StateFlow<BaseState>
        get() = _changeLanguageState


    init {
        getSupportedLanguages()
    }

    private fun getSupportedLanguages(){
        languageRepository.getSupportedLanguages()
    }

    override fun clearError() {
        if (getSupportedLanguagesState.value.isError) {
            _getSupportedLanguagesState.value = SupportedLanguagesState()
        }

        if (changeLanguageState.value.isError) {
            _changeLanguageState.value = BaseState()
        }
    }

    override fun onNavigateToDashboard() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }

    override fun onNavigateToWorkouts() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }

    override fun onNavigateToSettings() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }

    override fun onNavigateBack()  {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }
}