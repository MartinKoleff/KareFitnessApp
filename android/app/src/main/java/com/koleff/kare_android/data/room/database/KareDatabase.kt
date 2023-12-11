package com.koleff.kare_android.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dto.Exercise
import com.koleff.kare_android.data.room.dto.Workout


@Database(
    entities = [ExerciseDto::class, Exercise::class, WorkoutDto::class, Workout::class],
    version = 1,
    exportSchema = true
)
abstract class KareDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val workoutDao: WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: KareDatabase? = null

        fun getInstance(context: Context): KareDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                KareDatabase::class.java,
                Constants.DATABASE_NAME
            ).build()

    }
}