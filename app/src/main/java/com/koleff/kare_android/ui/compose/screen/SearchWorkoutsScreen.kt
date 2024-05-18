package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
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
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import com.koleff.kare_android.ui.view_model.SearchWorkoutViewModel

@Composable
fun SearchWorkoutsScreen(
    searchWorkoutViewModel: SearchWorkoutViewModel = hiltViewModel(),
    exerciseViewModel: ExerciseViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var selectedWorkoutId by remember { mutableStateOf(-1) }

    val workoutDetailsState by searchWorkoutViewModel.selectedWorkoutState.collectAsState()
    val updateWorkoutState by searchWorkoutViewModel.updateWorkoutState.collectAsState()

    var isUpdateLoading by remember { mutableStateOf(false) } //Used to show loading between getWorkoutDetails and updateWorkout

    val alpha = remember { Animatable(1f) }  //Used for animated transition
    val screenTitle = remember { mutableStateOf("Select workout") }

    val exerciseState by exerciseViewModel.state.collectAsState()

    LaunchedEffect(selectedWorkoutId) {
        selectedWorkoutId != -1 || return@LaunchedEffect

        searchWorkoutViewModel.getWorkoutDetails(selectedWorkoutId).also {
            isUpdateLoading = true
            screenTitle.value = "Loading..."
        }
    }

    LaunchedEffect(workoutDetailsState) {

        //Await workout details
        if (workoutDetailsState.isSuccessful && workoutDetailsState.workoutDetails.workoutId != -1) {
            val updatedExercises = workoutDetailsState.workoutDetails.exercises.toMutableList()
            val selectedExercise = exerciseState.exercise.copy(workoutId = workoutDetailsState.workoutDetails.workoutId)
            updatedExercises.add(selectedExercise)

            val updatedWorkoutDetails = workoutDetailsState.workoutDetails.copy(exercises = updatedExercises)
            searchWorkoutViewModel.updateWorkoutDetails(updatedWorkoutDetails)
        }
    }

    LaunchedEffect(updateWorkoutState) {

        //Await update workout
        if (updateWorkoutState.isSuccessful) {

//            //Animate transition navigation
//            alpha.animateTo(
//                targetValue = 0f,
//                animationSpec = TweenSpec(durationMillis = 500)
//            ) {
//                searchWorkoutViewModel.navigateToWorkoutDetails(updateWorkoutState.workoutDetails.workoutId)
//            }

            searchWorkoutViewModel.navigateToWorkouts()

            //Reset state
            searchWorkoutViewModel.resetUpdateWorkoutState()
        }
    }

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }

    //Dialog callbacks
    val onErrorDialogDismiss = {
        showErrorDialog = false
        searchWorkoutViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(workoutDetailsState, updateWorkoutState){
        val states = listOf(
            workoutDetailsState,
            updateWorkoutState
        )

        val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
        error = errorState.error
        showErrorDialog = errorState.isError

        Log.d("SearchWorkoutsScreen", "Error detected -> $showErrorDialog")
    }

    //Dialogs
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onErrorDialogDismiss)
        }
    }

    //States
    val workoutState by searchWorkoutViewModel.workoutsState.collectAsState()

    //Navigation Callbacks
    val onNavigateToSettings = {
        searchWorkoutViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { searchWorkoutViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

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

        if (workoutState.isLoading || isUpdateLoading) {
            LoadingWheel()
        } else {
            Column(modifier = modifier) {
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onSearch = { text ->
                        searchWorkoutViewModel.onTextChange(text)
                    },
                    onToggleSearch = {
                        searchWorkoutViewModel.onToggleSearch()
                    })

                SearchWorkoutList(
                    modifier = Modifier.fillMaxSize(),
                    workoutList = workoutState.workoutList,
                ) { workoutId ->

                    //Updates WorkoutDetailsViewModel...
                    selectedWorkoutId = workoutId
                }
            }
        }
    }
}