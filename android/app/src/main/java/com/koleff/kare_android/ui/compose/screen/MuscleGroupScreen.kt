package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.koleff.kare_android.ui.compose.ExerciseList
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.MachineFilterSegmentButton
import com.koleff.kare_android.ui.compose.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.DashboardViewModel
import com.koleff.kare_android.ui.view_model.ExerciseViewModel

@Composable
fun MuscleGroupScreen(
    navController: NavHostController,
    muscleGroupId: Int = -1, //Invalid group selected...
    isNavigationInProgress: MutableState<Boolean>,
    exerciseViewModelFactory: ExerciseViewModel.Factory,
    dashboardViewModel: DashboardViewModel
) {
    val muscleGroupState by dashboardViewModel.state.collectAsState()
    val muscleGroup = muscleGroupState.muscleGroupList[muscleGroupId]

    val exerciseListViewModel = viewModel<ExerciseViewModel>(
        factory = ExerciseViewModel.provideExerciseViewModelFactory(
            factory = exerciseViewModelFactory,
            muscleGroupId = muscleGroupId
        )
    )

    val exerciseListState by exerciseListViewModel.state.collectAsState()

    MainScreenScaffold(muscleGroup.name, navController, isNavigationInProgress) { innerPadding ->
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

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Filter buttons
            MachineFilterSegmentButton(
                modifier = buttonModifier,
                selectedOptionIndex = -1,
                isDisabled = exerciseListState.isLoading,
                exerciseListViewModel = exerciseListViewModel
            )

            if (exerciseListState.isLoading) {
                LoadingWheel(
                    innerPadding = innerPadding
                )
            } else {
                ExerciseList(
                    innerPadding = innerPadding,
                    exerciseList = exerciseListState.exerciseList
                )
            }
        }
    }
}