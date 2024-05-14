package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.SearchExercisesList
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.compose.dialogs.DuplicateExercisesFoundDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.event.OnMultipleExercisesUpdateEvent
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.theme.LocalExtendedColorScheme
import com.koleff.kare_android.ui.view_model.SearchExercisesViewModel

@Composable
fun SearchExercisesScreen(
    searchExercisesViewModel: SearchExercisesViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val exercisesState by searchExercisesViewModel.state.collectAsState()
    val updateWorkoutState by searchExercisesViewModel.updateWorkoutState.collectAsState()
    val duplicateExercisesState by searchExercisesViewModel.duplicateExercisesState.collectAsState()

    val selectedExercises = remember {
        mutableStateListOf<ExerciseDto>()
    }

    val onSubmitExercises: () -> Unit = {  //(List<ExerciseDto>) -> Unit
        searchExercisesViewModel.onMultipleExercisesUpdateEvent(
            OnMultipleExercisesUpdateEvent.OnMultipleExercisesSubmit(selectedExercises)
        )
    }

    val onSelectExercise: (ExerciseDto) -> Unit = { selectedExercise ->
        if (selectedExercises.contains(selectedExercise)) {
            selectedExercises.remove(selectedExercise)
        } else {
            selectedExercises.add(selectedExercise)
        }
    }

    var showLoadingDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showOnDuplicateExercisesFoundDialog by remember { mutableStateOf(false) }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }

    val onErrorDialogDismiss = {
        showErrorDialog = false
        searchExercisesViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    LaunchedEffect(
        exercisesState,
        updateWorkoutState,
        duplicateExercisesState
    ) {
        val states = listOf(
            exercisesState,
            updateWorkoutState,
            duplicateExercisesState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError
        Log.d("SearchExercisesScreen", "Error detected -> $showErrorDialog")

        val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    //TODO: fix not showing dialog and throwing invalid exercise error onSubmit...
    LaunchedEffect(duplicateExercisesState) {
        duplicateExercisesState.isSuccessful || return@LaunchedEffect

        if (duplicateExercisesState.containsDuplicates) {
            showOnDuplicateExercisesFoundDialog = true
        } else {

            //Don't show dialog and directly submit exercises
            onSubmitExercises()
        }
    }

    val onFindDuplicateExercisesDialogDismiss = {
        showOnDuplicateExercisesFoundDialog = false
    }

    if (showOnDuplicateExercisesFoundDialog) {
        DuplicateExercisesFoundDialog(
            onSubmit = onSubmitExercises,
            onDismiss = onFindDuplicateExercisesDialogDismiss
        )
    }

    //Navigation Callbacks
    val onNavigateToSettings = {
        searchExercisesViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack =
        { searchExercisesViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    SearchListScaffold(
        screenTitle = "Select exercise",
        onNavigateToAction = onNavigateToSettings,
        onNavigateBackAction = onNavigateBack
    ) { innerPadding ->
        val modifier = Modifier
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
            }

        if (showLoadingDialog) {
            LoadingWheel()
        } else {
            Column(modifier = modifier) {

                //Search bar
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onSearch = { text ->
                        searchExercisesViewModel.onTextChange(searchText = text)
                    },
                    onToggleSearch = {
                        searchExercisesViewModel.onToggleSearch()
                    })

                SearchExercisesList(
                    modifier = Modifier
                        .fillMaxSize(),
                    exerciseList = exercisesState.exerciseList,
                    onSelectExercise = onSelectExercise
                )
            }

            //Footer
            if (selectedExercises.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Bottom,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SelectedExercisesRow(selectedExercises = selectedExercises)
                    SubmitExercisesRow(
                        modifier = Modifier.padding(
                            start = 16.dp,
                            end = 16.dp,
                            top = 8.dp,
                            bottom = 16.dp
                        ),
                        onSubmit = {
                            searchExercisesViewModel.findDuplicateExercises(selectedExercises)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedExercisesRow(selectedExercises: List<ExerciseDto>) {
    val backgroundColor = MaterialTheme.colorScheme.tertiary
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val cornerSize = 16.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(cornerSize))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "${selectedExercises.size} exercises selected.",
            style = textStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun UpdateExercisesRow(
    modifier: Modifier = Modifier,
    text: String,
    backgroundColor: Color,
    onAction: () -> Unit
) {
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleMedium.copy(
        color = textColor
    )

    val cornerSize = 16.dp

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(cornerSize))
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable { onAction() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = textStyle,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SubmitExercisesRow(modifier: Modifier = Modifier, onSubmit: () -> Unit) {
    UpdateExercisesRow(
        modifier = modifier,
        text = "Add exercises",
        backgroundColor = LocalExtendedColorScheme.current.workoutBannerColors.selectButtonColor,
        onAction = onSubmit
    )
}

@Composable
fun DeleteExercisesRow(modifier: Modifier = Modifier, onDelete: () -> Unit) {
    UpdateExercisesRow(
        modifier = modifier,
        text = "Delete exercises",
        backgroundColor = LocalExtendedColorScheme.current.workoutBannerColors.deleteButtonColor,
        onAction = onDelete
    )
}

@Preview
@PreviewLightDark
@Composable
private fun SelectedExercisesRowPreview() {
    val selectedExercises = listOf<ExerciseDto>()

    SelectedExercisesRow(selectedExercises)
}

@Preview
@PreviewLightDark
@Composable
private fun DeleteExercisesRowPreview() {
    DeleteExercisesRow() {

    }
}

@Preview
@PreviewLightDark
@Composable
private fun SubmitExercisesRowPreview() {
    SubmitExercisesRow() {

    }
}

@Preview
@Composable
fun SearchExercisesScreenPreview() {

}

