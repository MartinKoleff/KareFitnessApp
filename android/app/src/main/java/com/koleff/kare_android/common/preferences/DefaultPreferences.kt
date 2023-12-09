package com.koleff.kare_android.common.preferences

import android.content.SharedPreferences
import com.koleff.kare_android.ui.view_model.MuscleGroupUIList

class DefaultPreferences(
    private val sharedPref: SharedPreferences
) : Preferences {

    override fun saveDashboardMuscleGroupList(muscleGroupList: MuscleGroupUIList) {
        sharedPref.edit()
            .putString(Preferences.DASHBOARD_MUSCLE_GROUP_LIST, muscleGroupList.toString())
            .apply()
    }

    override fun loadDashboardMuscleGroupList() {
        val muscleGroupList: String = sharedPref.getString(Preferences.DASHBOARD_MUSCLE_GROUP_LIST, "") ?: ""

        //Parse to MuscleGroupUIList...


          //Move to the caller -> call view model if empty
//        if(muscleGroupList.isEmpty()){
//
//        }
    }
}