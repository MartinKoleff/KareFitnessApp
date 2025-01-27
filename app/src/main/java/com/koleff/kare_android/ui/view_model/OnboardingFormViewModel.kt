package com.koleff.kare_android.ui.view_model

import android.util.Log
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.Gender
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OnboardingFormViewModel @Inject constructor(
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(navigationController = navigationController) { //, OnboardingScreenNavigation


    fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }

    override fun clearError() {

    }

    fun completeOnboarding(
        gender: Gender,
        height: Int,
        age: Int,
        weight: Int
    ){
        Log.d("OnboardingFormViewModel", "Onboarding data: $gender, $height, $age, $weight")
    }
}
