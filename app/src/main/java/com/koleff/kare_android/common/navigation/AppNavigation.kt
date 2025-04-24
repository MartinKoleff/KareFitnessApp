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
import com.koleff.kare_android.ui.compose.screen.ChangeLanguageScreen
import com.koleff.kare_android.ui.compose.screen.DashboardScreen
import com.koleff.kare_android.ui.compose.screen.DoWorkoutScreenV2
import com.koleff.kare_android.ui.compose.screen.ExerciseConfiguratorScreen
import com.koleff.kare_android.ui.compose.screen.ExerciseDetailsScreenV2
import com.koleff.kare_android.ui.compose.screen.LoginScreen
import com.koleff.kare_android.ui.compose.screen.MuscleGroupScreen
import com.koleff.kare_android.ui.compose.screen.OnboardingFormScreen
import com.koleff.kare_android.ui.compose.screen.OnboardingScreen
import com.koleff.kare_android.ui.compose.screen.RegisterScreen
import com.koleff.kare_android.ui.compose.screen.SearchExercisesScreenV2
import com.koleff.kare_android.ui.compose.screen.SearchWorkoutsScreenV2
import com.koleff.kare_android.ui.compose.screen.SettingsScreen
import com.koleff.kare_android.ui.compose.screen.StatisticsScreen
import com.koleff.kare_android.ui.compose.screen.WelcomeScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutHistoryScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutDetailsScreenV3
import com.koleff.kare_android.ui.compose.screen.WorkoutHistoryScreen
import com.koleff.kare_android.ui.compose.screen.WorkoutsScreenV2
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
    hasSignedIn: Boolean = false,
    hasOnboarded: Boolean = false
) {
    val navController = rememberNavController()

    //Navigation observer
    LaunchedEffect(navController) {
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

                    is NavigationEvent.NavigateBack -> {
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
    Log.d("AppNavigation", "Has onboarded -> $hasOnboarded")
    val startingDestination = if (hasSignedIn) {
        Destination.Dashboard.route
    } else if (!hasOnboarded) {
        Destination.Onboarding.route
    } else {
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
    addOnboardingGraph()
    composable(Destination.Dashboard.ROUTE) { backStackEntry ->
        DashboardScreen()
    }
    composable(Destination.Workouts.ROUTE) {
        WorkoutsScreenV2()
    }
    composable(Destination.MuscleGroupExercisesList.ROUTE) { backStackEntry ->
        MuscleGroupScreen()
    }
    composable(Destination.WorkoutDetails.ROUTE) { backStackEntry ->
        WorkoutDetailsScreenV3()
    }
    composable(Destination.ExerciseDetails.ROUTE) { backStackEntry ->
        ExerciseDetailsScreenV2()
    }
    composable(Destination.ExerciseDetailsConfigurator.ROUTE) { backStackEntry ->
        ExerciseConfiguratorScreen()
    }
    composable(Destination.Settings.ROUTE) {
        SettingsScreen()
    }
    composable(Destination.SearchWorkoutsScreen.ROUTE) { backStackEntry ->
        SearchWorkoutsScreenV2()
    }
    composable(Destination.SearchExercisesScreen.ROUTE) { backStackEntry ->
        SearchExercisesScreenV2()
    }
    composable(Destination.DoWorkoutScreen.ROUTE) { backStackEntry ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DoWorkoutScreenV2()
        }
    }
    composable(Destination.ChangeLanguage.ROUTE) { backStackEntry ->
        ChangeLanguageScreen()
    }
    composable(Destination.WorkoutHistory.ROUTE) { backStackEntry ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WorkoutHistoryScreen()
        }
    }
    composable(Destination.Statistics.ROUTE) { backStackEntry ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            StatisticsScreen()
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

internal fun NavGraphBuilder.addOnboardingGraph() {
    composable(Destination.Onboarding.ROUTE) { backStackEntry ->
        OnboardingScreen()
    }
    composable(Destination.OnboardingForm.ROUTE) { backStackEntry ->
        OnboardingFormScreen()
    }
}