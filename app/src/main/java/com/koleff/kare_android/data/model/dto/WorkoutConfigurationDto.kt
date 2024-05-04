package com.koleff.kare_android.data.model.dto

import android.os.Parcelable
import com.koleff.kare_android.data.KareEntity
import com.koleff.kare_android.data.room.entity.WorkoutConfiguration
import kotlinx.parcelize.Parcelize

@Parcelize
data class WorkoutConfigurationDto(
    val workoutId: Int = -1,
//    var exercises: List<ExerciseConfigurationDto> = emptyList(),
    var cooldownTime: ExerciseTime = ExerciseTime(0, 0, 10)
): Parcelable, KareEntity<WorkoutConfiguration>{
    override fun toEntity(): WorkoutConfiguration {
        return WorkoutConfiguration(
            workoutId = workoutId,
            cooldownTime = cooldownTime
        )
    }
}