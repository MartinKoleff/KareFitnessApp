package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.ui.MainScreen
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.ui.compose.banners.ExerciseBannerV2
import com.koleff.kare_android.ui.compose.banners.MuscleGroupHeader

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchExercisesList(
    modifier: Modifier,
    exerciseList: List<ExerciseDto>,
    workoutId: Int,
    navController: NavHostController
) {
    LazyColumn(modifier = modifier) {
        val muscleGroups = exerciseList.map(ExerciseDto::muscleGroup).distinct()

        muscleGroups.forEach { currentMuscleGroup ->

            //Category header
            stickyHeader {
                MuscleGroupHeader(currentMuscleGroup)
            }

            val totalExercisesForMuscleGroup = exerciseList.filter { exercise ->
                exercise.muscleGroup == currentMuscleGroup
            }

            //Exercises for each muscle group
            items(totalExercisesForMuscleGroup.size) { currentExerciseId ->
                val currentExercise = totalExercisesForMuscleGroup[currentExerciseId]
                ExerciseBannerV2(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    exercise = currentExercise,
                ) { selectedExercise ->
                    navController.navigate(
                        MainScreen.ExerciseDetailsConfigurator.createRoute(
                            exerciseId = selectedExercise.exerciseId,
                            workoutId = workoutId,
                            muscleGroupId = selectedExercise.muscleGroup.muscleGroupId
                        )
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun SearchExercisesListPreview() {
    val modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)

    val exerciseList = MockupDataGenerator.generateExerciseList()

    SearchExercisesList(
        modifier = modifier,
        exerciseList = exerciseList,
        workoutId = 1,
        navController = rememberNavController()
    )
}