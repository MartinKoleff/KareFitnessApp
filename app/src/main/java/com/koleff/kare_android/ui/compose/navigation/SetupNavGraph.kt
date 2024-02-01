package com.koleff.kare_android.ui.compose.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.data.model.dto.MuscleGroup
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
import com.koleff.kare_android.ui.view_model.ExerciseDetailsConfiguratorViewModel
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import com.koleff.kare_android.ui.view_model.SearchExercisesViewModel
import com.koleff.kare_android.ui.view_model.SearchWorkoutViewModel
import com.koleff.kare_android.ui.view_model.WorkoutDetailsViewModel
import com.koleff.kare_android.ui.view_model.WorkoutViewModel
import kotlinx.coroutines.flow.first

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SetupNavGraph(
    exerciseListViewModelFactory: ExerciseListViewModel.Factory,
    exerciseViewModelFactory: ExerciseViewModel.Factory,
    workoutDetailsViewModelFactory: WorkoutDetailsViewModel.Factory,
    exerciseDetailsConfiguratorViewModelFactory: ExerciseDetailsConfiguratorViewModel.Factory
) {
    val navController = rememberNavController()

    val isNavigationInProgress = rememberSaveable {
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
        composable(MainScreen.Workouts.route) {
            val workoutViewModel: WorkoutViewModel = hiltViewModel()

            WorkoutsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutListViewModel = workoutViewModel
            )
        }
        composable(MainScreen.MuscleGroupExercisesList.route) { backStackEntry ->
            val muscleGroupId =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            val exerciseListViewModel = viewModel<ExerciseListViewModel>(
                factory = ExerciseListViewModel.provideExerciseListViewModelFactory(
                    factory = exerciseListViewModelFactory,
                    muscleGroupId = muscleGroupId + 1
                )
            )

            MuscleGroupScreen(
                muscleGroupId = muscleGroupId,
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseListViewModel = exerciseListViewModel,
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
            ExerciseDetailsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.ExerciseDetailsConfigurator.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            val workoutId =
                backStackEntry.arguments?.getString("workout_id")?.toInt() ?: -1

            val exerciseDetailsConfiguratorViewModel = viewModel<ExerciseDetailsConfiguratorViewModel>(
                factory = ExerciseDetailsConfiguratorViewModel.provideExerciseDetailsConfiguratorViewModelFactory(
                    factory = exerciseDetailsConfiguratorViewModelFactory,
                    exerciseId = exerciseId,
                    workoutId = workoutId
                )
            )

            //If wired with ExerciseDetails...
            val initialMuscleGroupId =
                backStackEntry.arguments?.getString("muscle_group_id")?.toInt() ?: -1

            ExerciseDetailsConfiguratorScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exerciseDetailsConfiguratorViewModel = exerciseDetailsConfiguratorViewModel,
                initialMuscleGroupId = initialMuscleGroupId
            )
        }
        composable(MainScreen.Settings.route) {
            SettingsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress
            )
        }
        composable(MainScreen.SearchWorkoutsScreen.route) { backStackEntry ->
            val exerciseId =
                backStackEntry.arguments?.getString("exercise_id")?.toInt() ?: -1

            val exerciseViewModel = viewModel<ExerciseViewModel>(
                factory = ExerciseViewModel.provideExerciseViewModelFactory(
                    factory = exerciseViewModelFactory,
                    exerciseId = exerciseId
                )
            )

            val exerciseState by exerciseViewModel.state.collectAsState() //Recomposition will occur...
            Log.d("ExerciseViewModel", "Exercise selected in SearchWorkoutScreen: ${exerciseState.exercise}")

            val searchWorkoutViewModel = hiltViewModel<SearchWorkoutViewModel>()

            SearchWorkoutsScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                exercise = exerciseState.exercise,
                searchWorkoutViewModel = searchWorkoutViewModel
            )
        }
        composable(MainScreen.SearchExercisesScreen.route) { backStackEntry ->
            val workoutId =
                backStackEntry.arguments?.getString("workout_id")?.toInt() ?: -1

            val searchExercisesViewModel = hiltViewModel<SearchExercisesViewModel>()

            SearchExercisesScreen(
                navController = navController,
                isNavigationInProgress = isNavigationInProgress,
                workoutId = workoutId,
                searchExercisesViewModel = searchExercisesViewModel
            )
        }
    }
}