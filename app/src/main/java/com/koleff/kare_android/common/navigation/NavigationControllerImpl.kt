package com.koleff.kare_android.common.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class NavigationControllerImpl : NavigationController, NavigationNotifier {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    override val navigationEvents: Flow<NavigationEvent> = _navigationEvents.asSharedFlow()

    override suspend fun navigateTo(destination: Destination) {
        _navigationEvents.emit(NavigationEvent.NavigateTo(destination))
    }

    override suspend fun clearBackstackAndNavigateTo(destination: Destination) {
        _navigationEvents.emit(NavigationEvent.ClearBackstackAndNavigateTo(destination))
    }

    override suspend fun navigateBack() {
        _navigationEvents.emit(NavigationEvent.NavigateBack)
    }
}

