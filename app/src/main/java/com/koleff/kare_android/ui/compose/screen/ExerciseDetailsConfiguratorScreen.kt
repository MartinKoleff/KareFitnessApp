package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.AddNewSetFooter
import com.koleff.kare_android.ui.compose.components.ExerciseSetRow
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.RestBetweenSetsFooter
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.ExerciseDetailsConfiguratorScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.event.OnExerciseUpdateEvent
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.theme.LocalExtendedColorScheme
import com.koleff.kare_android.ui.view_model.ExerciseDetailsConfiguratorViewModel

@Composable
fun ExerciseDetailsConfiguratorScreen(
    exerciseDetailsConfiguratorViewModel: ExerciseDetailsConfiguratorViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val exerciseState by exerciseDetailsConfiguratorViewModel.exerciseState.collectAsState()
    val selectedWorkoutState by exerciseDetailsConfiguratorViewModel.selectedWorkoutState.collectAsState()
    val updateWorkoutState by exerciseDetailsConfiguratorViewModel.updateWorkoutState.collectAsState()

    Log.d("ExerciseDetailsConfiguratorScreen", exerciseState.exercise.muscleGroup.toString())
    val initialMuscleGroup = exerciseDetailsConfiguratorViewModel.initialMuscleGroup
    val exerciseImageId = MuscleGroup.getImage(initialMuscleGroup)

    LaunchedEffect(updateWorkoutState) {

        //Await update workout
        if (updateWorkoutState.isSuccessful) {
            exerciseDetailsConfiguratorViewModel.navigateToWorkoutDetails(selectedWorkoutState.workoutDetails.workoutId)
        }
    }

    //Navigation Callbacks
    val onNavigateBack: () -> Unit = {
        exerciseDetailsConfiguratorViewModel.onNavigationEvent(
            NavigationEvent.NavigateBack
        )
    }

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onSubmitExercise: () -> Unit = {  //TODO: wire with dialog on changes done
        if (!exerciseState.isLoading) {

            //Replace old exercise with new
            exerciseDetailsConfiguratorViewModel.onExerciseUpdateEvent(
                OnExerciseUpdateEvent.OnExerciseSubmit(exerciseState.exercise)
            )
        }
    }

    val onDeleteSet: (ExerciseSetDto) -> Unit = { selectedExerciseSet ->
        exerciseDetailsConfiguratorViewModel.deleteSet(selectedExerciseSet)
    }

    val onAddNewSetAction: () -> Unit = {
        exerciseDetailsConfiguratorViewModel.addNewSet()
    }

    //Switch state
    var restBetweenSetsIsChecked by remember {
        mutableStateOf(false)
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }

    val onErrorDialogDismiss = {
        showErrorDialog = false
        exerciseDetailsConfiguratorViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }
    LaunchedEffect(
        exerciseState,
        selectedWorkoutState,
        updateWorkoutState
    ) {
        val states = listOf(
            exerciseState,
            selectedWorkoutState,
            updateWorkoutState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("ExerciseDetailsConfiguratorScreen", "Error detected -> $showErrorDialog")

        val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    val cornerSize = 24.dp
    val loadingWheelSize = 50.dp
    val titleTextColor = MaterialTheme.colorScheme.onSurface
    val backgroundColor = MaterialTheme.colorScheme.surface
    val outlineColor = MaterialTheme.colorScheme.outline

    val titleTextStyle = MaterialTheme.typography.headlineSmall.copy(
        color = titleTextColor
    )

    ExerciseDetailsConfiguratorScaffold(
        screenTitle = exerciseState.exercise.name,
        exerciseImageId = exerciseImageId,
        onSubmitExercise = onSubmitExercise,
        onNavigateBackAction = onNavigateBack
    ) { innerPadding ->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .pointerInput(Unit) {

                //Hide keyboard on tap outside SearchBar
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            }) {

            //Exercise name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                    .height(50.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = exerciseState.exercise.name,
                    style = titleTextStyle,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            //Exercise sets
            LazyColumn(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 12.dp)
                    .clip(RoundedCornerShape(cornerSize))
                    .background(
                        color = backgroundColor,
                        shape = RoundedCornerShape(cornerSize)
                    )
                    .border(
                        border = BorderStroke(2.dp, color = outlineColor),
                        shape = RoundedCornerShape(cornerSize)
                    ),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Rows with sets / reps / weight configuration
                items(exerciseState.exercise.sets.size) { currentSetId ->
                    val currentSet = exerciseState.exercise.sets[currentSetId]
                    ExerciseSetRow(
                        set = currentSet,
                        onRepsChanged = { newReps ->
                            currentSet.reps = newReps
                        },
                        onWeightChanged = { newWeight ->
                            currentSet.weight = newWeight
                        },
                        onDelete = {
                            onDeleteSet(currentSet)
                        }
                    )
                }

                //Hide during initial loading
                if (!exerciseState.isLoading) {

                    //Add new set footer
                    item {
                        AddNewSetFooter(onAddNewSetAction = onAddNewSetAction)
                    }

                    //Rest after exercise footer
                    item {
                        RestBetweenSetsFooter(
                            isChecked = restBetweenSetsIsChecked,
                            onCheckedChange = { newState ->
                                restBetweenSetsIsChecked = newState
                            }
                        )
                    }
                }
            }
        }
    }

    if (showLoadingDialog) {
        LoadingWheel(
            innerPadding = PaddingValues(bottom = loadingWheelSize - 10.dp)
        )
    }
}
