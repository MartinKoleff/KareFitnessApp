package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.DoWorkoutPerformanceMetrics
import com.koleff.kare_android.data.room.entity.relations.DoWorkoutPerformanceWithSets
import java.util.Date

@Dao
interface DoWorkoutPerformanceMetricsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutPerformanceMetrics(doWorkoutPerformanceMetrics: DoWorkoutPerformanceMetrics): Long

    @Update
    suspend fun updateWorkoutPerformanceMetrics(doWorkoutPerformanceMetrics: DoWorkoutPerformanceMetrics)

    @Delete
    suspend fun deleteWorkoutPerformanceMetrics(doWorkoutPerformanceMetrics: DoWorkoutPerformanceMetrics)

    @Transaction
    @Query("SELECT * FROM do_workout_performance_metrics d WHERE d.id = :id")
    suspend fun getWorkoutPerformanceMetricsById(id: Int): DoWorkoutPerformanceWithSets?

    @Transaction
    @Query("SELECT * FROM do_workout_performance_metrics d WHERE d.id = :id AND d.date BETWEEN :start AND :end")
    suspend fun getAllWorkoutPerformanceMetricsById(id: Int, start: Date, end: Date): List<DoWorkoutPerformanceWithSets>

    @Transaction
    @Query("SELECT * FROM do_workout_performance_metrics d WHERE d.workoutId = :workoutId AND d.date BETWEEN :start AND :end")
    suspend fun getAllWorkoutPerformanceMetricsByWorkoutId(workoutId: Int, start: Date, end: Date): List<DoWorkoutPerformanceWithSets>

    @Transaction
    @Query("SELECT * FROM do_workout_performance_metrics")
    suspend fun getAllWorkoutPerformanceMetrics(): List<DoWorkoutPerformanceWithSets>

    @Transaction
    @Query("SELECT * FROM do_workout_performance_metrics d WHERE d.date BETWEEN :start AND :end")
    suspend fun getAllWorkoutPerformanceMetrics(start: Date, end: Date): List<DoWorkoutPerformanceWithSets>

    @Transaction
    @Query("SELECT * FROM do_workout_performance_metrics WHERE workoutId = :workoutId ORDER BY date DESC")
    suspend fun getAllWorkoutPerformanceMetricsByWorkoutId(workoutId: Int): List<DoWorkoutPerformanceWithSets>

    @Query("DELETE FROM do_workout_performance_metrics WHERE id = :id")
    fun deleteWorkoutPerformanceMetricsById(id: Int)
}
