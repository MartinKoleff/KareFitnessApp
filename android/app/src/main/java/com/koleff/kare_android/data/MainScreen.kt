package com.koleff.kare_android.data

sealed class MainScreen(val route: String) {
    object Dashboard : MainScreen("dashboard") //Muscle group screen
    object MyWorkout : MainScreen("my_workout")  //Current selected workout for the day
    object Workouts : MainScreen("workouts") //List of all workouts created / imported
    object MuscleGroupExercisesList : MainScreen("muscle_group/{muscle_group_id}"){ //When muscle group is selected -> show exercise list
            fun createRoute(muscleGroupId: Int) = "muscle_group/$muscleGroupId"
    }
    object ExerciseDetails : MainScreen("exercise_details/{exercise_id}/{muscle_group_id}"){ //When exercise is selected -> show details screen
        fun createRoute(exerciseId: Int, muscleGroupId: Int) = "exercise_details/$exerciseId/$muscleGroupId"
    }

    object ExerciseDetailsConfigurator : MainScreen("exercise_details_configurator/{exercise_id}/{muscle_group_id}"){ //When exercise is selected in workout screen -> show configurator screen (with reps, sets, weight)
        fun createRoute(exerciseId: Int, muscleGroupId: Int) = "exercise_details_configurator/$exerciseId/$muscleGroupId"
    }
    object WorkoutDetails : MainScreen("workout_details/{workout_id}/{submitted_exercise}"){ //When workout is selected -> show details screen
        fun createRoute(workoutId: Int, submittedExercise: String = "")
                = "workout_details/$workoutId/$submittedExercise"
    }

    object Settings : MainScreen("settings") //Settings list
    object SearchWorkoutsScreen : MainScreen("search_workouts") //Select workout where exercise will be added
}