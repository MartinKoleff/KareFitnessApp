package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.WorkoutDetails
import com.koleff.kare_android.data.room.entity.relations.WorkoutDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.WorkoutDetailsWithExercises
import com.koleff.kare_android.data.room.entity.WorkoutWithConfig

typealias WorkoutDetailsId = Long

@Dao
interface WorkoutDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutDetails(workoutDetails: WorkoutDetails): WorkoutDetailsId

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllWorkoutDetails(workoutDetailsList: List<WorkoutDetails>)

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertWorkoutDetailsExerciseCrossRef(crossRef: WorkoutDetailsExerciseCrossRef)
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertAllWorkoutDetailsExerciseCrossRef(crossRefs: List<WorkoutDetailsExerciseCrossRef>)
//
//    @Delete
//    suspend fun deleteWorkoutDetailsExerciseCrossRef(crossRef: WorkoutDetailsExerciseCrossRef)
//
//    @Delete
//    suspend fun deleteAllWorkoutDetailsExerciseCrossRef(crossRefs: List<WorkoutDetailsExerciseCrossRef>)

    @Query("DELETE FROM workout_details_table WHERE workoutDetailsId = :workoutId")
    suspend fun deleteWorkoutDetails(workoutId: Int)

    @Update
    suspend fun updateWorkoutDetails(workout: WorkoutDetails)

    @Transaction
    @Query("UPDATE workout_details_table SET isFavorite = 1 WHERE workoutDetailsId = :workoutId")
    suspend fun favoriteWorkoutDetailsById(workoutId: Int)

    @Transaction
    @Query("UPDATE workout_details_table SET isFavorite = 0 WHERE workoutDetailsId = :workoutId")
    suspend fun unfavoriteWorkoutDetailsById(workoutId: Int)

    @Transaction
    @Query("SELECT * FROM workout_details_table ORDER BY workoutDetailsId")
    fun getWorkoutDetailsOrderedById(): List<WorkoutDetailsWithExercises>

    @Transaction
    @Query("SELECT * FROM workout_details_table WHERE isFavorite = 1") //true = 1, false = 0
    fun getWorkoutByIsFavorite(): WorkoutDetailsWithExercises?

    @Transaction
    @Query("SELECT * FROM workout_details_table w WHERE workoutDetailsId = :workoutId")
    fun getWorkoutDetailsById(workoutId: Int): WorkoutDetailsWithExercises?
}