package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.common.DataManager
import com.koleff.kare_android.ui.compose.MainScreenScaffold

@Composable
fun MuscleGroupScreen(
    navController: NavHostController,
    muscleGroupId: Int = -1, //Invalid group selected...
    isNavigationInProgress: MutableState<Boolean>
) {
    val muscleGroup = DataManager.muscleGroupList[muscleGroupId]

    MainScreenScaffold(muscleGroup.name, navController, isNavigationInProgress) { innerPadding ->

        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

    }
}