package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.SearchExercisesList
import com.koleff.kare_android.ui.compose.components.SelectedExercisesRow
import com.koleff.kare_android.ui.compose.components.SubmitExercisesRow
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.compose.components.rememberMutableStateListOf
import com.koleff.kare_android.ui.compose.dialogs.DuplicateExercisesFoundDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.event.OnMultipleExercisesUpdateEvent
import com.koleff.kare_android.ui.state.BaseState
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
    val selectedExercises = rememberMutableStateListOf<ExerciseDto>()

    val onSubmitExercises: () -> Unit = {  //(List<ExerciseDto>) -> Unit
        searchExercisesViewModel.onMultipleExercisesUpdateEvent(
            OnMultipleExercisesUpdateEvent.OnMultipleExercisesSubmit(selectedExercises)
        )
    }

    val onSelectExercise: (ExerciseDto) -> Unit = { selectedExercise ->
        val isNewExercise = selectedExercises.map { it.exerciseId }
            .contains(selectedExercise.exerciseId)

        if (isNewExercise) {
            selectedExercises.removeAll { it.exerciseId == selectedExercise.exerciseId }
        } else {
            selectedExercises.add(
                selectedExercise.copy(workoutId = searchExercisesViewModel.workoutId)
            )
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


