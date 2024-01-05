package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.SettingsList

@Composable
fun SettingsScreen(navController: NavHostController, isNavigationInProgress: MutableState<Boolean>) {
    MainScreenScaffold("Settings", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        SettingsList(modifier = modifier)
    }
}