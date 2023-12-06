package com.koleff.kare_android.common

import com.koleff.kare_android.R
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.MuscleGroupUI

object DataManager {


    val muscleGroupList: List<MuscleGroupUI> = listOf(
        MuscleGroupUI("Chest", "", R.drawable.ic_chest),
        MuscleGroupUI("Back", "", R.drawable.ic_back),
        MuscleGroupUI("Triceps", "", R.drawable.ic_triceps),
        MuscleGroupUI("Biceps", "", R.drawable.ic_biceps),
        MuscleGroupUI("Shoulders", "", R.drawable.ic_shoulder),
        MuscleGroupUI("Legs", "", R.drawable.ic_legs),
//            MuscleGroup("Cardio", "", R.drawable.ic_faceid),
    ) //TODO: Move to viewModel
}