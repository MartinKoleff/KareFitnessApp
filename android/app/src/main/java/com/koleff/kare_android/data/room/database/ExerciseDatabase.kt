package com.koleff.kare_android.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dto.Exercise


@Database(
    entities = [ExerciseDto::class, Exercise::class],
    version = 1,
    exportSchema = true
)
abstract class ExerciseDatabase : RoomDatabase() {
    abstract val dao: ExerciseDao

    companion object {
        @Volatile
        private var INSTANCE: ExerciseDatabase? = null

        fun getInstance(context: Context): ExerciseDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ExerciseDatabase::class.java,
                Constants.DATABASE_NAME
            ).build()

    }
}