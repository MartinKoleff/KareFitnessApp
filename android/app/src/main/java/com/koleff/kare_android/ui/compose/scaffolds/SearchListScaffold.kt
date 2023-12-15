package com.koleff.kare_android.ui.compose.scaffolds

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.navigation.BottomNavigationBar
import com.koleff.kare_android.ui.compose.navigation.Toolbar

@Composable
fun SearchListScaffold(
    screenTitle: String,
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Toolbar(navController = navController, title = screenTitle, isNavigationInProgress = isNavigationInProgress)
        }
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}