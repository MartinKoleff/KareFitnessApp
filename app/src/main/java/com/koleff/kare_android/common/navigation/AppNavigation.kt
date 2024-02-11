package com.koleff.kare_android.common.navigation

import android.util.Log
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsConfiguratorScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.SearchExercisesScreen
import com.koleff.kare_android.ui.compose.screen.SearchWorkoutsScreen
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn

@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun AppNavigation(
    navigationNotifier: NavigationNotifier
) {
    val navController = rememberNavController()

    //Navigation observer
    LaunchedEffect(Unit) {
        Log.d("AppNavigation", "Successfully registered navigation events observer!")

        navigationNotifier.navigationEvents
            .flowOn(Dispatchers.Main)
            .catch { e -> Log.e("AppNavigation", "Error collecting navigation events", e) }
            .debounce(Constants.navigationDelay)
            .collectLatest { navigationEvent ->

            Log.d("AppNavigation", "Navigation event: $navigationEvent")
            when (navigationEvent) {
                is NavigationEvent.NavigateTo -> navController.navigate(navigationEvent.route)
                is NavigationEvent.NavigateToRoute -> navController.navigate(navigationEvent.route)
                is NavigationEvent.ClearBackstackAndNavigateTo -> navController.navigate(
                    navigationEvent.route
                ) {
                    popUpTo(navController.graph.id)
                }

                is NavigationEvent.ClearBackstackAndNavigateToRoute -> navController.navigate(
                    navigationEvent.route
                ) {
                    popUpTo(navController.graph.id)
                }
                NavigationEvent.NavigateBack -> navController.popBackStack()
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Dashboard.route
    ) {
        addDestinations()
    }
}

private fun NavGraphBuilder.addDestinations() {
    composable(Destination.Dashboard.route) { backStackEntry ->
        DashboardScreen()
    }
    composable(Destination.Workouts.route) {
        WorkoutsScreen()
    }
    composable(Destination.MuscleGroupExercisesList.route) { backStackEntry ->
        MuscleGroupScreen()
    }
    composable(Destination.WorkoutDetails.route) { backStackEntry ->
        WorkoutDetailsScreen()
    }
    composable(Destination.ExerciseDetails.route) { backStackEntry ->
        ExerciseDetailsScreen()
    }
    composable(Destination.ExerciseDetailsConfigurator.route) { backStackEntry ->
        ExerciseDetailsConfiguratorScreen()
    }
    composable(Destination.Settings.route) {
        SettingsScreen()
    }
    composable(Destination.SearchWorkoutsScreen.route) { backStackEntry ->
        SearchWorkoutsScreen()
    }
    composable(Destination.SearchExercisesScreen.route) { backStackEntry ->
        SearchExercisesScreen()
    }
}