package com.koleff.kare_android.data.room.manager

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.User
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

class UserDBManager @Inject constructor(
    private val userDao: UserDao,
    private val hasInitializedDB: Boolean
) {
    suspend fun initializeUserTable(onDBInitialized: () -> Unit) = withContext(Dispatchers.IO) {
        if (hasInitializedDB) return@withContext

        //Users from backend server...
        val user = User(
            UUID.fromString("5556f6b2-fa8e-4cd2-809d-1e2cf0cefdf9"),
            "koleff",
            "epic_password_69",
            "email@test.com"
        )

        val admin = User(
            UUID.fromString("21eec03c-f9ae-46c8-93c5-087428699463"),
            "admin",
            "password",
            "admin@test.com"
        )
        userDao.saveUser(user)
        userDao.saveUser(admin)
        onDBInitialized()
    }
}