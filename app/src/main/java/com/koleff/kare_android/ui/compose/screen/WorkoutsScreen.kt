package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.WorkoutSegmentButton
import com.koleff.kare_android.ui.compose.banners.AddWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.NoWorkoutSelectedBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableWorkoutBanner
import com.koleff.kare_android.ui.compose.dialogs.EditWorkoutDialog
import com.koleff.kare_android.ui.compose.dialogs.WarningDialog
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.state.BaseState
import com.koleff.kare_android.ui.view_model.WorkoutViewModel
import java.util.Locale

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutsScreen(
    workoutListViewModel: WorkoutViewModel = hiltViewModel()
) {
    MainScreenScaffold(
        "Workouts",
        onNavigateToDashboard = { workoutListViewModel.onNavigateToDashboard() },
        onNavigateToWorkouts = { workoutListViewModel.onNavigateToWorkouts() },
        onNavigateBackAction = { workoutListViewModel.onNavigateBack() },
        onNavigateToSettings = { workoutListViewModel.onNavigateToSettings() }
    ) { innerPadding ->
        val buttonModifier = Modifier
            .fillMaxWidth()
            .padding(
                PaddingValues(
                    top = innerPadding.calculateTopPadding(),
                    start = 4.dp + innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = 4.dp + innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = 0.dp
                )
            )

        val contentModifier = Modifier
            .fillMaxSize()
            .padding(
                PaddingValues(
                    top = 8.dp,
                    start = innerPadding.calculateStartPadding(LayoutDirection.Rtl),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Rtl),
                    bottom = innerPadding.calculateBottomPadding()
                )
            )

        val workoutBannerModifier = Modifier
            .fillMaxWidth()
            .height(200.dp)

        //Pull to refresh
        val pullRefreshState = rememberPullRefreshState(
            refreshing = workoutListViewModel.isRefreshing,
            onRefresh = { workoutListViewModel.getWorkouts() }
        )

        //Refresh screen
        LaunchedEffect(workoutListViewModel.hasUpdated) { //Update has happened in WorkoutDetails screen
            Log.d(
                "WorkoutsScreen",
                "WorkoutsScreen updated -> hasUpdated: ${workoutListViewModel.hasUpdated}."
            )
            workoutListViewModel.getWorkouts()
        }

        //States
        val workoutState by workoutListViewModel.state.collectAsState()
        val deleteWorkoutState by workoutListViewModel.deleteWorkoutState.collectAsState()
        val updateWorkoutState by workoutListViewModel.updateWorkoutState.collectAsState()

        //Dialog visibility
        var showEditWorkoutNameDialog by remember { mutableStateOf(false) }
        var showSelectDialog by remember { mutableStateOf(false) }
        var showDeleteDialog by remember { mutableStateOf(false) }
        var showErrorDialog by remember { mutableStateOf(false) }
        var showLoadingDialog by remember { mutableStateOf(false) }

        //Dialog callbacks
        var selectedWorkout by remember { mutableStateOf<WorkoutDto?>(null) }

        val onDeleteWorkout: () -> Unit = {
            selectedWorkout?.let {
                workoutListViewModel.deleteWorkout(selectedWorkout!!.workoutId)

                showDeleteDialog = false
            }
        }

        val onSelectWorkout: () -> Unit = {
            selectedWorkout?.let {
                workoutListViewModel.selectWorkout(selectedWorkout!!.workoutId)

                showSelectDialog = false
            }
        }

        val onEditWorkoutName: (String) -> Unit = { newName ->
            selectedWorkout?.let {
                selectedWorkout = selectedWorkout!!.copy(name = newName)

                //Update workout
                workoutListViewModel.updateWorkout(selectedWorkout!!)

                showEditWorkoutNameDialog = false
            }
        }

        //Error handling
        val onErrorDialogDismiss = {
            showErrorDialog = false
            workoutListViewModel.clearError() //Enters launched effect to update showErrorDialog...
        }

        var error by remember { mutableStateOf<KareError?>(null) }
        LaunchedEffect(
            workoutState,
            deleteWorkoutState,
            updateWorkoutState
        ) {
            val states = listOf(
                workoutState,
                deleteWorkoutState,
                updateWorkoutState
            )

            val errorState: BaseState = states.firstOrNull { it.isError } ?: BaseState()
            error = errorState.error
            showErrorDialog = errorState.isError
            Log.d("WorkoutsScreen", "Error detected -> $showErrorDialog")

            val loadingState: BaseState = states.firstOrNull { it.isLoading } ?: BaseState()
            showLoadingDialog = loadingState.isLoading || workoutListViewModel.isRefreshing
        }

        if (showErrorDialog) {
            error?.let {
                ErrorDialog(it, onErrorDialogDismiss)
            }
        }

        //Dialogs
        if (showEditWorkoutNameDialog && selectedWorkout != null) {
            EditWorkoutDialog(
                currentName = selectedWorkout!!.name,
                onDismiss = { showEditWorkoutNameDialog = false },
                onConfirm = onEditWorkoutName
            )
        }

        if (showDeleteDialog && selectedWorkout != null) {
            WarningDialog(
                title = "Delete Workout",
                description = "Are you sure you want to delete this workout? This action cannot be undone.",
                actionButtonTitle = "Delete",
                onClick = onDeleteWorkout,
                onDismiss = { showDeleteDialog = false }
            )
        }

        if (showSelectDialog && selectedWorkout != null) {
            val selectWord = if (selectedWorkout!!.isSelected) "De-select" else "Select"

            WarningDialog(
                title = "$selectWord Workout",
                description = "Are you sure you want to ${selectWord.lowercase(Locale.getDefault())} this workout?",
                actionButtonTitle = selectWord,
                onClick = onSelectWorkout,
                onDismiss = { showSelectDialog = false }
            )
        }

        Box(
            Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                //Filter buttons
                WorkoutSegmentButton(
                    modifier = buttonModifier,
                    selectedOptionIndex = 1, //Workouts screen
                    isDisabled = workoutState.isLoading,
                    workoutListViewModel = workoutListViewModel
                )

                if (showLoadingDialog) { //Don't show loader if retrieved from cache...
                    LoadingWheel(
                        innerPadding = innerPadding,
                        hideScreen = true
                    )
                } else {

                    //MyWorkout Screen
                    if (workoutState.isMyWorkoutScreen) {
                        val workout = workoutState.workoutList.firstOrNull {
                            it.isSelected
                        }

                        workout?.let {
                            SwipeableWorkoutBanner(
                                modifier = workoutBannerModifier,
                                workout = workout,
                                hasDescription = true,
                                onDelete = {
                                    showDeleteDialog = true
                                    selectedWorkout = workout
                                },
                                onSelect = { showSelectDialog = true },
                                onClick = {
                                    workoutListViewModel.navigateToWorkoutDetails(
                                        workout = it
                                    )
                                },
                                onEdit = {
                                    showEditWorkoutNameDialog = true
                                    selectedWorkout = workout
                                }
                            )
                        } ?: run {

                            //No workout is selected
                            NoWorkoutSelectedBanner {

                                //Navigate to SearchWorkoutsScreen...
                                workoutListViewModel.navigateToSearchWorkout(-1) //TODO: test
                            }
                        }
                    } else {

                        //All Workouts Screen
                        LazyColumn(modifier = contentModifier) {

                            //Workout List
                            items(workoutState.workoutList.size) { workoutId ->
                                val workout = workoutState.workoutList[workoutId]

                                SwipeableWorkoutBanner(
                                    modifier = workoutBannerModifier,
                                    workout = workout,
                                    onDelete = {
                                        showDeleteDialog = true
                                        selectedWorkout = workout
                                    },
                                    onSelect = {
                                        showSelectDialog = true
                                        selectedWorkout = workout
                                    },
                                    onClick = {
                                        workoutListViewModel.navigateToWorkoutDetails(
                                            workout = workout
                                        )
                                    },
                                    onEdit = {
                                        showEditWorkoutNameDialog = true
                                        selectedWorkout = workout
                                    }
                                )
                            }

                            //Footer
                            item {
                                if (workoutState.workoutList.isEmpty()) {
                                    NoWorkoutSelectedBanner {

                                        //Navigate to SearchWorkoutsScreen...
                                        workoutListViewModel.navigateToSearchWorkout(-1) //TODO: test
                                    }
                                } else {
                                    AddWorkoutBanner {
                                        workoutListViewModel.createNewWorkout()
                                    }
                                }
                            }
                        }
                    }
                }
            }

            PullRefreshIndicator(
                modifier = Modifier.align(Alignment.TopCenter),
                refreshing = workoutListViewModel.isRefreshing,
                state = pullRefreshState
            ) //If put as first content -> hides behind the screen...
        }
    }
}

