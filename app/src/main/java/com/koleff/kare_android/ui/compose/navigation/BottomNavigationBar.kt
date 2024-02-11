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
import com.koleff.kare_android.ui.compose.NavigationIconButton

@Composable
fun BottomNavigationBar(
    onNavigateToDashboard: () -> Unit,
    onNavigateToWorkouts: () -> Unit,
) {
    BottomAppBar(
        actions = {
            NavigationIconButton(
                icon = Icons.Filled.Star,
                label = "Dashboard",
                onNavigateAction = onNavigateToDashboard
            )

            //Spacing between items
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .width(0.dp)
            )

            NavigationIconButton(
                icon = painterResource(id = R.drawable.ic_vector_my_workout),
                label = "Workout screen",
                onNavigateAction = onNavigateToWorkouts
            )
        }
    )
}

@Preview
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(
        onNavigateToDashboard = {},
        onNavigateToWorkouts = {}
    )
}
