package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.WorkoutDto

interface Preferences {
    fun saveDashboardMuscleGroupList(muscleGroupList: List<MuscleGroup>)
    fun saveFavoriteWorkouts(favoriteWorkouts: List<WorkoutDto>)
    fun loadFavoriteWorkouts(): List<WorkoutDto>
    fun loadDashboardMuscleGroupList(): List<MuscleGroup>
    fun hasInitializedExerciseTable(): Boolean
    fun hasInitializedWorkoutTable(): Boolean
    fun hasInitializedUserTable(): Boolean
    fun initializeExerciseTable()
    fun initializeWorkoutTable()
    fun initializeUserTable()
    fun saveCredentials(credentials: Credentials)
    fun getCredentials(): Credentials?
    fun deleteCredentials()
    fun saveTokens(tokens: Tokens)
    fun getTokens(): Tokens?

    companion object {
        const val DASHBOARD_MUSCLE_GROUP_LIST = "dashboard_muscle_group_list"
        const val HAS_INITIALIZED_EXERCISE_TABLE = "has_initialized_exercise_table"
        const val HAS_INITIALIZED_WORKOUT_TABLE = "has_initialized_workout_table"
        const val HAS_INITIALIZED_USER_TABLE = "has_initialized_user_table"
        const val HAS_TOKENS = "has_tokens"
        const val FAVORITE_WORKOUTS = "favorite_workouts"
        const val CREDENTIALS = "credentials"
        const val TOKENS = "tokens"
    }
}