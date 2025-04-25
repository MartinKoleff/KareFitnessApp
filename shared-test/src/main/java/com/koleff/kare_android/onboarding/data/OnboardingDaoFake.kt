package com.koleff.kare_android.onboarding.data

import com.koleff.kare_android.data.room.dao.OnboardingDao
import com.koleff.kare_android.data.room.entity.ExerciseWithSets
import com.koleff.kare_android.data.room.entity.OnboardingData
import com.koleff.kare_android.utils.FakeDao

class OnboardingDaoFake : OnboardingDao, FakeDao {

    private val onboardingDB = mutableListOf<OnboardingData>()

    private val isInternalLogging = false

    companion object {
        private const val TAG = "OnboardingDaoFake"
    }

    override fun getOnboardingDataById(id: Long): OnboardingData? {
        val index = onboardingDB.indexOfFirst { it.id == id }

        //OnboardingData found
        if (index != -1) {

            val onboardingData = onboardingDB[index]
            return onboardingData
        } else {

            //No OnboardingData found
            return null
        }
    }

    override suspend fun saveOnboardingData(onboardingData: OnboardingData): Long {
        if(onboardingData.id == -1L) {
           val updateOnboardingData = onboardingData.copy(id = onboardingDB.size.toLong())
            onboardingDB.add(updateOnboardingData)
            return updateOnboardingData.id
        }else {
            onboardingDB.add(onboardingData)
            return onboardingData.id
        }
    }

    override suspend fun updateOnboardingData(onboardingData: OnboardingData) {
        val index = onboardingDB.indexOfFirst { it.id == onboardingData.id }

        //OnboardingData found
        if (index != -1) {

            onboardingDB[index] = onboardingData
        } else {

            //No OnboardingData found
        }
    }

    override suspend fun deleteOnboardingData(onboardingData: OnboardingData) {
        onboardingDB.removeAll { it.id == onboardingData.id }
    }

    override fun clearDB() {
        onboardingDB.clear()
    }
}