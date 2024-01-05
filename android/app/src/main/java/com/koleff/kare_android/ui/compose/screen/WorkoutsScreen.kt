package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.WorkoutSegmentButton
import com.koleff.kare_android.ui.compose.banners.AddWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.NoWorkoutSelectedBanner
import com.koleff.kare_android.ui.compose.banners.SwipeableWorkoutBanner
import com.koleff.kare_android.ui.compose.banners.openWorkoutDetailsScreen
import com.koleff.kare_android.ui.compose.scaffolds.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.WorkoutViewModel

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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            val workoutState by workoutListViewModel.state.collectAsState()

            //Filter buttons
            WorkoutSegmentButton(
                modifier = buttonModifier,
                navController = navController,
                selectedOptionIndex = 0, //MyWorkout screen
                isDisabled = workoutState.isLoading,
                workoutListViewModel = workoutListViewModel
            )

            if (workoutState.isLoading && workoutState.workoutList.isEmpty()) { //Don't show loader if retrieved from cache...
                LoadingWheel(innerPadding = innerPadding)
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
                            onDelete = {},
                            onClick = {
                                openWorkoutDetailsScreen(
                                    workout = it,
                                    navController = navController
                                )
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
                                onDelete = {},
                                onClick = {
                                    openWorkoutDetailsScreen(workout, navController = navController)
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
    }
}
