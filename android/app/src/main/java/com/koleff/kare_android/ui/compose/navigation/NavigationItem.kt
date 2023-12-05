package com.koleff.kare_android.ui.compose.navigation

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.MainScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: MainScreen?, //When screen is null -> navigate to latest backstack entry after popping the details screen
    icon: Any, //Can be Painter or ImageVector
    label: String,
    isBlocked: MutableState<Boolean>
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
        onClick = {
            screen?.let {
                Log.d(
                    "Navigation",
                    "Is navigation in progress: ${isBlocked.value}"
                )

                if (navController.currentBackStackEntry!!.destination.route == screen.route || isBlocked.value) return@IconButton

                navController.navigate(screen.route).also {
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
            is ImageVector -> Icon(imageVector = icon, contentDescription = label)
            is Painter -> Icon(icon, contentDescription = label)
            else -> return@IconButton
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
