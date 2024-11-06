package com.koleff.kare_android.ui.view_model

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.domain.usecases.LanguageUseCases
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.state.SupportedLanguagesState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChangeLanguageViewModel @Inject constructor(
    private val languageUseCases: LanguageUseCases,
    private val navigationController: NavigationController,
) : BaseViewModel(navigationController), MainScreenNavigation {

    private var _getSupportedLanguagesState: MutableStateFlow<SupportedLanguagesState> =
        MutableStateFlow(SupportedLanguagesState())

    val getSupportedLanguagesState: StateFlow<SupportedLanguagesState>
        get() = _getSupportedLanguagesState

    private var _changeLanguageState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())

    val changeLanguageState: StateFlow<BaseState>
        get() = _changeLanguageState


    init {
        getSupportedLanguages()
    }

    private fun getSupportedLanguages() {
        viewModelScope.launch(Dispatchers.Main) {
            languageUseCases.getSupportedLanguagesUseCase.invoke().collect { apiResult ->
                if (apiResult.isLoading) {
                    _getSupportedLanguagesState.value = getSupportedLanguagesState.value.copy(
                        isLoading = true
                    )
                } else {
                    _getSupportedLanguagesState.value = apiResult
                }
            }
        }
    }

    fun changeLanguage(context: Context, selectedLanguage: KareLanguage) {
        viewModelScope.launch(Dispatchers.Main) {
            languageUseCases.changeLanguageUseCase(context, selectedLanguage).collect { apiResult ->
               _changeLanguageState.value = apiResult

                if(apiResult.isSuccessful){
                    onNavigateBack()
                }
            }
        }
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

    override fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }
}