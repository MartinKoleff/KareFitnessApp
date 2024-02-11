package com.koleff.kare_android.common.navigation

//TODO: add navigation options for custom screen routes...
sealed interface NavigationEvent {
    data class NavigateTo(val destination: Destination) : NavigationEvent {
        val route
            get() = destination.route
    }

    data class ClearBackstackAndNavigateTo(val destination: Destination) : NavigationEvent {
        val route
            get() = destination.route
    }

    data object NavigateBack : NavigationEvent
}