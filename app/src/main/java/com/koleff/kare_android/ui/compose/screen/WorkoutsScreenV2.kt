package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
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
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.SearchBar
import com.koleff.kare_android.ui.compose.components.WorkoutBannerV2
import com.koleff.kare_android.ui.compose.components.WorkoutSegmentButton
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.WorkoutScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.WorkoutViewModelV2

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutsScreenV2(
    workoutsViewModel: WorkoutViewModelV2 = hiltViewModel(),
) {
    WorkoutScaffold(
        "Workouts",
        onWorkoutHistoryAction = { workoutsViewModel.onNavigateToWorkoutHistory() },
        onNavigateToDashboard = { workoutsViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { workoutsViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { workoutsViewModel.onNavigateBack() },
        onNavigateToSettings = { workoutsViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        val keyboardController = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        //Pull to refresh
        val pullRefreshState = rememberPullRefreshState(
            refreshing = workoutsViewModel.isRefreshing,
            onRefresh = {
                workoutsViewModel.onRefresh()
            }
        )

        //States
        val workoutState by workoutsViewModel.state.collectAsState()

        //Refresh screen
        LaunchedEffect(workoutsViewModel.hasUpdated) { //Update has happened in WorkoutDetails screen
            Log.d(
                "WorkoutsScreen",
                "WorkoutsScreen updated -> hasUpdated: ${workoutsViewModel.hasUpdated}."
            )
            workoutsViewModel.getWorkouts()
        }

        //Dialog visibility
        var showErrorDialog by remember { mutableStateOf(false) }
        var showLoadingDialog by remember { mutableStateOf(false) }
        var showFooter by remember { //Workouts are fetched
            mutableStateOf(workoutState.isSuccessful && !showLoadingDialog) //&& workoutState.workoutList.isNotEmpty()
        }

        //Error handling
        val onErrorDialogDismiss = {
            showErrorDialog = false
            workoutsViewModel.clearError()
        }

        var error by remember { mutableStateOf<KareError?>(null) }
        LaunchedEffect(
            workoutState
        ) {
            val states = listOf(
                workoutState
            )

            val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
            error = errorState.error
            showErrorDialog = errorState.isError
            Log.d("WorkoutsScreen", "Error detected -> $showErrorDialog")

            val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
            showLoadingDialog = loadingState.isLoading || workoutsViewModel.isRefreshing

            showFooter =
                workoutState.isSuccessful && !showLoadingDialog
            Log.d("WorkoutsScreen", "Show footer -> $showFooter")
        }

        if (showErrorDialog) {
            error?.let {
                ErrorDialog(it, onErrorDialogDismiss)
            }
        }

        var searchText by remember { mutableStateOf("") }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .pointerInput(Unit) {

                        //Hide keyboard on tap outside SearchBar
                        detectTapGestures(
                            onTap = {
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                        )
                    }
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                SearchBar(
                    searchText = searchText,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    onSearch = { text ->
                        workoutsViewModel.onTextChange(text)
                    },
                    onSearchTextChange = { searchText = it },
                    onToggleSearch = {
                        workoutsViewModel.onToggleSearch()
                    }
                )

                //Filter buttons
                WorkoutSegmentButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    selectedOptionIndex = 1, //Workouts screen
                    isDisabled = showLoadingDialog,
                    onWorkoutFilter = {
                        workoutsViewModel.onWorkoutFilter(searchText, it)
                    }
                )

                //Don't show loader if retrieved from cache...
                if (showLoadingDialog) {
                    LoadingWheel(
                        innerPadding = innerPadding,
                        hideScreen = true
                    )
                } else {
                    LazyColumn {

                        //Workout List
                        items(workoutsViewModel.selectedWorkoutList.size) { currentWorkoutIndex ->
                            val workout =
                                workoutsViewModel.selectedWorkoutList.getOrNull(currentWorkoutIndex)
                                    ?: return@items

                            WorkoutBannerV2(
                                modifier = Modifier.padding(bottom = 6.dp),
                                workout = workout,
                                onClick = {
                                    workoutsViewModel.navigateToWorkoutDetails(
                                        workout = workout
                                    )
                                }
                            )
                        }

                        //Footer
                        if (showFooter) {
                            item {
//                                if (workoutsViewModelV2.selectedWorkoutList.isEmpty()) {
//                                    NoWorkoutSelectedBanner {
//
//                                    }
//                                } else {
//                                    AddWorkoutBanner {
//                                        workoutsViewModelV2.createNewWorkout()
//                                        Log.d("WorkoutScreen", "hasUpdated set to true.")
//                                    }
//                                }
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = workoutsViewModel.isRefreshing,
                state = pullRefreshState
            ) //If put as first content -> hides behind the screen...
        }
    }
}