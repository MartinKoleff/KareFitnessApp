package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.SearchBar
import com.koleff.kare_android.ui.compose.SearchExercisesList
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.SearchExercisesViewModel

@Composable
fun SearchExercisesScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    workoutId: Int,
    searchExercisesViewModel: SearchExercisesViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    SearchListScaffold(
        screenTitle = "Select exercise",
        navController = navController,
        isNavigationInProgress = isNavigationInProgress
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
                    navController = navController
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
       screenTitle =  "Select exercise",
        navController = navController,
        isNavigationInProgress = mutableStateOf(false)
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
                navController = navController
            )
        }
    }
}

