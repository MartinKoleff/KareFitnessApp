package com.koleff.kare_android.ui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.MyWorkoutScreen
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = MainScreen.Dashboard.route
    ) {
        composable(MainScreen.Dashboard.route) { DashboardScreen(navController) }
        composable(MainScreen.MyWorkout.route) { MyWorkoutScreen(navController) }
        composable(MainScreen.Workouts.route) { WorkoutsScreen(navController) }
        composable(MainScreen.MuscleGroup.route) { backStackEntry ->
            val muscleGroupId =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            MuscleGroupScreen(
                muscleGroupId = muscleGroupId,
                navController = navController
            )
        }
        composable(MainScreen.ExerciseDetails.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            ExerciseDetailsScreen(
                exerciseId = exerciseId,
                navController = navController
            )
        }
        composable(MainScreen.Settings.route) { SettingsScreen(navController) }
    }
}