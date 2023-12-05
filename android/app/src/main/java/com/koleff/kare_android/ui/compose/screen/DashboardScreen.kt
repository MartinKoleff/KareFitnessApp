package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.navigation.NavHostController
import com.koleff.kare_android.R
import com.koleff.kare_android.common.DataManager
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.compose.MainScreenScaffold
import com.koleff.kare_android.ui.compose.MuscleGroupGrid

@Composable
fun DashboardScreen(navController: NavHostController) {
    MainScreenScaffold("Dashboard", navController) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        MuscleGroupGrid(
            modifier = modifier,
            navController = navController,
            muscleGroupList = DataManager.muscleGroupList
        )
    }
}