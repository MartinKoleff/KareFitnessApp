package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel(
    private val navigationController: NavigationController,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    fun onNavigationEvent(navigationEvent: NavigationEvent) {
        viewModelScope.launch(dispatcher) {
            when (navigationEvent) {
                is NavigationEvent.ClearBackstackAndNavigateTo -> {
                    navigationController.clearBackstackAndNavigateTo(navigationEvent.destination)
                }

                NavigationEvent.NavigateBack -> {
                    navigationController.navigateBack()
                }

                is NavigationEvent.NavigateTo -> {
                    navigationController.navigateTo(navigationEvent.destination)
                }

                is NavigationEvent.PopUpToAndNavigateTo -> {
                    navigationController.popUpToAndNavigateTo(
                        popUpToRoute = navigationEvent.popUpToRoute,
                        destinationRoute = navigationEvent.destinationRoute,
                        inclusive = navigationEvent.inclusive,
                        saveState = navigationEvent.saveState
                    )
                }
            }
        }
    }

    abstract fun clearError()
}