package com.koleff.kare_android.common.navigation

sealed class Destination(val route: String) {
    object Dashboard : Destination("dashboard") //Muscle group screen
    object Workouts : Destination("workouts") //List of all workouts created / imported
    object MuscleGroupExercisesList :
        Destination("muscle_group/{muscle_group_id}") { //When muscle group is selected -> show exercise list
        fun createRoute(muscleGroupId: Int) = "muscle_group/$muscleGroupId"
    }

    object ExerciseDetails :
        Destination("exercise_details/{exercise_id}/{muscle_group_id}") { //When exercise is selected -> show details screen
        fun createRoute(exerciseId: Int, muscleGroupId: Int) =
            "exercise_details/$exerciseId/$muscleGroupId"
    }

    object ExerciseDetailsConfigurator :
        Destination("exercise_details_configurator/{exercise_id}/{muscle_group_id}/{workout_id}") { //When exercise is selected in search exercises screen (opened from workout details screen) -> show configurator screen (with reps, sets, weight)
        fun createRoute(exerciseId: Int, muscleGroupId: Int, workoutId: Int) =
            "exercise_details_configurator/$exerciseId/$muscleGroupId/$workoutId"
    }

    object WorkoutDetails :
        Destination("workout_details/{workout_id}") { //When workout is selected -> show details screen
        fun createRoute(workoutId: Int) = "workout_details/$workoutId"
    }

    object Settings : Destination("settings") //Settings list
    object SearchWorkoutsScreen :
        Destination("search_workouts/{exercise_id}") { //Select workout where exercise will be added
        fun createRoute(exerciseId: Int) = "search_workouts/$exerciseId"
    }

    object SearchExercisesScreen :
        Destination("search_exercise/{workout_id}") { //Select exercise for current workout
        fun createRoute(workoutId: Int) = "search_exercise/$workoutId"
    }

}