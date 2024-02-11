package com.koleff.kare_android.ui.navigation

interface NavigationController {
    suspend fun navigateTo(destination: Destination)
    suspend fun clearBackstackAndNavigateTo(destination: Destination)
    suspend fun navigateBack()
}