package com.koleff.kare_android.ui.compose

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.MainScreen

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: MainScreen?, //When screen is null -> navigate to latest backstack entry after popping the details screen
    icon: Any, //Can be Painter or ImageVector
    label: String
) {
    IconButton(
        onClick = {
            screen?.let {
                if(navController.currentBackStackEntry!!.destination.route == screen.route) return@IconButton

                navController.navigate(screen.route)

                Log.d(
                    "Navigation",
                    "Backstack: ${navController.currentBackStackEntry}, ${navController.currentBackStack.value.size}"
                )

                return@IconButton
            } ?: run {
                Log.d(
                    "Navigation",
                    "Backstack before pop: ${navController.currentBackStackEntry}, Size: ${navController.currentBackStack.value.size}"
                )

                //Starting navigation and current navigation
                if(navController.currentBackStack.value.size == 2) return@IconButton

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
