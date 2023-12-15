package com.koleff.kare_android.ui.compose.banners

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun AddWorkoutBanner(
    title: String = "Want to add new workout?",
    description: String = "Click to create a workout.",
    onClick: () -> Unit
) {
    WorkoutFooterBanner(title = title, description = description) {
        onClick()
    }
}

@Preview
@Composable
fun AddWorkoutBannerPreview() {
    AddWorkoutBanner() {

    }
}