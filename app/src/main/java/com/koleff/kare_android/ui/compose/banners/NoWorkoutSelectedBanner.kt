package com.koleff.kare_android.ui.compose.banners

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

@Composable
fun NoWorkoutSelectedBanner(
    title: String = "No workout is currently selected!",
    description: String = "Click to select a workout.",
    onClick: () -> Unit
) {
    WorkoutFooterBanner(title = title, description = description) {
        onClick()
    }
}

@Preview
@PreviewLightDark
@Composable
fun NoWorkoutSelectedBannerPreview() {
    NoWorkoutSelectedBanner() {

    }
}