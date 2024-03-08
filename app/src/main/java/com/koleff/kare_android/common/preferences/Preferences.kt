package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto

interface Preferences {
    fun saveDashboardMuscleGroupList(muscleGroupList: List<MuscleGroup>)
    fun saveSelectedWorkout(selectedWorkout: WorkoutDto)
    fun loadSelectedWorkout(): WorkoutDto?
    fun loadDashboardMuscleGroupList(): List<MuscleGroup>
    fun hasInitializedExerciseTable(): Boolean
    fun hasInitializedWorkoutTable(): Boolean
    fun hasInitializedUserTable(): Boolean
    fun initializeExerciseTable()
    fun initializeWorkoutTable()
    fun initializeUserTable()
    fun saveCredentials(credentials: Credentials)
    fun getCredentials(): Credentials?


    companion object {
        const val DASHBOARD_MUSCLE_GROUP_LIST = "dashboard_muscle_group_list"
        const val HAS_INITIALIZED_EXERCISE_TABLE = "has_initialized_exercise_table"
        const val HAS_INITIALIZED_WORKOUT_TABLE = "has_initialized_workout_table"
        const val HAS_INITIALIZED_USER_TABLE = "has_initialized_user_table"
        const val SELECTED_WORKOUT = "selected_workout"
        const val CREDENTIALS = "credentials"
    }
}