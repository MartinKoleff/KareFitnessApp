package com.koleff.kare_android.ui.view_model

import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(navigationController = navigationController) { //, OnboardingScreenNavigation


    fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }

    override fun clearError() {

    }

    fun skip() {
        navigateToFormsScreen()
    }

    fun navigateToFormsScreen() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.OnboardingForm))
    }
}
