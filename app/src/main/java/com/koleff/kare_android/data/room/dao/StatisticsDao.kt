package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.MuscleGroupMaxWeight
import com.koleff.kare_android.data.model.dto.WeightProgression
import com.koleff.kare_android.data.room.entity.ExerciseWithSets
import com.koleff.kare_android.data.room.entity.Workout

@Dao
interface StatisticsDao {

    //Exercise specific
    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE reps = 1 & exerciseId = :exerciseId")
    suspend fun getBestPR(exerciseId: Int): Float?

    @Query("SELECT COUNT(reps) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalRepsPerformedForExercise(exerciseId: Int): Int?

    @Query("SELECT COUNT(*) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalSetsPerformedForExercise(exerciseId: Int): Int?


    //Workout specific stats
    @Query("SELECT COUNT(*) FROM do_workout_performance_metrics WHERE workoutId = :workoutId")
    suspend fun getTotalWorkoutsCompleted(workoutId: Int): Int?

    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getMaxWeightForExercise(exerciseId: Int): Float?

    @Query("SELECT weight, date FROM do_workout_exercise_set WHERE exerciseId = :exerciseId ORDER BY date ASC")
    suspend fun getWeightProgression(exerciseId: Int): List<WeightProgression>?

    @Query("SELECT COUNT(reps) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId & workoutId = :workoutId")
    suspend fun getTotalRepsPerformedForExercise(exerciseId: Int, workoutId: Int): Int?

    @Query("SELECT COUNT(*) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId & workoutId = :workoutId")
    suspend fun getTotalSetsPerformedForExercise(exerciseId: Int, workoutId: Int): Int?

    @Query("SELECT COUNT(reps * weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalWeightLiftedForExercise(exerciseId: Int): Float?

    @Query("SELECT COUNT(reps * weight) FROM do_workout_exercise_set WHERE workoutId = :workoutId")
    suspend fun getTotalWeightLiftedForWorkout(workoutId: Int): Float?


    //General stats
    @Query("SELECT COUNT(*) FROM do_workout_performance_metrics")
    suspend fun getTotalWorkoutsCompleted(): Int?

    @Query("SELECT DISTINCT COUNT(exerciseId) FROM do_workout_exercise_set")
    suspend fun getTotalExercisesCount(): Int?

    @Query("""
    SELECT e.*, d.*
    FROM do_workout_exercise_set d
    JOIN exercise_table e ON d.exerciseId = e.exerciseId
    GROUP BY d.exerciseId
    ORDER BY COUNT(d.exerciseId) DESC
    LIMIT 1
    """)
    suspend fun getMostPerformedExercise(): ExerciseWithSets?

    @Query("SELECT muscleGroup FROM do_workout_exercise_set JOIN workout_table w ON do_workout_exercise_set.workoutId = w.workoutId GROUP BY w.muscleGroup ORDER BY COUNT(w.muscleGroup) DESC LIMIT 1")
    suspend fun getFavoriteMuscleGroup(): MuscleGroup?

    @Query("SELECT COUNT(*) FROM do_workout_exercise_set")
    suspend fun getTotalSetsCount(): Int?

    @Query("SELECT COUNT(reps) FROM do_workout_exercise_set")
    suspend fun getTotalRepsCount(): Int?

    @Query("SELECT COUNT(reps * weight) FROM do_workout_exercise_set")
    suspend fun getTotalWeightLifted(): Float?

    @Query("""
    SELECT e.muscleGroup AS muscleGroup, MAX(d.weight) AS maxWeight
    FROM do_workout_exercise_set d
    INNER JOIN exercise_table e ON e.exerciseId = d.exerciseId
    GROUP BY e.muscleGroup
    ORDER BY COUNT(d.weight) DESC
    LIMIT 1
    """)
    suspend fun getMaxWeightPerMuscleGroup(): MuscleGroupMaxWeight?

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