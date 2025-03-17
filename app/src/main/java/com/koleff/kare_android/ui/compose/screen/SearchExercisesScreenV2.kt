package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
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
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.AddToWorkoutButton
import com.koleff.kare_android.ui.compose.components.ExerciseList
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.MuscleGroupFilterButtonGrid
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.compose.dialogs.DuplicateExercisesFoundDialog
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.LoadingDialog
import com.koleff.kare_android.ui.event.OnMultipleExercisesUpdateEvent
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.SearchExercisesViewModelV2

@Composable
fun SearchExercisesScreenV2(
    searchExercisesViewModel: SearchExercisesViewModelV2 = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val exercisesState by searchExercisesViewModel.state.collectAsState()
    val updateWorkoutState by searchExercisesViewModel.updateWorkoutState.collectAsState() //TODO: update loading logic
    val duplicateExercisesState by searchExercisesViewModel.duplicateExercisesState.collectAsState()
    val selectedExercisesState by searchExercisesViewModel.selectedExercisesState.collectAsState()

    val onSubmitExercises: () -> Unit = {  //(List<ExerciseDto>) -> Unit
        searchExercisesViewModel.onMultipleExercisesUpdateEvent(
            OnMultipleExercisesUpdateEvent.OnMultipleExercisesSubmit(selectedExercisesState.exerciseList)
        )
    }

    var showLocalLoadingDialog by remember { mutableStateOf(false) }
    var showGlobalLoadingDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showOnDuplicateExercisesFoundDialog by remember { mutableStateOf(false) }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }

    val onErrorDialogDismiss = {
        showErrorDialog = false
        searchExercisesViewModel.clearError()
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

        showLocalLoadingDialog = exercisesState.isLoading || duplicateExercisesState.isLoading
        showGlobalLoadingDialog = updateWorkoutState.isLoading || updateWorkoutState.isSuccessful
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

    if (showGlobalLoadingDialog) {
        LoadingDialog {
            showGlobalLoadingDialog = false
        }
    }

    var searchText by remember { mutableStateOf("") }

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

        Column(modifier = modifier) {
            SearchBar(
                searchText = searchText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onSearch = { text ->
                    searchExercisesViewModel.onTextChange(searchText = text)
                },
                onSearchTextChange = { searchText = it },
                onToggleSearch = {
                    searchExercisesViewModel.onToggleSearch()
                })

            MuscleGroupFilterButtonGrid(
                muscleGroups = MuscleGroup.getSupportedMuscleGroups(),
                onClick = { muscleGroup ->
                    searchExercisesViewModel.onFilterExercise(searchText, muscleGroup)
                }
            )

            if (showLocalLoadingDialog) {
                LoadingWheel(innerPadding = innerPadding)
            } else {
                ExerciseList(
                    modifier = Modifier.weight(7f),
                    exerciseList = exercisesState.exerciseList,
                    selectedExercisesList = selectedExercisesState.exerciseList,
                    onSelect = searchExercisesViewModel::selectExercise,
                    onClick = searchExercisesViewModel::selectExercise
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(top = 8.dp, bottom = 16.dp)
                ) {
                    if (selectedExercisesState.exerciseList.isNotEmpty()) {
                        AddToWorkoutButton(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            text = "Add ${selectedExercisesState.exerciseList.size} to workout",
                            onClick = {
                                searchExercisesViewModel.findDuplicateExercises(
                                    selectedExercisesState.exerciseList
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

