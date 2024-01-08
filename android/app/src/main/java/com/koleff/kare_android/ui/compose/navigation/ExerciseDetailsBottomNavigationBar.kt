package com.koleff.kare_android.ui.compose.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.data.model.dto.NavigationArguments


@Composable
fun ExerciseDetailsBottomNavigationBar(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseId: Int
) {
    val isBlocked = mutableStateOf(isNavigationInProgress.value || exerciseId == -1)

    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.secondary,
        tonalElevation = 5.dp,
        actions = {
            Spacer(Modifier.weight(1f))

            NavigationItem(
                navController = navController,
                screen = MainScreen.SearchWorkoutsScreen,
                icon = painterResource(id = R.drawable.ic_vector_add),
                label = "Add to workout",
                isBlocked = isBlocked,
                tint = Color.White,
                navigationArguments = NavigationArguments(exerciseId = exerciseId)
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
        isNavigationInProgress = mutableStateOf(false),
        exerciseId = -1
    )
}