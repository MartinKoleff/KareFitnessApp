package com.koleff.kare_android.common.navigation

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Singleton

@Singleton
class NavigationControllerImpl: NavigationController, NavigationNotifier {

    private val _navigationEvents = MutableSharedFlow<NavigationEvent>(replay = 1)
    override val navigationEvents: Flow<NavigationEvent> = _navigationEvents.asSharedFlow()

    override suspend fun navigateTo(destination: Destination) {
        Log.d("NavigationController", "Emitting NavigateTo Event {${destination.route}}")
        _navigationEvents.emit(NavigationEvent.NavigateTo(destination))
    }

    override suspend fun clearBackstackAndNavigateTo(destination: Destination) {
        Log.d(
            "NavigationController",
            "Emitting ClearBackstackAndNavigateTo Event {${destination.route}}"
        )
        _navigationEvents.emit(NavigationEvent.ClearBackstackAndNavigateTo(destination))
    }

    override suspend fun popUpToAndNavigateTo(
        popUpToRoute: String,
        destinationRoute: String,
        inclusive: Boolean,
        saveState: Boolean
    ) {
        Log.d(
            "NavigationController",
            "Emitting PopUpToAndNavigateTo Event. Destination: {$destinationRoute}, PopUpToRoute: {$popUpToRoute}, Inclusive: $inclusive"
        )
        _navigationEvents.emit(
            NavigationEvent.PopUpToAndNavigateTo(
                popUpToRoute = popUpToRoute,
                destinationRoute = destinationRoute,
                inclusive = inclusive,
                saveState = saveState
            )
        )
    }

    override suspend fun navigateBack() {
        Log.d("NavigationController", "Emitting NavigateBack Event")
        _navigationEvents.emit(NavigationEvent.NavigateBack)
    }
}

