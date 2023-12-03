package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.MainScreenScaffold

@Composable
fun SettingsScreen(navController: NavHostController) {
    MainScreenScaffold("Settings", navController) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

    }
}