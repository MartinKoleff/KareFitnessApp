package com.koleff.kare_android.data.model.dto

import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.room.entity.OnboardingData

data class OnboardingDataDto(
    val id: Long = 0,
    val gender: Gender = Gender.NONE,
    val height: Int = -1,
    val age: Int = -1,
    val weight: Int = -1
): KareEntity<OnboardingData> {
    override fun toEntity(): OnboardingData {
        return OnboardingData(
            id = id,
            gender = gender,
            height = height,
            age = age,
            weight = weight
        )
    }
}

