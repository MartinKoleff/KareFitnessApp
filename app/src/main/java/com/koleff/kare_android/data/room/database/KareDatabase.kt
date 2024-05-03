package com.koleff.kare_android.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.room.Converters
import com.koleff.kare_android.data.room.ExerciseTimeConverters
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.ExerciseSet
import com.koleff.kare_android.data.room.entity.User
import com.koleff.kare_android.data.room.entity.Workout
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
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
        ExerciseSet::class,
        User::class,
        DoWorkoutExerciseSet::class,
        DoWorkoutPerformanceMetrics::class,
        WorkoutConfiguration::class
    ],
    version = 21,
    exportSchema = false,
)
@TypeConverters(Converters::class, ExerciseTimeConverters::class)
abstract class KareDatabase : RoomDatabase() {
    abstract val exerciseDao: ExerciseDao
    abstract val exerciseDetailsDao: ExerciseDetailsDao
    abstract val workoutDao: WorkoutDao
    abstract val workoutDetailsDao: WorkoutDetailsDao
    abstract val exerciseSetDao: ExerciseSetDao
    abstract val userDao: UserDao
    abstract val doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao
    abstract val doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao
    abstract val workoutConfigurationDao: WorkoutConfigurationDao

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