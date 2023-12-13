package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel

@Composable
fun SearchExercisesScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exercisesListViewModel: ExerciseListViewModel,
    workoutId: Int
) {
    SearchListScaffold("Select exercise", navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        SearchExercisesContent(modifier = modifier)
    }
}

@Composable
fun SearchExercisesContent(modifier: Modifier) {

}
