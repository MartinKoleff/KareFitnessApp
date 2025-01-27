package com.koleff.kare_android.ui.view_model

import android.util.Log
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.Gender
import com.koleff.kare_android.data.model.dto.OnboardingDataDto
import com.koleff.kare_android.domain.usecases.OnboardingUseCases
import com.koleff.kare_android.ui.state.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OnboardingFormViewModel @Inject constructor(
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    private val onboardingUseCases: OnboardingUseCases,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(navigationController = navigationController) { //, OnboardingScreenNavigation

    private val _saveOnboardingDataState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val saveOnboardingDataState: StateFlow<BaseState>
        get() = _saveOnboardingDataState

    fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }

    override fun clearError() {
        if(saveOnboardingDataState.value.isError){
            _saveOnboardingDataState.value = BaseState()
        }
    }

    suspend fun completeOnboarding(
        gender: Gender,
        height: Int,
        age: Int,
        weight: Int
    ) {
        Log.d("OnboardingFormViewModel", "Onboarding data: $gender, $height, $age, $weight")
        onboardingUseCases.saveOnboardingUseCase(
            OnboardingDataDto(
                gender = gender,
                height = height,
                age = age,
                weight = weight
            )
        ).collect { saveOnboardingDataState ->
            _saveOnboardingDataState.value = saveOnboardingDataState
        }
    }
}
