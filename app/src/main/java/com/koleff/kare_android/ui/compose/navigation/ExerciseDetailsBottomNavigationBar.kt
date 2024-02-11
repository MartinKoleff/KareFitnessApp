package com.koleff.kare_android.ui.compose.navigation

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
import com.koleff.kare_android.ui.compose.NavigationIconButton


@Composable
fun ExerciseDetailsBottomNavigationBar(
    exerciseId: Int,
    onNavigateSubmitExercise: () -> Unit
) {
    val isBlocked = mutableStateOf(exerciseId == -1)

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = 5.dp,
        actions = {
            Spacer(Modifier.weight(1f))

            NavigationIconButton(
                icon = painterResource(id = R.drawable.ic_vector_add),
                label = "Add to workout",
                tint = Color.White,
                onNavigateAction = onNavigateSubmitExercise
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
        exerciseId = -1,
        onNavigateSubmitExercise = {}
    )
}