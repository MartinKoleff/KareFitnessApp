package com.koleff.kare_android.common

object Constants {
    const val SCHEME_LOCAL = "http"
    const val SCHEME = "https"
    const val PORT = 8080

    const val BASE_URL = "test" //TODO: add deployed server url
    const val BASE_URL_FULL = "$SCHEME://$BASE_URL/"

    const val BASE_LOCAL_URL = "localhost"
    const val BASE_LOCAL_URL_FULL = "$SCHEME_LOCAL://$BASE_LOCAL_URL"
    const val BASE_LOCAL_URL_FULL_PORT = "$SCHEME_LOCAL://$BASE_LOCAL_URL:$PORT/"

    const val useLocalDataSource = true
    const val useMockupDataSource = true
    const val fakeDelay: Long = 2000L
    const val fakeSmallDelay: Long = 750L

    const val DATABASE_NAME = "my_database"
    const val EXERCISE_TABLE_NAME = "ExerciseDto"
    const val WORKOUT_TABLE_NAME = "WorkoutDto"
}