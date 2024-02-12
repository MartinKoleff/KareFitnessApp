package com.koleff.kare_android.common.navigation

interface NavigationController {
    suspend fun navigateTo(destination: Destination)
    suspend fun navigateToRoute(route: String)
    suspend fun clearBackstackAndNavigateTo(destination: Destination)
    suspend fun clearBackstackAndNavigateToRoute(route: String)
    suspend fun popUpToAndNavigateTo(
        popUpToRoute: String,
        destinationRoute: String,
        inclusive: Boolean = false,
        saveState: Boolean = false
    )

    suspend fun navigateBack()
}