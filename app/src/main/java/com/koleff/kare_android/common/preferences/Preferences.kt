package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto

interface Preferences {
    fun saveDashboardMuscleGroupList(muscleGroupList: List<MuscleGroup>)
    fun saveSelectedWorkout(selectedWorkout: WorkoutDto)
    fun loadSelectedWorkout(): WorkoutDto?
    fun loadDashboardMuscleGroupList(): List<MuscleGroup>
    fun hasInitializedExerciseTableRoomDB(): Boolean
    fun hasInitializedWorkoutTableRoomDB(): Boolean
    fun initializeExerciseTableRoomDB()
    fun initializeWorkoutTableRoomDB()

    companion object {
        const val DASHBOARD_MUSCLE_GROUP_LIST = "dashboard_muscle_group_list"
        const val HAS_INITIALIZED_EXERCISE_TABLE_ROOM_DB = "has_initialized_exercise_table_room_db"
        const val HAS_INITIALIZED_WORKOUT_TABLE_ROOM_DB = "has_initialized_workout_table_room_db"
        const val SELECTED_WORKOUT = "selected_workout"
    }
}