package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.SearchWorkoutList
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.SuccessDialog
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import com.koleff.kare_android.ui.view_model.SearchWorkoutViewModelV2

@Composable
fun SearchWorkoutsScreenV2(
    searchWorkoutViewModel: SearchWorkoutViewModelV2 = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val workoutState by searchWorkoutViewModel.workoutsState.collectAsState()
    val updateWorkoutState by searchWorkoutViewModel.updateWorkoutState.collectAsState()

    val screenTitle = remember { mutableStateOf("Select workout") }

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    val onErrorDialogDismiss = {
        showErrorDialog = false
        searchWorkoutViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    LaunchedEffect(workoutState, updateWorkoutState) {
        val states = listOf(
            workoutState,
            updateWorkoutState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError

        Log.d("SearchWorkoutsScreen", "Error detected -> $showErrorDialog")

        val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
        showLoadingDialog = loadingState.isLoading //|| updateWorkoutState.isSuccessful
        Log.d("SearchWorkoutsScreen", "Is loading -> $showLoadingDialog")

        showSuccessDialog = updateWorkoutState.isSuccessful
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            title = "Exercise added successfully!",
            onDismiss = {
                searchWorkoutViewModel.navigateToWorkouts()
                showSuccessDialog = false
            },
            onClick = {
                searchWorkoutViewModel.navigateToWorkouts()
                showSuccessDialog = false
            }
        )
    }

    //Navigation Callbacks
    val onNavigateToSettings = {
        searchWorkoutViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { searchWorkoutViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    var searchText by remember { mutableStateOf("") }

    SearchListScaffold(
//        modifier = Modifier.alpha(alpha.value), //Animation transition
        screenTitle = screenTitle.value,
        onNavigateToAction = onNavigateToSettings,
        onNavigateBackAction = onNavigateBack
    ) { innerPadding ->
        val modifier = Modifier
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
            .fillMaxSize()

        if (showLoadingDialog) {
            LoadingWheel()
        } else {
            Column(modifier = modifier) {
                SearchBar(
                    searchText = searchText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onSearch = { text ->
                        searchWorkoutViewModel.onTextChange(text)
                    },
                    onSearchTextChange = { searchText = it },
                    onToggleSearch = {
                        searchWorkoutViewModel.onToggleSearch()
                    })

                SearchWorkoutList(
                    modifier = Modifier.fillMaxSize(),
                    workoutList = workoutState.workoutList,
                    onSelectedWorkout = { workout ->
                        searchWorkoutViewModel.updateWorkoutDetails(workout)
                    }
                )
            }
        }
    }
}