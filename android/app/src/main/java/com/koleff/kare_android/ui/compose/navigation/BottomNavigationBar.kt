package com.koleff.kare_android.ui.compose.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.data.MainScreen

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
                screen = MainScreen.MyWorkout,
                icon = painterResource(id = R.drawable.ic_vector_my_workout),
                label = "Workout screen",
                isBlocked = isNavigationInProgress
            )
        }
    )
}

@Composable
fun ExerciseDetailsBottomNavigationBar(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = 5.dp,
        actions = {
            Spacer(Modifier.weight(1f))

            FloatingNavigationItem(
                navController = navController,
                screen = MainScreen.SearchWorkoutsScreen,
                icon = painterResource(id = R.drawable.ic_vector_add),
                label = "Add to workout",
                isBlocked = isNavigationInProgress,
                tint = Color.White
            )

            Spacer(Modifier.weight(1f))
        }
    )
}

@Preview
@Composable
fun PreviewExerciseDetailsBottomNavigationBar() {
    val navController = rememberNavController()
    ExerciseDetailsBottomNavigationBar(
        navController = navController,
        isNavigationInProgress = mutableStateOf(false)
    )
}