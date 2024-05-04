package com.koleff.kare_android.ui.compose.components.navigation_components.bottom_navigation_bar

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.compose.components.navigation_components.NavigationItem


@Composable
fun ExerciseDetailsBottomNavigationBar(
    exerciseId: Int,
    onNavigateSubmitExercise: () -> Unit
) {
    val isBlocked = mutableStateOf(exerciseId == -1)
    val submitExerciseAction = if (isBlocked.value) {
        {}
    } else {
        onNavigateSubmitExercise
    }

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = 5.dp,
        actions = {
            Spacer(Modifier.weight(1f))

            NavigationItem(
                icon = painterResource(id = R.drawable.ic_vector_add),
                label = "Add to workout",
                tint = Color.White,
                onNavigateAction = submitExerciseAction
            )

            Spacer(Modifier.weight(1f))
        }
    )
}

@Preview
@Composable
fun PreviewExerciseDetailsBottomNavigationBar() {
    ExerciseDetailsBottomNavigationBar(
        exerciseId = -1,
        onNavigateSubmitExercise = {}
    )
}