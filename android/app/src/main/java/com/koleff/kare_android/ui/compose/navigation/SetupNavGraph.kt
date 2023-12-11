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
import com.koleff.kare_android.ui.compose.screen.WorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen
import com.koleff.kare_android.ui.view_model.DashboardViewModel
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    exerciseViewModelFactory: ExerciseViewModel.Factory,
    exerciseDetailsViewModelFactory: ExerciseDetailsViewModel.Factory,
    workoutDetailsViewModelFactory: WorkoutDetailsViewModel.Factory
) {
    val isNavigationInProgress = rememberSaveable {
        mutableStateOf(false)
    }

    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val workoutViewModel: WorkoutViewModel = hiltViewModel()


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
        composable(MainScreen.MyWorkout.route) {
            workoutViewModel.getWorkouts()

            MyWorkoutScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutListViewModel = workoutViewModel
            )
        }
        composable(MainScreen.Workouts.route) {
            WorkoutsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutListViewModel = workoutViewModel
            )
        }
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
        composable(MainScreen.WorkoutDetails.route) { backStackEntry ->
            val workoutId =
                backStackEntry.arguments?.getString("workout_id")?.toInt() ?: -1

            WorkoutDetailsScreen(
                workoutId = workoutId,
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutDetailsViewModelFactory = workoutDetailsViewModelFactory
            )
        }
        composable(MainScreen.ExerciseDetails.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            val initialMuscleGroupId =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            ExerciseDetailsScreen(
                exerciseId = exerciseId,
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseDetailsViewModelFactory = exerciseDetailsViewModelFactory,
                initialMuscleGroupId = initialMuscleGroupId
            )
        }
        composable(MainScreen.Settings.route) {
            SettingsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.SearchWorkoutsScreen.route) {
            SearchWorkoutsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
    }
}