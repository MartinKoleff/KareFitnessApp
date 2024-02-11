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
    modifier: Modifier = Modifier,
    screenTitle: String,
    onNavigateBackAction: () -> Unit,
    onNavigateToAction: () -> Unit,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Toolbar(
                title = screenTitle,
                onNavigateBackAction = onNavigateBackAction,
                onNavigateToAction = onNavigateToAction
            )
        }
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}