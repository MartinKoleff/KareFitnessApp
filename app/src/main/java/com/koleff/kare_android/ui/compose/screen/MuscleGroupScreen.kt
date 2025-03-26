package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.ExerciseList
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.MachineFilterSegmentButton
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel

@Composable
fun MuscleGroupScreen(
    exerciseListViewModel: ExerciseListViewModel = hiltViewModel()
) {
    val exerciseListState by exerciseListViewModel.state.collectAsState()
    val muscleGroup = exerciseListViewModel.muscleGroup

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        exerciseListViewModel.clearError()
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(exerciseListState) {
        showErrorDialog =
            exerciseListState.isError
        error = exerciseListState.error

        Log.d("MuscleGroupScreen", "Error detected -> $showErrorDialog")
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    MainScreenScaffold(
        muscleGroup.muscleGroupName,
        onNavigateToDashboard = { exerciseListViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { exerciseListViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { exerciseListViewModel.onNavigateBack() },
        onNavigateToSettings = { exerciseListViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            //Filter buttons
            MachineFilterSegmentButton(
                modifier = Modifier.padding(horizontal = 2.dp),
                selectedOptionIndex = -1,
                isDisabled = exerciseListState.isLoading,
                onFilterSelected = exerciseListViewModel::OnFilterExerciseEvent
            )

            if (exerciseListState.isLoading) {
                LoadingWheel(
                    innerPadding = innerPadding
                )
            } else {
                ExerciseList(
                    exerciseList = exerciseListState.exerciseList,
                    onClick = { exercise ->
                        exerciseListViewModel.navigateToExerciseDetails(
                            exerciseId = exercise.exerciseId,
                            muscleGroupId = exercise.muscleGroup.muscleGroupId
                        )
                    }
                )
            }
        }
    }
}