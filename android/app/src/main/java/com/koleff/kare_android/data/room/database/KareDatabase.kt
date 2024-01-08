package com.koleff.kare_android.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.SetEntity
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsWorkoutCrossRef

@Database(
    entities = [
        Exercise::class,
        Workout::class,
        WorkoutDetails::class,
        ExerciseDetails::class,
        SetEntity::class,
        WorkoutDetailsExerciseCrossRef::class,
        WorkoutDetailsWorkoutCrossRef::class,
        ExerciseDetailsExerciseCrossRef::class,
        ExerciseSetCrossRef::class
    ],
    version = 3,
    exportSchema = false,
)
abstract class KareDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val exerciseDetailsDao: ExerciseDetailsDao
    abstract val workoutDao: WorkoutDao
    abstract val workoutDetailsDao: WorkoutDetailsDao
    abstract val exerciseSetDao: ExerciseSetDao

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