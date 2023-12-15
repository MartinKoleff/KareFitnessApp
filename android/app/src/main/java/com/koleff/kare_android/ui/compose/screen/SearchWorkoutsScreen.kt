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
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.model.event.OnSearchEvent
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.SearchBar
import com.koleff.kare_android.ui.compose.SearchWorkoutList
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

@Composable
fun SearchWorkoutsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exerciseId: Int,
    workoutViewModel: WorkoutViewModel
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    SearchListScaffold("Select workout", navController, isNavigationInProgress) { innerPadding ->
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

        val workoutState by workoutViewModel.state.collectAsState()
        val allWorkouts = workoutState.workoutList


        //All workouts
        if (workoutState.isLoading) {
            LoadingWheel()
        } else {
            Column(modifier = modifier) {
                //Search bar
                SearchBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onSearch = { text ->
                        workoutViewModel.onSearchEvent(OnSearchEvent.OnSearchTextChange(text))
                    },
                    onToggleSearch = {
                        workoutViewModel.onSearchEvent(OnSearchEvent.OnToggleSearch())
                    })

                SearchWorkoutList(
                    modifier = Modifier.fillMaxSize(),
                    workoutList = allWorkouts,
                    exerciseId = exerciseId,
                    navController = navController
                )
            }
        }
    }
}