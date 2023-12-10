package com.koleff.kare_android.ui.compose

import ExerciseDetailsToolbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.navigation.BottomNavigationBar
import com.koleff.kare_android.ui.compose.navigation.Toolbar

@Composable
fun DetailsScreenScaffold(
    screenTitle: String,
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseImageId: Int,
    modifierPadding: @Composable (paddingValues: PaddingValues) -> Unit
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            ExerciseDetailsToolbar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight / 2.5f)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer
                    ),
                isNavigationInProgress = isNavigationInProgress,
                navController = navController,
                exerciseImageId = exerciseImageId
            )
        },
    ) { innerPadding ->
        modifierPadding(innerPadding)
    }
}