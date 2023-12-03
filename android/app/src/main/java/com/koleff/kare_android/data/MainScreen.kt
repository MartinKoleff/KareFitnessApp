package com.koleff.kare_android.data

sealed class MainScreen(val route: String) {
    object Dashboard : MainScreen("dashboard") //Muscle group screen
    object MyWorkout : MainScreen("my_workout")  //Current selected workout for the day
    object Workouts : MainScreen("workouts") //List of all workouts created / imported
    object MuscleGroup : MainScreen("muscle_group/{muscle_group_id}"){ //When muscle group is selected -> show exercise list
            fun createRoute(muscleGroupId: Int) = "muscle_group/$muscleGroupId"
    }
    object ExerciseDetails : MainScreen("exercise_details/{exercise_id}"){ //When exercise is selected -> show details screen
        fun createRoute(exerciseId: Int) = "exercise_details/$exerciseId"
    }
    object Settings : MainScreen("settings") //Settings list
}