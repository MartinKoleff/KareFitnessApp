package com.koleff.kare_android.common.preferences

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
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

    override fun saveFavoriteWorkouts(favoriteWorkouts: List<WorkoutDto>) {
        val json = gson.toJson(favoriteWorkouts)

        sharedPref.edit()
            .putString(Preferences.FAVORITE_WORKOUTS, json)
            .apply()

    }

    override fun loadFavoriteWorkouts(): List<WorkoutDto> {
        val favoriteWorkoutsJson: String =
            sharedPref.getString(Preferences.FAVORITE_WORKOUTS, "") ?: ""

        val type: Type = object : TypeToken<List<WorkoutDto>>() {}.type

        //Parse to List<WorkoutDto>
        try {
            Log.d("DefaultPreferences", "Favorite workouts json: $favoriteWorkoutsJson")
            val workoutsList: List<WorkoutDto> = gson.fromJson(favoriteWorkoutsJson, type)

            if (workoutsList.isEmpty()) {
                return emptyList()
            }

            return workoutsList
        } catch (ex: Exception) {
            when (ex) {
                is IllegalAccessException, is NullPointerException -> {
                    return emptyList()
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
            Log.d("DefaultPreferences", "Dashboard muscle groups json: $muscleGroupListJson")
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
        val json = gson.toJson(credentials)

        sharedPref.edit()
            .putString(Preferences.CREDENTIALS, json)
            .apply()
    }

    override fun getCredentials(): Credentials? {
        val credentialsJson: String =
            sharedPref.getString(Preferences.CREDENTIALS, "") ?: ""

        return try {
            Log.d("DefaultPreferences", "Credentials json: $credentialsJson")
            gson.fromJson(credentialsJson, UserDto::class.java)
        } catch (ex: Exception) {
            when (ex) {
                is IllegalAccessException, is NullPointerException -> {
                    null
                }

                else -> throw ex
            }
        }
    }

    override fun deleteCredentials() {
        sharedPref.edit()
            .putString(Preferences.CREDENTIALS, "")
            .apply()
    }

    override fun saveTokens(tokens: Tokens) {
        val json = gson.toJson(tokens)

        sharedPref.edit()
            .putString(Preferences.TOKENS, json)
            .apply()
    }

    override fun getTokens(): Tokens? {
        val tokensJson: String =
            sharedPref.getString(Preferences.TOKENS, "") ?: ""

        return try {
            Log.d("DefaultPreferences", "Tokens json: $tokensJson")
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

    override fun deleteTokens() {
        sharedPref.edit()
            .putString(Preferences.TOKENS, "")
            .apply()
    }

    override fun updateTokens(tokens: Tokens) {
        deleteTokens()
        saveTokens(tokens)
    }
}