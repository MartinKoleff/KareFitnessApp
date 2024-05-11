package com.koleff.kare_android.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

data class ExtendedColorScheme(
    val doWorkoutColors: DoWorkoutColors,
    val workoutBannerColors: WorkoutBannerColors,
    val detailsScreenBackgroundGradient: List<Color>,
)

data class DoWorkoutColors(
    val elapsedLineColor: Color = Color.Yellow,
    val setsTextColor: Color = Color.Yellow,
)

data class WorkoutBannerColors(
    val deleteButtonColor: Color = Color.Red,
    val selectButtonColor: Color = Color.Green,
    val editButtonColor: Color = Color.Blue,
)

//Override in KareTheme
val LocalExtendedColorScheme = compositionLocalOf {
    ExtendedColorScheme(
        doWorkoutColors = DoWorkoutColors(),
        workoutBannerColors = WorkoutBannerColors(),
        detailsScreenBackgroundGradient = listOf()
    )
}
