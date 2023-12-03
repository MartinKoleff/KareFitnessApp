package com.koleff.kare_android.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.MainScreenScaffold

@Composable
fun MuscleGroupScreen(
    navController: NavHostController,
    muscleGroupId: Int = -1 //Invalid group selected...
) {
    val muscleGroupName = ""
    MainScreenScaffold(muscleGroupName, navController) {

    }
}