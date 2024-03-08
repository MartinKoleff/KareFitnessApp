package com.koleff.kare_android.common.preferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.WorkoutDto
import java.lang.NullPointerException
import java.lang.reflect.Type


class DefaultPreferences(
    private val sharedPref: SharedPreferences
) : Preferences {

    private val gson: Gson = GsonBuilder().create()

    override fun saveDashboardMuscleGroupList(muscleGroupList: List<MuscleGroup>) {
        val json = gson.toJson(muscleGroupList)

        sharedPref.edit()
            .putString(Preferences.DASHBOARD_MUSCLE_GROUP_LIST, json)
            .apply()
    }

    override fun saveSelectedWorkout(selectedWorkout: WorkoutDto) {
        val json = gson.toJson(selectedWorkout)

        sharedPref.edit()
            .putString(Preferences.SELECTED_WORKOUT, json)
            .apply()

    }

    override fun loadSelectedWorkout(): WorkoutDto? {
        val selectedWorkoutJson: String =
            sharedPref.getString(Preferences.SELECTED_WORKOUT, "") ?: ""

        return try {
            gson.fromJson(selectedWorkoutJson, WorkoutDto::class.java)
        } catch (ex: Exception) {
            when (ex) {
                is IllegalAccessException, is NullPointerException -> {
                    null
                }

                else -> throw ex
            }
        }
    }

    override fun loadDashboardMuscleGroupList(): List<MuscleGroup>{
        val muscleGroupListJson: String =
            sharedPref.getString(Preferences.DASHBOARD_MUSCLE_GROUP_LIST, "") ?: ""

        val type: Type = object : TypeToken<List<MuscleGroup>>() {}.type

        //Parse to MuscleGroupUIList...
        try {
            val muscleGroupList: List<MuscleGroup> = gson.fromJson(muscleGroupListJson, type)

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

    override fun hasInitializedExerciseTable(): Boolean {
        val hasInitialized: Boolean =
            sharedPref.getBoolean(Preferences.HAS_INITIALIZED_EXERCISE_TABLE, false)

        return hasInitialized
    }

    override fun initializeExerciseTable() {
        sharedPref.edit()
            .putBoolean(Preferences.HAS_INITIALIZED_EXERCISE_TABLE, true)
            .apply()
    }

    override fun hasInitializedWorkoutTable(): Boolean {
        val hasInitialized: Boolean =
            sharedPref.getBoolean(Preferences.HAS_INITIALIZED_WORKOUT_TABLE, false)

        return hasInitialized
    }

    override fun initializeWorkoutTable() {
        sharedPref.edit()
            .putBoolean(Preferences.HAS_INITIALIZED_WORKOUT_TABLE, true)
            .apply()
    }

    override fun hasInitializedUserTable(): Boolean {
        val hasInitialized: Boolean =
            sharedPref.getBoolean(Preferences.HAS_INITIALIZED_USER_TABLE, false)

        return hasInitialized
    }

    override fun initializeUserTable() {
        sharedPref.edit()
            .putBoolean(Preferences.HAS_INITIALIZED_USER_TABLE, true)
            .apply()
    }

    override fun saveCredentials(credentials: Credentials) {
        sharedPref.edit()
            .putString(Preferences.CREDENTIALS, credentials.toString())
            .apply()
    }

    override fun getCredentials(): Credentials? {
        val credentialsJson: String =
            sharedPref.getString(Preferences.CREDENTIALS, "") ?: ""

        return try {
            gson.fromJson(credentialsJson, Credentials::class.java)
        } catch (ex: Exception) {
            when (ex) {
                is IllegalAccessException, is NullPointerException -> {
                    null
                }

                else -> throw ex
            }
        }
    }

    override fun saveTokens(tokens: Tokens) {
        sharedPref.edit()
            .putString(Preferences.TOKENS, tokens.toString())
            .apply()
    }

    override fun getTokens(): Tokens? {
        val tokensJson: String =
            sharedPref.getString(Preferences.TOKENS, "") ?: ""

        return try {
            gson.fromJson(tokensJson, Tokens::class.java)
        } catch (ex: Exception) {
            when (ex) {
                is IllegalAccessException, is NullPointerException -> {
                    null
                }

                else -> throw ex
            }
        }
    }
}