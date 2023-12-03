package com.koleff.kare_android.ui.compose.screen

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.MainScreenScaffold

@Composable
fun ExerciseDetailsScreen(
    navController: NavHostController,
    exerciseId: Int = -1 //Invalid exercise selected...
) {
    //TODO: fetch exercise data with id.

    val exerciseName = ""
    MainScreenScaffold(exerciseName, navController) {

    }
}