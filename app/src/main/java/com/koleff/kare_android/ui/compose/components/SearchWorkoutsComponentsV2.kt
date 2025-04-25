package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.model.dto.WorkoutDto

@Composable
fun SearchWorkoutList(
    modifier: Modifier,
    workoutList: List<WorkoutDto>,
    onSelectedWorkout: (WorkoutDto) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(workoutList.size) { currentWorkoutId ->
            val currentWorkout = workoutList[currentWorkoutId]
            WorkoutBannerV2(
                workout = currentWorkout,
                onClick = onSelectedWorkout
            )
        }
    }
}


@Preview
@Composable
fun SearchWorkoutListPreview() {
    val modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)

    val workoutList = MockupDataGeneratorV2.generateWorkoutList()

    SearchWorkoutList(
        modifier = modifier,
        workoutList = workoutList,
    ) {

    }
}