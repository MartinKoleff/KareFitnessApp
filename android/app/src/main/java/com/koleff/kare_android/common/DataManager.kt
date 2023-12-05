package com.koleff.kare_android.common

import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.MuscleGroup

object DataManager {

    val muscleGroupList: List<MuscleGroup> = listOf(
        MuscleGroup("Chest", "", R.drawable.ic_chest),
        MuscleGroup("Back", "", R.drawable.ic_back),
        MuscleGroup("Triceps", "", R.drawable.ic_triceps),
        MuscleGroup("Biceps", "", R.drawable.ic_biceps),
        MuscleGroup("Shoulders", "", R.drawable.ic_shoulder),
        MuscleGroup("Legs", "", R.drawable.ic_legs),
//            MuscleGroup("Cardio", "", R.drawable.ic_faceid),
    )
}