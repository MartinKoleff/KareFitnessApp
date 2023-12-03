package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.navigation.BottomNavigationBar
import com.koleff.kare_android.ui.compose.navigation.Toolbar

@Composable
fun MainScreenScaffold(
    screenTitle: String,
    navController: NavHostController,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Toolbar(navController = navController, title = screenTitle)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}