package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.dto.ExerciseTime

data class TimerState (
    val time: ExerciseTime = ExerciseTime(0, 0, 0)
)