package com.koleff.kare_android.common.navigation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject

class NavigationControllerImpl @Inject constructor() : NavigationController, NavigationNotifier {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>()

    override val navigationEvents: Flow<NavigationEvent> = _navigationEvents.asSharedFlow()

    override suspend fun navigateTo(destination: Destination) {
        _navigationEvents.emit(NavigationEvent.NavigateTo(destination))
    }

    //Used for custom routes
    override suspend fun navigateToRoute(route: String) {
        _navigationEvents.emit(NavigationEvent.NavigateToRoute(route))
    }

    override suspend fun clearBackstackAndNavigateTo(destination: Destination) {
        _navigationEvents.emit(NavigationEvent.ClearBackstackAndNavigateTo(destination))
    }

    override suspend fun clearBackstackAndNavigateToRoute(route: String) {
        _navigationEvents.emit(NavigationEvent.ClearBackstackAndNavigateToRoute(route))
    }

    override suspend fun navigateBack() {
        _navigationEvents.emit(NavigationEvent.NavigateBack)
    }
}

