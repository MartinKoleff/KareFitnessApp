package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.R
import com.koleff.kare_android.data.MainScreen
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.state.ExerciseDetailsState
import com.koleff.kare_android.ui.compose.DetailsScreenScaffold
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.YoutubeVideoPlayer
import com.koleff.kare_android.ui.compose.navigation.FloatingNavigationItem
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel

@Composable
fun ExerciseDetailsScreen(
    navController: NavHostController,
    exerciseId: Int = -1, //Invalid exercise selected...
    initialMuscleGroupId: Int = -1, //Invalid muscle group selected...
    isNavigationInProgress: MutableState<Boolean>,
    exerciseDetailsViewModelFactory: ExerciseDetailsViewModel.Factory
) {
    val initialMuscleGroup = MuscleGroup.fromId(initialMuscleGroupId)

    val exerciseDetailsViewModel = viewModel<ExerciseDetailsViewModel>(
        factory = ExerciseDetailsViewModel.provideExerciseDetailsViewModelFactory(
            factory = exerciseDetailsViewModelFactory,
            exerciseId = exerciseId,
            initialMuscleGroup = initialMuscleGroup
        )
    )

    val exerciseDetailsState by remember(exerciseDetailsViewModel) {
        mutableStateOf(exerciseDetailsViewModel.state)
    }

    Log.d("ExerciseDetailsScreen", exerciseDetailsState.value.exercise.muscleGroup.toString())
    val exerciseImageId = MuscleGroup.getImage(exerciseDetailsState.value.exercise.muscleGroup)

    DetailsScreenScaffold(
        screenTitle = exerciseDetailsState.value.exercise.name,
        navController = navController,
        isNavigationInProgress = isNavigationInProgress,
        exerciseImageId = exerciseImageId
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.primary,
                        MaterialTheme.colorScheme.secondary
                    )
                )
            )

        ExerciseDetailsContent(
            modifier = modifier,
            innerPadding = innerPadding,
            exerciseDetailsState = exerciseDetailsState.value,
            navController = navController,
            isNavigationInProgress = isNavigationInProgress
        )
    }
}

@Composable
fun ExerciseDetailsContent(
    modifier: Modifier,
    innerPadding: PaddingValues,
    exerciseDetailsState: ExerciseDetailsState,
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    ) {
    Box(modifier = modifier) {

        //All content without add to workout button
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (exerciseDetailsState.isLoading) {
                LoadingWheel(
                    innerPadding = innerPadding
                )
            } else {
                Text(
                    modifier = Modifier.padding(
                        PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 16.dp,
                            bottom = 8.dp
                        )
                    ),
                    text = exerciseDetailsState.exercise.name,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                YoutubeVideoPlayer(
                    youtubeVideoId = exerciseDetailsState.exercise.videoUrl,
                    lifecycleOwner = LocalLifecycleOwner.current
                )

                Text(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.Start),
                    text = exerciseDetailsState.exercise.description,
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        //Add to workout
        Box(modifier = Modifier.padding(16.dp).align(Alignment.BottomEnd)) {
            FloatingNavigationItem(
                navController = navController,
                screen = MainScreen.SelectWorkout,
                icon = painterResource(id = R.drawable.ic_vector_add_green),
                label = "Add to workout",
                isBlocked = isNavigationInProgress,
                tint = Color.Green
            )
        }
    }
}

@Preview
@Composable
fun ExerciseDetailsScreenPreview() {
    val navController = rememberNavController()

    DetailsScreenScaffold(
        screenTitle = "TEST",
        navController = navController,
        isNavigationInProgress = mutableStateOf(false),
        exerciseImageId = R.drawable.ic_legs
    ) { innerPadding ->
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

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            )

            Text("Description")
        }
    }
}