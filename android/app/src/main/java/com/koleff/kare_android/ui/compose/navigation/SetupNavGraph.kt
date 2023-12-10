package com.koleff.kare_android.ui.compose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.MyWorkoutScreen
import com.koleff.kare_android.ui.compose.screen.SearchWorkoutsScreen
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen
import com.koleff.kare_android.ui.view_model.DashboardViewModel
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    exerciseViewModelFactory: ExerciseViewModel.Factory,
    exerciseDetailsViewModelFactory: ExerciseDetailsViewModel.Factory
) {
    val isNavigationInProgress = rememberSaveable  {
        mutableStateOf(false)
    }

    val dashboardViewModel: DashboardViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = MainScreen.Dashboard.route
    ) {
        composable(MainScreen.Dashboard.route) { backStackEntry ->
            DashboardScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                dashboardViewModel = dashboardViewModel
            )
        }
        composable(MainScreen.MyWorkout.route) { MyWorkoutScreen(navController, isNavigationInProgress) }
        composable(MainScreen.Workouts.route) { WorkoutsScreen(navController, isNavigationInProgress) }
        composable(MainScreen.MuscleGroupExercisesList.route) { backStackEntry ->
            val muscleGroupId =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            MuscleGroupScreen(
                muscleGroupId = muscleGroupId,
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseViewModelFactory = exerciseViewModelFactory,
                dashboardViewModel = dashboardViewModel
            )
        }
        composable(MainScreen.ExerciseDetails.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            val initialMuscleGroupId  =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            ExerciseDetailsScreen(
                exerciseId = exerciseId,
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseDetailsViewModelFactory = exerciseDetailsViewModelFactory,
                initialMuscleGroupId = initialMuscleGroupId
            )
        }
        composable(MainScreen.Settings.route) { SettingsScreen(navController, isNavigationInProgress) }
        composable(MainScreen.SearchWorkoutsScreen.route) { SearchWorkoutsScreen(navController, isNavigationInProgress) }
    }
}