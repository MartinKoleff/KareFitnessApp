package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.room.entity.Exercise
import com.koleff.kare_android.data.room.entity.ExerciseDetails
import com.koleff.kare_android.data.room.entity.SetEntity
import com.koleff.kare_android.data.room.entity.relations.ExerciseDetailsExerciseCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseSetCrossRef
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet

@Dao
interface ExerciseSetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSet(setEntity: SetEntity)

    @Update
    suspend fun updateSet(setEntity: SetEntity)

    @Delete
    suspend fun deleteSet(setEntity: SetEntity)
}