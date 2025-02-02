package com.koleff.kare_android.data.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koleff.kare_android.data.KareDto
import com.koleff.kare_android.data.model.dto.Gender
import com.koleff.kare_android.data.model.dto.OnboardingDataDto

@Entity(
    tableName = "onboarding_data_table"
)
data class OnboardingData(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val gender: Gender,
    val age: Int,
    val height: Int,
    val weight: Int,
): KareDto<OnboardingDataDto> {
    override fun toDto(): OnboardingDataDto {
        return OnboardingDataDto(
            id = id,
            gender = gender,
            age = age,
            height = height,
            weight = weight
        )
    }
}
