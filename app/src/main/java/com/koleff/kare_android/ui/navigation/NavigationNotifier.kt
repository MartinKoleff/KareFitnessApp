package com.koleff.kare_android.ui.navigation

import kotlinx.coroutines.flow.Flow

interface NavigationNotifier {
    val navigationEvents: Flow<NavigationEvent>
}