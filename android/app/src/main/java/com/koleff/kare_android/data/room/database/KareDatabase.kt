package com.koleff.kare_android.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef

@Database(
    entities = [
        Exercise::class,
        Workout::class,
        WorkoutDetails::class,
        ExerciseDetails::class,
        WorkoutDetailsExerciseCrossRef::class
    ],
    version = 1,
    exportSchema = false,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 3)
//    ]
)
abstract class KareDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val workoutDao: WorkoutDao

    companion object {
        @Volatile
        private var INSTANCE: KareDatabase? = null

//        val MIGRATION_1_3: Migration = MigrationFrom1To3()
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
            )
//                .addMigrations(MIGRATION_1_3)
//                .fallbackToDestructiveMigration()
                .build()
    }
}