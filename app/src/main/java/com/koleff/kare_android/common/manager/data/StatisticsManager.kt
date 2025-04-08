package com.koleff.kare_android.common.manager.data

import com.koleff.kare_android.R
import com.koleff.kare_android.ui.state.ExerciseStatisticsState
import com.koleff.kare_android.ui.state.GeneralStatisticsState
import com.koleff.kare_android.ui.state.WorkoutStatisticsState
import com.koleff.kare_android.ui.style.StatisticsDataUI

object StatisticsManager {

    //TODO: images
    fun extractGeneralStatistics(generalStatistics: GeneralStatisticsState): List<StatisticsDataUI> {
        val stats = mutableListOf<StatisticsDataUI>()

        // General Stats
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Workouts Completed",
                desc = "Total workouts completed: ${generalStatistics.getWorkoutsCompletedState.totalTimesCompleted}"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Distinct Workouts",
                desc = "Total workouts completed: ${generalStatistics.getDistinctWorkoutsCompletedState.totalTimesCompleted}"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Workout Streak",
                desc = "${generalStatistics.getWorkoutStreakUseCase.streak} days in a row"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Most Frequent Workout",
                desc = generalStatistics.getMostFrequentWorkoutState.workout.name ?: "N/A"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Most Trained Muscle Group",
                desc = generalStatistics.getMostTrainedMuscleGroupState.muscleGroup.muscleGroupName ?: "N/A"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Most Trained Exercise",
                desc = generalStatistics.getMostTrainedExerciseState.exercise.name ?: "N/A"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Weight Lifted",
                desc = "${generalStatistics.getTotalWeightLiftedState.totalWeight} KGs"
            )
        )

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Strongest Muscle Group",
                desc = generalStatistics.getStrongestMuscleGroupState.muscleGroup.muscleGroupName ?: "N/A"
            )
        )

        return stats
    }

    fun extractWorkoutStatistics(workoutStatistics: WorkoutStatisticsState): List<StatisticsDataUI> {
        val stats = mutableListOf<StatisticsDataUI>()
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Times Completed",
                desc = "${workoutStatistics.getWorkoutTotalTimesCompletedState.totalTimesCompleted} times"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Reps Performed",
                desc = "${workoutStatistics.getWorkoutTotalRepsPerformedState.totalReps} reps"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Sets Performed",
                desc = "${workoutStatistics.getWorkoutTotalSetsPerformedState.totalSets} sets"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Weight Lifted",
                desc = "${workoutStatistics.getWorkoutTotalWeightLiftedState.totalWeight} KGs"
            )
        )

        return stats
    }

    fun extractExerciseStatistics(exerciseStatistics: ExerciseStatisticsState): List<StatisticsDataUI> {
        val stats = mutableListOf<StatisticsDataUI>()

        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "PR",
                desc = "${exerciseStatistics.getExercisePRState.pr} KGs"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "1 Rep Max",
                desc = "${exerciseStatistics.getExercise1RepMaxState.pr} KGs"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Reps Performed",
                desc = "${exerciseStatistics.getExerciseTotalRepsPerformedState.totalReps} reps"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Sets Performed",
                desc = "${exerciseStatistics.getExerciseTotalSetsPerformedState.totalSets} sets"
            )
        )
        stats.add(
            StatisticsDataUI(
                imageId = R.drawable.weight,
                title = "Total Weight Lifted",
                desc = "${exerciseStatistics.getExerciseTotalWeightLiftedState.totalWeight} KGs"
            )
        )

        return stats
    }
}