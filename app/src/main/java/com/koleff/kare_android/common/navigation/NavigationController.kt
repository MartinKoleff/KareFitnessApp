package com.koleff.kare_android.common.navigation

interface NavigationController {
    suspend fun navigateTo(destination: Destination)
    suspend fun clearBackstackAndNavigateTo(destination: Destination)
    suspend fun navigateBack()
}