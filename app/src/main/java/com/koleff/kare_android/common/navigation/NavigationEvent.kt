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

    //Used for custom routes
    data class ClearBackstackAndNavigateToRoute(val route: String) : NavigationEvent

    data class PopUpToAndNavigateTo(
        val popUpToRoute: String,
        val destinationRoute: String,
        val inclusive: Boolean = false, //Optionally clear the popped destination from the back stack
        val saveState: Boolean = false //Optionally save the state of the popped destination
    ) : NavigationEvent

    data object NavigateBack : NavigationEvent
}