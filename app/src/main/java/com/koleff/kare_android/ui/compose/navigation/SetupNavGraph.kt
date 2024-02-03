package com.koleff.kare_android.ui.compose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsConfiguratorScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.SearchExercisesScreen
import com.koleff.kare_android.ui.compose.screen.SearchWorkoutsScreen
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen
import com.koleff.kare_android.ui.view_model.DashboardViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph() {
    val navController = rememberNavController()

    val isNavigationInProgress = rememberSaveable {
        mutableStateOf(false)
    }

    NavHost(
        navController = navController,
        startDestination = MainScreen.Dashboard.route
    ) {
        composable(MainScreen.Dashboard.route) { backStackEntry ->
            DashboardScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.Workouts.route) {
            WorkoutsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.MuscleGroupExercisesList.route) { backStackEntry ->
            MuscleGroupScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(route = MainScreen.WorkoutDetails.route) { backStackEntry ->
            WorkoutDetailsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.ExerciseDetails.route) { backStackEntry ->
            ExerciseDetailsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.ExerciseDetailsConfigurator.route) { backStackEntry ->
            ExerciseDetailsConfiguratorScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.Settings.route) {
            SettingsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.SearchWorkoutsScreen.route) { backStackEntry ->
            SearchWorkoutsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.SearchExercisesScreen.route) { backStackEntry ->
            SearchExercisesScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
            )
        }
    }
}