package com.koleff.kare_android.ui.view_model

import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val navigationController: NavigationController,
) : BaseViewModel(navigationController = navigationController) {


    //Navigation
    fun navigateToLogin() {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.Login
            )
        )
    }

    fun navigateToRegister() {
        super.onNavigationEvent(
            NavigationEvent.NavigateTo(
                Destination.Register
            )
        )
    }
}
