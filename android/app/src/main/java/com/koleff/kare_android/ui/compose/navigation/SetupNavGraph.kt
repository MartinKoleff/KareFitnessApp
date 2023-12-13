package com.koleff.kare_android.ui.compose.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.event.OnWorkoutDetailsEvent
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsConfiguratorScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.MyWorkoutScreen
import com.koleff.kare_android.ui.compose.screen.SearchExercisesScreen
import com.koleff.kare_android.ui.compose.screen.SearchWorkoutsScreen
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen
import com.koleff.kare_android.ui.view_model.DashboardViewModel
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    exerciseListViewModelFactory: ExerciseListViewModel.Factory,
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
                exerciseListViewModelFactory = exerciseListViewModelFactory,
                dashboardViewModel = dashboardViewModel
            )
        }
        composable(route = MainScreen.WorkoutDetails.route) { backStackEntry ->
            val workoutId =
                backStackEntry.arguments?.getString("workout_id")?.toInt() ?: -1

            val workoutDetailsViewModel = viewModel<WorkoutDetailsViewModel>(
                factory = WorkoutDetailsViewModel.provideWorkoutDetailsViewModelFactory(
                    factory = workoutDetailsViewModelFactory,
                    workoutId = workoutId
                )
            )

            WorkoutDetailsScreen(
                workoutId = workoutId,
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutDetailsViewModel = workoutDetailsViewModel
            )
        }
        composable(MainScreen.ExerciseDetails.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            val initialMuscleGroupId =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            val initialMuscleGroup = MuscleGroup.fromId(initialMuscleGroupId)

            val exerciseDetailsViewModel = viewModel<ExerciseDetailsViewModel>(
                factory = ExerciseDetailsViewModel.provideExerciseDetailsViewModelFactory(
                    factory = exerciseDetailsViewModelFactory,
                    exerciseId = exerciseId,
                    initialMuscleGroup = initialMuscleGroup
                )
            )

            ExerciseDetailsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseDetailsViewModel = exerciseDetailsViewModel
            )
        }
        composable(MainScreen.ExerciseDetailsConfigurator.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            val exerciseViewModel = viewModel<ExerciseViewModel>(
                factory = ExerciseViewModel.provideExerciseViewModelFactory(
                    factory = exerciseViewModelFactory,
                    exerciseId = exerciseId
                )
            )

            val workoutId =
                backStackEntry.arguments?.getString("workout_id")?.toInt() ?: -1

            val workoutDetailsViewModel = viewModel<WorkoutDetailsViewModel>(
                factory = WorkoutDetailsViewModel.provideWorkoutDetailsViewModelFactory(
                    factory = workoutDetailsViewModelFactory,
                    workoutId = workoutId
                )
            )

            ExerciseDetailsConfiguratorScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseViewModel = exerciseViewModel,
                workoutDetailsViewModel = workoutDetailsViewModel
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
        composable(MainScreen.SearchExercisesScreen.route) { backStackEntry ->
            val workoutId =
                backStackEntry.arguments?.getString("workout_id")?.toInt() ?: -1

            SearchExercisesScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutId = workoutId
            )
        }
    }
}