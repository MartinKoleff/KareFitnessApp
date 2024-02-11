package com.koleff.kare_android.ui.navigation

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