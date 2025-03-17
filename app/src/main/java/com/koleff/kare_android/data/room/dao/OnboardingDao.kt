package com.koleff.kare_android.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.koleff.kare_android.data.room.entity.OnboardingData

@Dao
interface OnboardingDao {

    @Query("SELECT * FROM onboarding_data_table WHERE id = :id")
    fun getOnboardingDataById(id: Long): OnboardingData?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOnboardingData(onboardingData: OnboardingData)

    @Update
    suspend fun updateOnboardingData(onboardingData: OnboardingData)

    @Delete
    suspend fun deleteOnboardingData(onboardingData: OnboardingData)
}