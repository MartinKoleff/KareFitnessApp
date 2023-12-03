package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.R
import com.koleff.kare_android.data.MainScreen

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomAppBar(
        actions = {
            NavigationItem(
                navController = navController,
                screen = MainScreen.Dashboard,
                icon = Icons.Filled.Star,
                label = "Dashboard"
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
                label = "My Workout"
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
                icon = painterResource(id = R.drawable.ic_vector_list),
                label = "Workouts"
            )
        }
    )
}