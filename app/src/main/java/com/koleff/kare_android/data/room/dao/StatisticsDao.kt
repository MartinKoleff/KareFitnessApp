package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.koleff.kare_android.data.model.dto.WeightProgression
import com.koleff.kare_android.data.room.entity.Workout

@Dao
interface StatisticsDao {


    //Exercise specific
    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getBestPR(exerciseId: Int): Float?

    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE reps = 1 AND exerciseId = :exerciseId")
    suspend fun get1RepMax(exerciseId: Int): Float?

    @Query("SELECT SUM(reps) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalRepsPerformedForExercise(exerciseId: Int): Int?

    @Query("SELECT COUNT(*) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalSetsPerformedForExercise(exerciseId: Int): Int?

    @Query("SELECT SUM(reps * weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalWeightLiftedForExercise(exerciseId: Int): Float?


    //Workout specific stats
    @Query("SELECT COUNT(*) FROM do_workout_performance_metrics WHERE workoutId = :workoutId")
    suspend fun getTotalWorkoutsCompleted(workoutId: Int): Int?

    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getMaxWeightForExercise(exerciseId: Int): Float?

    @Query("SELECT weight, date FROM do_workout_exercise_set WHERE exerciseId = :exerciseId ORDER BY date ASC")
    suspend fun getWeightProgression(exerciseId: Int): List<WeightProgression>?

    @Query("SELECT SUM(reps) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId AND workoutId = :workoutId")
    suspend fun getTotalRepsPerformedForExercise(exerciseId: Int, workoutId: Int): Int? //Not working...

    @Query("SELECT COUNT(*) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId AND workoutId = :workoutId")
    suspend fun getTotalSetsPerformedForExercise(exerciseId: Int, workoutId: Int): Int?

    @Query("SELECT SUM(reps * weight) FROM do_workout_exercise_set WHERE workoutId = :workoutId")
    suspend fun getTotalWeightLiftedForWorkout(workoutId: Int): Float?


    //General stats
    @Query("SELECT COUNT(*) FROM do_workout_performance_metrics")
    suspend fun getTotalWorkoutsCompleted(): Int?

    @Query("SELECT DISTINCT COUNT(exerciseId) FROM do_workout_exercise_set")
    suspend fun getTotalExercisesCount(): Int?

    @Query("SELECT COUNT(*) FROM do_workout_exercise_set")
    suspend fun getTotalSetsCount(): Int?

    @Query("SELECT SUM(reps) FROM do_workout_exercise_set")
    suspend fun getTotalRepsCount(): Int?

    @Query("SELECT SUM(reps * weight) FROM do_workout_exercise_set")
    suspend fun getTotalWeightLifted(): Float?

    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE reps = 1")
    suspend fun getBestPR(): Float?

    @Query("""
    SELECT w.*
    FROM workout_table w
    JOIN do_workout_performance_metrics d ON w.workoutId = d.workoutId
    GROUP BY d.workoutId
    ORDER BY COUNT(d.workoutId) DESC
    LIMIT 1
    """)
    suspend fun getFavoriteWorkout(): Workout?

    //TODO: longest workout by time NOT DATE
    //TODO: total time training...
}