package com.koleff.kare_android.common.navigation

import kotlinx.coroutines.flow.Flow

interface NavigationNotifier {
    val navigationEvents: Flow<NavigationEvent>
}