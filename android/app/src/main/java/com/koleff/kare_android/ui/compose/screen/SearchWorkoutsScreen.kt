package com.koleff.kare_android.ui.compose.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.SearchBar
import com.koleff.kare_android.ui.compose.SearchWorkoutList
import com.koleff.kare_android.ui.compose.scaffolds.SearchListScaffold
import com.koleff.kare_android.ui.view_model.SearchWorkoutViewModel

@Composable
fun SearchWorkoutsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    exercise: ExerciseDto,
    searchWorkoutViewModel: SearchWorkoutViewModel,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    var selectedWorkoutId by remember { mutableStateOf(-1) }

    val workoutDetailsState by searchWorkoutViewModel.selectedWorkoutState.collectAsState()
    val updateWorkoutState by searchWorkoutViewModel.updateWorkoutState.collectAsState()

    var isUpdateLoading by remember { mutableStateOf(false) } //Used to show loading between getWorkoutDetails and updateWorkout

    val alpha = remember { Animatable(1f) }  //Used for animated transition
    val screenTitle = remember { mutableStateOf("Select workout") }

    LaunchedEffect(selectedWorkoutId) {
        selectedWorkoutId != -1 || return@LaunchedEffect

        searchWorkoutViewModel.getWorkoutDetails(selectedWorkoutId).also {
            isUpdateLoading = true
            screenTitle.value = "Loading..."
        }
    }

    LaunchedEffect(key1 = workoutDetailsState) {

        //Await workout details
        if (workoutDetailsState.isSuccessful && workoutDetailsState.workout.workoutId != -1) {
            workoutDetailsState.workout.exercises.add(exercise)

            searchWorkoutViewModel.updateWorkout(workoutDetailsState.workout)
        }
    }

    LaunchedEffect(key1 = updateWorkoutState) {
        //Await update workout
        if (updateWorkoutState.isSuccessful) {

            //Animate transition navigation
            alpha.animateTo(
                targetValue = 0f,
                animationSpec = TweenSpec(durationMillis = 500)
            ) {
                navController.navigate(MainScreen.WorkoutDetails.createRoute(workoutId = updateWorkoutState.workout.workoutId)) {

                    //Pop backstack and set the first element to be the dashboard
                    popUpTo(MainScreen.Workouts.route) { inclusive = false }

                    //Clear all other entries in the back stack
                    launchSingleTop = true
                }
            }

            //Reset state
            searchWorkoutViewModel.resetUpdateWorkoutState()
        }
    }

    SearchListScaffold(
//        modifier = Modifier.alpha(alpha.value), //Animation transition
        screenTitle = screenTitle.value,
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

        val workoutState by searchWorkoutViewModel.workoutsState.collectAsState()
        val allWorkouts = workoutState.workoutList

        //All workouts
        if (workoutState.isLoading || isUpdateLoading) {
            LoadingWheel()
        } else {
            Column(modifier = modifier) {
                //Search bar
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
                    workoutList = allWorkouts,
                    navController = navController
                ) { workoutId ->

                    //Updates WorkoutDetailsViewModel...
                    selectedWorkoutId = workoutId
                }
            }
        }
    }
}