package com.koleff.kare_android.common.navigation

import android.annotation.SuppressLint
import android.os.Build
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
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.DoWorkoutScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsConfiguratorScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreen
import com.koleff.kare_android.ui.compose.screen.LoginScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.RegisterScreen
import com.koleff.kare_android.ui.compose.screen.SearchExercisesScreen
import com.koleff.kare_android.ui.compose.screen.SearchWorkoutsScreen
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.WelcomeScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn

@SuppressLint("RestrictedApi")
@FlowPreview
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun AppNavigation(
    navigationNotifier: NavigationNotifier,
    hasSignedIn: Boolean = false
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
                    is NavigationEvent.NavigateTo -> {

                        //Trying to navigate to the same screen
                        if (navigationEvent.route != navController.currentBackStackEntry?.destination?.route) {
                            navController.navigate(navigationEvent.route)
                        }
                    }

                    is NavigationEvent.ClearBackstackAndNavigateTo -> navController.navigate(
                        navigationEvent.route
                    ) {
                        popUpTo(navController.graph.id)
                    }

                    is NavigationEvent.PopUpToAndNavigateTo -> {
                        navController.navigate(navigationEvent.destinationRoute) {
                            popUpTo(navigationEvent.popUpToRoute) {
                                this.inclusive = navigationEvent.inclusive
                                this.saveState = navigationEvent.saveState
                            }

                            launchSingleTop = true
                        }
                    }

                    NavigationEvent.NavigateBack -> {
                        if (navController.currentBackStack.value.size == 2) return@collectLatest //Don't pop up starting location
                        navController.popBackStack()
                    }
                }

                val navigationBackstack = navController.currentBackStack.value
                Log.d("AppNavigation", "----------------------------\n")
                Log.d("AppNavigation", "Backstack: \n")
                navigationBackstack.forEach { navigationBackstackEntry ->
                    Log.d("AppNavigation", "$navigationBackstackEntry\n")
                }
            }
    }

    //No cached data -> go to welcome screen (first time launch).
    //Cached data -> go to dashboard screen (already signed in).
    Log.d("AppNavigation", "Has credentials -> $hasSignedIn")
    val startingDestination = if(hasSignedIn) {
        Destination.Dashboard.route
    }else{
        Destination.Welcome.route
    }

    NavHost(
        navController = navController,
        startDestination = startingDestination
    ) {
        addDestinations()
    }
}

private fun NavGraphBuilder.addDestinations() {
    addWelcomeGraph()
    composable(Destination.Dashboard.ROUTE) { backStackEntry ->
        DashboardScreen()
    }
    composable(Destination.Workouts.ROUTE) {
        WorkoutsScreen()
    }
    composable(Destination.MuscleGroupExercisesList.ROUTE) { backStackEntry ->
        MuscleGroupScreen()
    }
    composable(Destination.WorkoutDetails.ROUTE) { backStackEntry ->
        WorkoutDetailsScreen()
    }
    composable(Destination.ExerciseDetails.ROUTE) { backStackEntry ->
        ExerciseDetailsScreen()
    }
    composable(Destination.ExerciseDetailsConfigurator.ROUTE) { backStackEntry ->
        ExerciseDetailsConfiguratorScreen()
    }
    composable(Destination.Settings.ROUTE) {
        SettingsScreen()
    }
    composable(Destination.SearchWorkoutsScreen.ROUTE) { backStackEntry ->
        SearchWorkoutsScreen()
    }
    composable(Destination.SearchExercisesScreen.ROUTE) { backStackEntry ->
        SearchExercisesScreen()
    }
    composable(Destination.DoWorkoutScreen.ROUTE) { backStackEntry ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DoWorkoutScreen()
        }
    }
}

internal fun NavGraphBuilder.addWelcomeGraph() {
    composable(Destination.Welcome.ROUTE) { backStackEntry ->
        WelcomeScreen()
    }
    composable(Destination.Login.ROUTE) {
        LoginScreen()
    }
    composable(Destination.Register.ROUTE) { backStackEntry ->
        RegisterScreen()
    }
}