package com.koleff.kare_android.common.manager.data

import androidx.compose.ui.graphics.Color
import com.koleff.kare_android.R
import com.koleff.kare_android.ui.style.OnboardingDataUI

object OnboardingDataGenerator {

    val onboardingData = mutableListOf<OnboardingDataUI>()

    fun generateOnboardingData(mainColor: Color): List<OnboardingDataUI> {
        val backgroundColor = Color(0xFFE4AF19)

        if(onboardingData.isEmpty()) {
            onboardingData.add(
                OnboardingDataUI(
                    R.drawable.motivation_4,
                    "Track Your Progress",
                    "Monitor your fitness journey with detailed stats. Keep track of your workouts, progress, and milestones all in one place.",
                    backgroundColor = backgroundColor,
                    mainColor = mainColor
                )
            )

            onboardingData.add(
                OnboardingDataUI(
                    R.drawable.motivation_5,
                    "Personalized Workouts",
                    "Enjoy tailored workouts that fit your goals and lifestyle. Create routines and get step-by-step guidance for every exercise.",
                    backgroundColor = backgroundColor,
                    mainColor = mainColor
                )
            )

            onboardingData.add(
                OnboardingDataUI(
                    R.drawable.motivation_1,
                    "Achieve Your Goals",
                    "Set your fitness goals and crush them. Whether it's building strength, losing weight, or staying active, we've got you covered!",
                    backgroundColor = backgroundColor,
                    mainColor = mainColor
                )
            )
        }

        return onboardingData
    }
}