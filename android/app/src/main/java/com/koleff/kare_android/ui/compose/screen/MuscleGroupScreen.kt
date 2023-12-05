package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.common.DataManager
import com.koleff.kare_android.ui.compose.MachineFilterSegmentButton
import com.koleff.kare_android.ui.compose.MainScreenScaffold

@Composable
fun MuscleGroupScreen(
    navController: NavHostController,
    muscleGroupId: Int = -1, //Invalid group selected...
    isNavigationInProgress: MutableState<Boolean>
) {
    val muscleGroup = DataManager.muscleGroupList[muscleGroupId] //TODO: add invalid muscle group id handling...

    MainScreenScaffold(muscleGroup.name, navController, isNavigationInProgress) { innerPadding ->

        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()

        MachineFilterSegmentButton(modifier = modifier, selectedOptionIndex = -1)
    }
}