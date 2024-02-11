package com.koleff.kare_android.common.navigation

sealed interface NavigationEvent {
    data class NavigateTo(val destination: Destination) : NavigationEvent {
        val route
            get() = destination.route
    }

    //Used for custom routes
    data class NavigateToRoute(val route: String) : NavigationEvent

    data class ClearBackstackAndNavigateTo(val destination: Destination) : NavigationEvent {
        val route
            get() = destination.route
    }

    data object NavigateBack : NavigationEvent
}