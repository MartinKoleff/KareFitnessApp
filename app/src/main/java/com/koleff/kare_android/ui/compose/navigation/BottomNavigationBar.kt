package com.koleff.kare_android.ui.compose.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.MainScreen

@Composable
fun BottomNavigationBar(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>
) {
    BottomAppBar(
        actions = {
            NavigationItem(
                navController = navController,
                screen = MainScreen.Dashboard,
                icon = Icons.Filled.Star,
                label = "Dashboard",
                isBlocked = isNavigationInProgress
            )

            //Spacing between items
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .width(0.dp)
            )

            NavigationItem(
                navController = navController,
                screen = MainScreen.Workouts,
                icon = painterResource(id = R.drawable.ic_vector_my_workout),
                label = "Workout screen",
                isBlocked = isNavigationInProgress
            )
        }
    )
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    val navController = rememberNavController()
        BottomNavigationBar(
        navController = navController,
        isNavigationInProgress = mutableStateOf(false)
    )
}
