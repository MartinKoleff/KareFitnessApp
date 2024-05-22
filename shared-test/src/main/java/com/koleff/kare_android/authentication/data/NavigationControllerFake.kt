package com.koleff.kare_android.authentication.data

import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController

class NavigationControllerFake: NavigationController {
    var lastDestination: Destination? = null
        private set

    var lastPopUpToRoute: String? = null
        private set

    var lastDestinationRoute: String? = null
        private set

    var isBackstackCleared = false
        private set

    var isNavigateBackCalled = false
        private set

    override suspend fun navigateTo(destination: Destination) {
        lastDestination = destination
    }

    override suspend fun clearBackstackAndNavigateTo(destination: Destination) {
        isBackstackCleared = true
        lastDestination = destination
    }

    override suspend fun popUpToAndNavigateTo(
        popUpToRoute: String,
        destinationRoute: String,
        inclusive: Boolean,
        saveState: Boolean
    ) {
        lastPopUpToRoute = popUpToRoute
        lastDestinationRoute = destinationRoute
    }

    override suspend fun navigateBack() {
        isNavigateBackCalled = true
    }
}