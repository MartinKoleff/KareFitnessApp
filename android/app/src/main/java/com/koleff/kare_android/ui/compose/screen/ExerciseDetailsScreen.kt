package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.ui.compose.DetailsScreenScaffold
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.MainScreenScaffold
import com.koleff.kare_android.ui.compose.YoutubeVideoPlayer
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel_Factory

@Composable
fun ExerciseDetailsScreen(
    navController: NavHostController,
    exerciseId: Int = -1, //Invalid exercise selected...
    isNavigationInProgress: MutableState<Boolean>,
    exerciseDetailsViewModelFactory: ExerciseDetailsViewModel.Factory
) {
    val exerciseDetailsViewModel = viewModel<ExerciseDetailsViewModel>(
        factory = ExerciseDetailsViewModel.provideExerciseDetailsViewModelFactory(
            factory = exerciseDetailsViewModelFactory,
            exerciseId = exerciseId
        )
    )

    val exerciseDetailsState by exerciseDetailsViewModel.state.collectAsState()
    val exerciseName = exerciseDetailsState.exercise?.name ?: "Loading..."
    val exerciseDescription = exerciseDetailsState.exercise?.description ?: "Description"
    val exerciseVideoUrl = exerciseDetailsState.exercise?.videoUrl ?: "" //TODO: split after v=

    DetailsScreenScaffold(exerciseName, navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ) {
            if (exerciseDetailsState.isLoading) {
                LoadingWheel(
                    innerPadding = innerPadding
                )
            } else {
                YoutubeVideoPlayer(
                    youtubeVideoId = "dQw4w9WgXcQ",
                    lifecycleOwner = LocalLifecycleOwner.current
                )

                Text(exerciseDescription)
            }
        }
    }
}

@Preview
@Composable
fun ExerciseDetailsScreenPreview() {
    val navController = rememberNavController()

    DetailsScreenScaffold("TEST", navController, mutableStateOf(false)) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()

        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ) {
            YoutubeVideoPlayer(
                youtubeVideoId = "dQw4w9WgXcQ",
                lifecycleOwner = LocalLifecycleOwner.current
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(50.dp))

            Text("Description")
        }
    }
}