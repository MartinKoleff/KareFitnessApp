package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Query
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.Workout

@Dao
interface StatisticsDao {

    //Exercise specific
    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE reps = 1 & exerciseId = :exerciseId")
    suspend fun getBestPR(exerciseId: Int): Float?

    @Query("SELECT SUM(reps) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalRepsPerformedForExercise(exerciseId: Int) : Int?

    @Query("SELECT SUM(*) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalSetsPerformedForExercise(exerciseId: Int) : Int?


    //Workout specific stats
    @Query("SELECT * FROM do_workout_performance_metrics WHERE workoutId = :workoutId")
    suspend fun getTotalWorkoutsCompleted(workoutId: Int): Int?

    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getMaxWeightForExercise(exerciseId: Int): Float?

    @Query("SELECT weight, date FROM do_workout_exercise_set WHERE exerciseId = :exerciseId ORDER BY date ASC")
    suspend fun getWeightProgression(exerciseId: Int): List<Pair<Float, String>>?

    @Query("SELECT SUM(reps) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId & workoutId = :workoutId")
    suspend fun getTotalRepsPerformedForExercise(exerciseId: Int, workoutId: Int) : Int?

    @Query("SELECT SUM(*) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId & workoutId = :workoutId")
    suspend fun getTotalSetsPerformedForExercise(exerciseId: Int, workoutId: Int) : Int?

    @Query("SELECT SUM(reps * weight) FROM do_workout_exercise_set WHERE exerciseId = :exerciseId")
    suspend fun getTotalWeightLiftedForExercise(exerciseId: Int): Float?

    @Query("SELECT SUM(reps * weight) FROM do_workout_exercise_set WHERE workoutId = :workoutId")
    suspend fun getTotalWeightLiftedForWorkout(workoutId: Int): Float?


    //General stats
    @Query("SELECT * FROM do_workout_performance_metrics")
    suspend fun getTotalWorkoutsCompleted(): Int?

    @Query("SELECT DISTINCT COUNT(exerciseId) FROM do_workout_exercise_set")
    suspend fun getTotalExercisesCount(): Int?

    @Query("SELECT * FROM do_workout_exercise_set GROUP BY exerciseId ORDER BY COUNT(exerciseId) DESC LIMIT 1.")
    suspend fun getMostPerformedExercise(): Exercise?

    @Query("SELECT * FROM do_workout_exercise_set JOIN workout_table w ON do_workout_exercise_set.workoutId = w.workoutId GROUP BY w.muscleGroup ORDER BY COUNT(w.muscleGroup) DESC LIMIT 1")
    suspend fun getFavoriteMuscleGroup(): MuscleGroup?

    @Query("SELECT * FROM do_workout_exercise_set")
    suspend fun getTotalSetsCount(): Int?

    @Query("SELECT SUM(reps) FROM do_workout_exercise_set")
    suspend fun getTotalRepsCount(): Int?

    @Query("SELECT SUM(reps * weight) FROM do_workout_exercise_set")
    suspend fun getTotalWeightLifted(): Float?

    @Query("SELECT e.muscleGroup, MAX(weight) FROM do_workout_exercise_set d INNER JOIN exercise_table e ON e.exerciseId = d.exerciseId GROUP BY muscleGroup")
    suspend fun getMaxWeightPerMuscleGroup(): Pair<MuscleGroup, Float>?

    @Query("SELECT MAX(weight) FROM do_workout_exercise_set WHERE reps = 1")
    suspend fun getBestPR(): Float?

    @Query("SELECT MAX(*) FROM do_workout_performance_metrics LIMIT 1") //TODO: asc or desc?
    suspend fun getFavoriteWorkout(): Workout?

    //TODOя: longest workout by time NOT DATE
    //TODO: total time training...
}