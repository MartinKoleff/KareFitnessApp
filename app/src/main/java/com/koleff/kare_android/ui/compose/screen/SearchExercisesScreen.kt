package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.SearchExercisesList
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.SearchExercisesViewModel

@Composable
fun SearchExercisesScreen(
    searchExercisesViewModel: SearchExercisesViewModel = hiltViewModel()
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    //Navigation Callbacks
    val onNavigateToSettings = {
        searchExercisesViewModel.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }
    val onNavigateBack = { searchExercisesViewModel.onNavigationEvent(NavigationEvent.NavigateBack) }

    SearchListScaffold(
        screenTitle = "Select exercise",
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

        val exercisesState by searchExercisesViewModel.state.collectAsState()
        val allExercises = exercisesState.exerciseList
        val workoutId = searchExercisesViewModel.workoutId

        //All exercises
        if (exercisesState.isLoading) {
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
                    exerciseList = allExercises,
                    workoutId = workoutId,
                    navigateToExerciseDetailsConfigurator = { selectedExercise, workoutId ->
                        searchExercisesViewModel.navigateToExerciseDetailsConfigurator(
                            exerciseId = selectedExercise.exerciseId,
                            workoutId = workoutId,
                            muscleGroupId = selectedExercise.muscleGroup.muscleGroupId
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun SearchExercisesScreenPreview() {
    val navController = rememberNavController()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    SearchListScaffold(
        screenTitle = "Select exercise",
        onNavigateBackAction = {},
        onNavigateToAction = {},
    ) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                )
            }
            .fillMaxSize()


        val allExercises = MockupDataGenerator.generateExerciseList()

        Column(modifier = modifier) {
            SearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                onToggleSearch = {},
                onSearch = {}
            )

            SearchExercisesList(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 5.dp),
                exerciseList = allExercises,
                workoutId = 1,
                navigateToExerciseDetailsConfigurator = { exercise, workoutId ->

                }
            )
        }
    }
}

