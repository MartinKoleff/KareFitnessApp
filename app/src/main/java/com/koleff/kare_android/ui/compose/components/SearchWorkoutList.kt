package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.ui.compose.banners.WorkoutBanner

@Composable
fun SearchWorkoutList(
    modifier: Modifier,
    workoutList: List<WorkoutDto>,
    onExerciseAddToWorkout: (Int) -> Unit,
) {
    LazyColumn(modifier = modifier) {
        items(workoutList.size) { currentWorkoutId ->
            val currentWorkout = workoutList[currentWorkoutId]
            WorkoutBanner(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                workout = currentWorkout,
            ) { workout ->
                onExerciseAddToWorkout(workout.workoutId)

                //TODO: show dialog for successfully added workout...
            }
        }
    }
}


@Preview
@Composable
fun SearchWorkoutListPreview() {
    val modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)

    val workoutList = MockupDataGenerator.generateWorkoutList()

    SearchWorkoutList(
        modifier = modifier,
        workoutList = workoutList,
    ) {

    }
}