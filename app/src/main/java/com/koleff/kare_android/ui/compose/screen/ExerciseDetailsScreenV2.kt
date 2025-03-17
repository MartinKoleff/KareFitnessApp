package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.AddToWorkoutButton
import com.koleff.kare_android.ui.compose.components.DescriptionBox
import com.koleff.kare_android.ui.compose.components.DifficultyInfo
import com.koleff.kare_android.ui.compose.components.DurationInfo
import com.koleff.kare_android.ui.compose.components.EquipmentNeededInfo
import com.koleff.kare_android.ui.compose.components.FooterButton
import com.koleff.kare_android.ui.compose.components.MuscleWorkedInfo
import com.koleff.kare_android.ui.compose.components.YoutubeVideoPlayer
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.LoadingDialog
import com.koleff.kare_android.ui.state.ExerciseDetailsState
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import com.koleff.kare_android.ui.view_model.ExerciseDetailsViewModel

@Composable
fun ExerciseDetailsScreenV2(
    exerciseDetailsViewModel: ExerciseDetailsViewModel = hiltViewModel()
) {
    val exerciseDetailsState by exerciseDetailsViewModel.state.collectAsState()

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        exerciseDetailsViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(exerciseDetailsState) {
        showErrorDialog =
            exerciseDetailsState.isError
        error = exerciseDetailsState.error

        Log.d("ExerciseDetailsScreen", "Error detected -> $showErrorDialog")

        showLoadingDialog = exerciseDetailsState.isLoading
        Log.d("ExerciseDetailsScreen", "Loading -> $showLoadingDialog")

    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    val subtitleTextColor = LocalExtendedColors.current.subtitle
    val subtitleTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = subtitleTextColor
    )

    val tintColor = MaterialTheme.colorScheme.onSurface

    MainScreenScaffold(
        screenTitle = exerciseDetailsState.exercise.name,
        onNavigateToDashboard = { exerciseDetailsViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { exerciseDetailsViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { exerciseDetailsViewModel.onNavigateBack() },
        onNavigateToSettings = { exerciseDetailsViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            if (exerciseDetailsState.isLoading) {
                LoadingDialog(onDismiss = {
                    showLoadingDialog = false
                })
            }

            YoutubeVideoPlayer(
                youtubeVideoId = exerciseDetailsState.exercise.videoUrl,
                lifecycleOwner = LocalLifecycleOwner.current
            )
            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                DifficultyInfo(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                )
                DurationInfo(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                )
                EquipmentNeededInfo(
                    modifier = Modifier
                        .padding(horizontal = 1.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            DescriptionBox(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                description = exerciseDetailsState.exercise.description
            )
            Spacer(modifier = Modifier.height(12.dp))

            Column {
                Text(
                    modifier = Modifier.padding(horizontal = 8.dp),
                    text = "Muscles worked",
                    style = subtitleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    MuscleWorkedInfo(
                        modifier = Modifier
                            .padding(horizontal = 2.dp),
                        percentage = 33f,
                        exercise = exerciseDetailsState.exercise,
                        label = "Primary muscle"
                    )

                    MuscleWorkedInfo(
                        modifier = Modifier
                            .padding(horizontal = 2.dp),
                        percentage = 75f,
                        exercise = exerciseDetailsState.exercise,
                        label = "Secondary muscle"
                    )

                    MuscleWorkedInfo(
                        modifier = Modifier
                            .padding(horizontal = 2.dp),
                        percentage = 75f,
                        exercise = exerciseDetailsState.exercise,
                        label = "Secondary muscle"
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                AddToWorkoutButton(modifier = Modifier.align(Alignment.BottomCenter)) {
                    exerciseDetailsViewModel.navigateToSearchWorkout()
                }
            }
        }
    }
}