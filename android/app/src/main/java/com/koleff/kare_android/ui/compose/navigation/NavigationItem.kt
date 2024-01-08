package com.koleff.kare_android.ui.compose.navigation

import android.util.Log
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.data.model.dto.NavigationArguments
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationItem(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    screen: MainScreen?, //When screen is null -> navigate to latest backstack entry after popping the details screen
    icon: Any, //Can be Painter or ImageVector
    label: String,
    isBlocked: MutableState<Boolean>,
    tint: Color? = null, //Color.Black
    navigationArguments: NavigationArguments = NavigationArguments(),
    onCustomClickAction: () -> Unit = {}
) {
    LaunchedEffect(key1 = isBlocked.value) {
        Log.d(
            "Navigation LaunchedEffect",
            "Is navigation in progress: ${isBlocked.value}"
        )

        if (isBlocked.value) {
            blockNavigationButtons(isBlocked)
        }
    }

    IconButton(
        modifier = modifier,
        onClick = {
            screen?.let {
                Log.d(
                    "Navigation",
                    "Is navigation in progress: ${isBlocked.value}"
                )

                if (navController.currentBackStackEntry!!.destination.route == screen.route || isBlocked.value) return@IconButton

                onCustomClickAction.invoke()

                when (screen) {
                    MainScreen.SearchExercisesScreen -> {
                        navController.navigate(MainScreen.SearchExercisesScreen.createRoute(workoutId = navigationArguments.workoutId))
                    }
                    MainScreen.SearchWorkoutsScreen -> {
                        navController.navigate(MainScreen.SearchWorkoutsScreen.createRoute(exerciseId = navigationArguments.exerciseId))
                    }
                    else -> {

                        //Default screen -> no custom routing...
                        navController.navigate(screen.route)
                    }
                }.also {
                    Log.d(
                        "Navigation",
                        "Updated isBlocked to true"
                    )

                    isBlocked.value = true
                }
                return@IconButton
            } ?: run {
                Log.d(
                    "Navigation",
                    "Backstack before pop: ${navController.currentBackStackEntry}, Size: ${navController.currentBackStack.value.size}"
                )

                //Starting navigation and current navigation
                if (navController.currentBackStack.value.size == 2) return@IconButton

                navController.popBackStack()

                Log.d(
                    "Navigation",
                    "Backstack after pop: ${navController.currentBackStackEntry}, Size: ${navController.currentBackStack.value.size}"
                )
            }
        }
    ) {
        when (icon) {
            is ImageVector -> {
                tint?.let {
                    Icon(imageVector = icon, contentDescription = label, tint = tint)
                    return@IconButton
                }
                Icon(imageVector = icon, contentDescription = label)
            }

            is Painter -> {
                tint?.let {
                    Icon(icon, contentDescription = label, tint = tint)
                    return@IconButton
                }
                Icon(icon, contentDescription = label)
            }

            else -> return@IconButton
        }
    }
}

@Composable
fun FloatingNavigationItem(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    screen: MainScreen?, //When screen is null -> navigate to latest backstack entry after popping the details screen
    icon: Any, //Can be Painter or ImageVector
    label: String,
    isBlocked: MutableState<Boolean>,
    tint: Color? = null, //Color.Black
    onCustomClickAction: () -> Unit = {}
) {
    LaunchedEffect(key1 = isBlocked.value) {
        Log.d(
            "Navigation LaunchedEffect",
            "Is navigation in progress: ${isBlocked.value}"
        )

        if (isBlocked.value) {
            blockNavigationButtons(isBlocked)
        }
    }

    FloatingActionButton(
        modifier = modifier,
        onClick = {
            screen?.let {
                Log.d(
                    "Navigation",
                    "Is navigation in progress: ${isBlocked.value}"
                )

                if (navController.currentBackStackEntry!!.destination.route == screen.route || isBlocked.value) return@FloatingActionButton

                onCustomClickAction.invoke()

                navController.navigate(screen.route).also {
                    Log.d(
                        "Navigation",
                        "Updated isBlocked to true"
                    )

                    isBlocked.value = true
                }
                return@FloatingActionButton
            } ?: run {
                Log.d(
                    "Navigation",
                    "Backstack before pop: ${navController.currentBackStackEntry}, Size: ${navController.currentBackStack.value.size}"
                )

                //Starting navigation and current navigation
                if (navController.currentBackStack.value.size == 2) return@FloatingActionButton

                navController.popBackStack()

                Log.d(
                    "Navigation",
                    "Backstack after pop: ${navController.currentBackStackEntry}, Size: ${navController.currentBackStack.value.size}"
                )
            }
        }
    ) {
        when (icon) {
            is ImageVector -> {
                tint?.let {
                    Icon(imageVector = icon, contentDescription = label, tint = tint)
                    return@FloatingActionButton
                }
                Icon(imageVector = icon, contentDescription = label)
            }

            is Painter -> {
                tint?.let {
                    Icon(icon, contentDescription = label, tint = tint)
                    return@FloatingActionButton
                }
                Icon(icon, contentDescription = label)
            }

            else -> return@FloatingActionButton
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun blockNavigationButtons(isBlocked: MutableState<Boolean>) {
    GlobalScope.launch {
        delay(750)
        Log.d(
            "Navigation",
            "Updated isBlocked to false"
        )
        isBlocked.value = false
    }
}
