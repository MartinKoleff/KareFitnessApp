package com.koleff.kare_android.ui.compose.screen

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.WorkoutSegmentButton
import com.koleff.kare_android.ui.compose.banners.AddWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.NoWorkoutSelectedBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.openWorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun WorkoutsScreen(
    navController: NavHostController,
    isNavigationInProgress: MutableState<Boolean>,
    workoutListViewModel: WorkoutViewModel
) {
    MainScreenScaffold("Workouts", navController, isNavigationInProgress) { innerPadding ->
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

        val workoutState by workoutListViewModel.state.collectAsState()
        val deleteWorkoutState by workoutListViewModel.deleteWorkoutState.collectAsState()

        val onDeleteWorkout: (WorkoutDto) -> Unit = { workout ->
            workoutListViewModel.deleteWorkout(workout.workoutId)
        }

        val onSelectWorkout: (WorkoutDto) -> Unit = { workout ->
            workoutListViewModel.selectWorkout(workout.workoutId)
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
                    navController = navController,
                    selectedOptionIndex = 1, //Workouts screen
                    isDisabled = workoutState.isLoading,
                    workoutListViewModel = workoutListViewModel
                )

                if (workoutState.isLoading || workoutListViewModel.isRefreshing || deleteWorkoutState.isLoading) { //Don't show loader if retrieved from cache...
                    LoadingWheel(
                        innerPadding = innerPadding,
                        hideScreen = true
                    )
                } else {

                    //MyWorkout Screen
                    if (workoutState.isMyWorkoutScreen) {
                        val selectedWorkout = workoutState.workoutList.firstOrNull {
                            it.isSelected
                        }

                        selectedWorkout?.let {
                            SwipeableWorkoutBanner(
                                modifier = workoutBannerModifier,
                                workout = selectedWorkout,
                                hasDescription = true,
                                onDelete = { onDeleteWorkout(selectedWorkout) },
                                onSelect = { onSelectWorkout(selectedWorkout) },
                                onClick = {
                                    openWorkoutDetailsScreen(
                                        workout = it,
                                        navController = navController
                                    )
                                },
                                onEdit = {
                                    //Edit prompt -> update workout use case
                                }
                            )
                        } ?: run {

                            //No workout is selected
                            NoWorkoutSelectedBanner {
                                //Navigate to SearchWorkoutsScreen...
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
                                    onDelete = { onDeleteWorkout(workout) },
                                    onSelect = { onSelectWorkout(workout) },
                                    onClick = {
                                        openWorkoutDetailsScreen(
                                            workout,
                                            navController = navController
                                        )
                                    },
                                    onEdit = {
                                        //Edit prompt -> update workout use case
                                    }
                                )
                            }

                            //Footer
                            item {
                                if (workoutState.workoutList.isEmpty()) {
                                    NoWorkoutSelectedBanner {
                                        //Navigate to SearchWorkoutsScreen...
                                    }
                                } else {
                                    AddWorkoutBanner {
                                        //Navigate to WorkoutDetails...
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
