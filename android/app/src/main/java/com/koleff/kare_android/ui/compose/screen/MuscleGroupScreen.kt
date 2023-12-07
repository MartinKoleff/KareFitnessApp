package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.koleff.kare_android.common.DataManager
import com.koleff.kare_android.ui.compose.ExerciseList
import com.koleff.kare_android.ui.compose.LoadingWheel
import com.koleff.kare_android.ui.compose.MachineFilterSegmentButton
import com.koleff.kare_android.ui.compose.MainScreenScaffold
import com.koleff.kare_android.ui.view_model.ExerciseViewModel

@Composable
fun MuscleGroupScreen(
    navController: NavHostController,
    muscleGroupId: Int = -1, //Invalid group selected...
    isNavigationInProgress: MutableState<Boolean>,
    exerciseViewModelFactory: ExerciseViewModel.Factory
) {
    val muscleGroup =
        DataManager.muscleGroupList[muscleGroupId] //TODO: add invalid muscle group id handling...

    val exerciseListViewModel = viewModel<ExerciseViewModel>(
        factory = ExerciseViewModel.provideExerciseViewModelFactory(
            factory = exerciseViewModelFactory,
            muscleGroupId = muscleGroupId
        )
    )

    val exerciseListState by exerciseListViewModel.state.collectAsState()

    MainScreenScaffold(muscleGroup.name, navController, isNavigationInProgress) { innerPadding ->
        val modifier = Modifier
            .padding(innerPadding)
            .fillMaxWidth()

        //Filter buttons
        MachineFilterSegmentButton(modifier = modifier, selectedOptionIndex = -1)

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )

        if(exerciseListState.isLoading){
            LoadingWheel(modifier = Modifier.size(40.dp))
        }else{
            ExerciseList(modifier = modifier, exerciseList = exerciseListState.exerciseList)
        }
    }
}