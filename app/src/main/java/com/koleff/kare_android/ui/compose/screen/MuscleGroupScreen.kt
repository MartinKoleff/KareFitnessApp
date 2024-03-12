package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.MachineFilterSegmentButton
import com.koleff.kare_android.ui.compose.banners.ExerciseList
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.view_model.ExerciseListViewModel

@Composable
fun MuscleGroupScreen(
    exerciseListViewModel: ExerciseListViewModel = hiltViewModel()
) {

    //Navigation Callbacks
    val onNavigateToDashboard = {
        exerciseListViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }
    val onNavigateToWorkouts = {
        exerciseListViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }
    val onNavigateToSettings = {
        exerciseListViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { exerciseListViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    val exerciseListState by exerciseListViewModel.state.collectAsState()
    val muscleGroup = exerciseListViewModel.muscleGroup

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        exerciseListViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(exerciseListState.isError){

        error = if (exerciseListState.isError) {
            exerciseListState.error
        } else {
            null
        }

        showErrorDialog =
            exerciseListState.isError

        Log.d("MuscleGroupScreen", "Error detected -> $showErrorDialog")
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(error!!, onErrorDialogDismiss)
        }
    }

    MainScreenScaffold(
        muscleGroup.name,
        onNavigateToDashboard = onNavigateToDashboard,
        onNavigateToWorkouts = onNavigateToWorkouts,
        onNavigateBackAction = onNavigateBack,
        onNavigateToSettings = onNavigateToSettings
    ) { innerPadding ->
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    start = 4.dp + innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = 4.dp + innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = 0.dp
                )
            )

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Filter buttons
            MachineFilterSegmentButton(
                modifier = buttonModifier,
                selectedOptionIndex = -1,
                isDisabled = exerciseListState.isLoading,
                exerciseListViewModel = exerciseListViewModel
            )

            if (exerciseListState.isLoading) {
                LoadingWheel(
                    innerPadding = innerPadding
                )
            } else {
                ExerciseList(
                    innerPadding = innerPadding,
                    exerciseList = exerciseListState.exerciseList,
                    navigateToExerciseDetails = { exercise ->
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