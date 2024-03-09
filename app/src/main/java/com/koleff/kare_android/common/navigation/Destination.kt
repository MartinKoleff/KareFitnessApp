package com.koleff.kare_android.common.navigation

sealed interface Destination {
    val route: String

    //Dashboard screen (Muscle group screen)
    data object Dashboard : Destination {

        override val route: String
            get() = "dashboard"

        const val ROUTE = "dashboard"
    }

    //List of all workouts created / imported
    data object Workouts : Destination {
        override val route: String
            get() = "workouts"

        const val ROUTE = "workouts"
    }

    //When muscle group is selected -> show exercise list
    class MuscleGroupExercisesList(private val muscleGroupId: Int) : Destination {
        override val route: String
            get() = "muscle_group/$muscleGroupId"

        companion object {
            const val ROUTE = "muscle_group/{muscle_group_id}"
        }
    }

    //When exercise is selected -> show details screen
    class ExerciseDetails(private val exerciseId: Int, private val muscleGroupId: Int) :
        Destination {
        override val route: String
            get() = "exercise_details/$exerciseId/$muscleGroupId"

        companion object {
            const val ROUTE = "exercise_details/{exercise_id}/{muscle_group_id}"
        }
    }

    //When exercise is selected in search exercises screen (opened from workout details screen) ->
    //show configurator screen (with reps, sets, weight)
    class ExerciseDetailsConfigurator(
        private val exerciseId: Int,
        private val muscleGroupId: Int,
        private val workoutId: Int
    ) : Destination {
        override val route: String
            get() = "exercise_details_configurator/$exerciseId/$muscleGroupId/$workoutId"

        companion object {
            const val ROUTE =
                "exercise_details_configurator/{exercise_id}/{muscle_group_id}/{workout_id}"
        }
    }

    //When workout is selected -> show details screen
    class WorkoutDetails(private val workoutId: Int) : Destination {
        override val route: String
            get() = "workout_details/$workoutId"

        companion object {
            const val ROUTE =
                "workout_details/{workout_id}"
        }
    }

    //Settings list
    data object Settings : Destination {
        override val route: String
            get() = "settings"

        const val ROUTE = "settings"
    }

    //Select workout where exercise will be added
    data class SearchWorkoutsScreen(private val exerciseId: Int) : Destination {
        override val route: String
            get() = "search_workouts/$exerciseId"

        companion object {
            const val ROUTE =
                "search_workouts/{exercise_id}"
        }
    }

    //Select exercise for current workout
    data class SearchExercisesScreen(private val workoutId: Int) : Destination {
        override val route: String
            get() = "search_exercise/$workoutId"

        companion object {
            const val ROUTE =
                "search_exercise/{workout_id}"
        }
    }

    //Welcome screen
    data object Welcome : Destination {

        override val route: String
            get() = "welcome"

        const val ROUTE = "welcome"
    }

    //Login screen
    data object Login : Destination {

        override val route: String
            get() = "login"

        const val ROUTE = "login"
    }

    //Register screen
    data object Register : Destination {

        override val route: String
            get() = "register"

        const val ROUTE = "register"
    }
}