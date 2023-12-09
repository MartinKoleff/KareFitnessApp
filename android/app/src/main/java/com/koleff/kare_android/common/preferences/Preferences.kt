package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.data.model.dto.MuscleGroupUI
import com.koleff.kare_android.ui.view_model.MuscleGroupUIList

interface Preferences {
    fun saveDashboardMuscleGroupList(muscleGroupList: MuscleGroupUIList)
    fun loadDashboardMuscleGroupList(): List<MuscleGroupUI>
    fun hasInitializedRoomDB(): Boolean
    fun initializeRoomDB()

    companion object {
        const val DASHBOARD_MUSCLE_GROUP_LIST = "dashboard_muscle_group_list"
        const val HAS_INITIALIZED_ROOM_DB = "has_initialized_room_db"
    }
}