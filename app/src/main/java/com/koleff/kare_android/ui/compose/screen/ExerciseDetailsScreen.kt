package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.YoutubeVideoPlayer
import com.koleff.kare_android.ui.compose.scaffolds.ExerciseDetailsScreenScaffold
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel

@Composable
fun ExerciseDetailsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseDetailsViewModel: ExerciseDetailsViewModel
) {
    val exerciseDetailsState by exerciseDetailsViewModel.state.collectAsState()

    Log.d("ExerciseDetailsScreen", exerciseDetailsState.exercise.muscleGroup.toString())
    val exerciseImageId = MuscleGroup.getImage(exerciseDetailsState.exercise.muscleGroup)

    ExerciseDetailsScreenScaffold(
        screenTitle = exerciseDetailsState.exercise.name,
        navController = navController,
        isNavigationInProgress = isNavigationInProgress,
        exerciseImageId = exerciseImageId,
        exerciseId = exerciseDetailsState.exercise.id
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
            exerciseDetailsState = exerciseDetailsState,
        )
    }
}

@Composable
fun ExerciseDetailsContent(
    modifier: Modifier,
    exerciseDetailsState: ExerciseDetailsState,
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (exerciseDetailsState.isLoading) {
            item {
                LoadingWheel()
            }
        } else {
            item { //TODO: fix in place...
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
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            item {
                YoutubeVideoPlayer(
                    youtubeVideoId = exerciseDetailsState.exercise.videoUrl,
                    lifecycleOwner = LocalLifecycleOwner.current
                )
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = "Description:",
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = exerciseDetailsState.exercise.description,
                        style = TextStyle(
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        textAlign = TextAlign.Start,
                        maxLines = 5,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(horizontal = 8.dp))
            }

            //Rows with cool emojies...
            item {

            }
        }
    }
}

@Preview
@Composable
fun ExerciseDetailsScreenPreview() {
    val navController = rememberNavController()
    val isNavigationInProgress = mutableStateOf(false)
    val exerciseDetailsState = ExerciseDetailsState(
        exercise = ExerciseDetailsDto(
            id = 1,
            name = "Bulgarian split squad",
            description = "Description: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies. Quisque suscipit, purus ut congue porta, eros eros tincidunt sem, sed commodo magna metus eu nibh. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum quis velit eget eros malesuada luctus. Suspendisse iaculis ullamcorper condimentum. Sed metus augue, dapibus eu venenatis vitae, ornare non turpis. Donec suscipit iaculis dolor, id fermentum mauris interdum in. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.",
            muscleGroup = MuscleGroup.LEGS,
            machineType = MachineType.DUMBBELL,
            videoUrl = "dQw4w9WgXcQ" //https://www.youtube.com/watch?v=
        ),
        isSuccessful = true
    )
    val exerciseImageId = MuscleGroup.getImage(exerciseDetailsState.exercise.muscleGroup)

    ExerciseDetailsScreenScaffold(
        screenTitle = exerciseDetailsState.exercise.name,
        navController = navController,
        isNavigationInProgress = isNavigationInProgress,
        exerciseImageId = exerciseImageId,
        exerciseId = exerciseDetailsState.exercise.id
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
            exerciseDetailsState = exerciseDetailsState
        )
    }
}
