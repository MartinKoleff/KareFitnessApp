package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.MainScreenScaffold

@Composable
fun ExerciseDetailsScreen(
    navController: NavHostController,
    exerciseId: Int = -1, //Invalid exercise selected...
    isNavigationInProgress: MutableState<Boolean>
) {
    //TODO: fetch exercise data with id.

    val exerciseName = ""
    MainScreenScaffold(exerciseName, navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

    }
}