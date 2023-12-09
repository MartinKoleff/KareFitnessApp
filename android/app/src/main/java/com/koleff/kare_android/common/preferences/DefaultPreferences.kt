package com.koleff.kare_android.common.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.koleff.kare_android.data.model.dto.MuscleGroupUI
import com.koleff.kare_android.ui.view_model.MuscleGroupUIList
import java.lang.IllegalStateException
import java.lang.NullPointerException
import java.lang.reflect.Type


class DefaultPreferences(
    private val sharedPref: SharedPreferences
) : Preferences {

    private val gson: Gson = GsonBuilder().create()

    override fun saveDashboardMuscleGroupList(muscleGroupList: MuscleGroupUIList) {
        val json = gson.toJson(muscleGroupList)

        sharedPref.edit()
            .putString(Preferences.DASHBOARD_MUSCLE_GROUP_LIST, json)
            .apply()
    }

    override fun loadDashboardMuscleGroupList(): List<MuscleGroupUI> {
        val muscleGroupListJson: String =
            sharedPref.getString(Preferences.DASHBOARD_MUSCLE_GROUP_LIST, "") ?: ""

        val type: Type = object : TypeToken<List<MuscleGroupUI>>() {}.type

        //Parse to MuscleGroupUIList...
        try {
            val muscleGroupList: List<MuscleGroupUI> = gson.fromJson(muscleGroupListJson, type)

            if (muscleGroupList.isEmpty()) {
                return emptyList()
            }

            return muscleGroupList
        } catch (ex: Exception) {
            when (ex) {
                is IllegalAccessException, is NullPointerException -> {
                    return emptyList()
                }
                else -> throw ex
            }
        }
    }

    override fun hasInitializedRoomDB(): Boolean {
        val hasInitialized: Boolean =
            sharedPref.getBoolean(Preferences.HAS_INITIALIZED_ROOM_DB, false)

        return hasInitialized
    }

    override fun initializeRoomDB() {
        sharedPref.edit()
            .putBoolean(Preferences.HAS_INITIALIZED_ROOM_DB, true)
            .apply()
    }
}