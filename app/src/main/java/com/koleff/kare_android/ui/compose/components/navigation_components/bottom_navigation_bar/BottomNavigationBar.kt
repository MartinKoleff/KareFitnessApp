package com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem

@Composable
fun BottomNavigationBar(
    onNavigateToDashboard: () -> Unit,
    onNavigateToWorkouts: () -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.primary

    BottomAppBar(
        modifier = Modifier.background(backgroundColor),
        actions = {
            NavigationItem(
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

            NavigationItem(
                icon = painterResource(id = R.drawable.ic_vector_my_workout),
                label = "Workout screen",
                onNavigateAction = onNavigateToWorkouts
            )
        }
    )
}

@Preview
@PreviewLightDark
@Composable
fun PreviewBottomNavigationBar() {
    BottomNavigationBar(
        onNavigateToDashboard = {},
        onNavigateToWorkouts = {}
    )
}
