package com.koleff.kare_android.suite

import com.koleff.kare_android.workout.WorkoutUseCasesUnitTest
import org.junit.platform.suite.api.IncludePackages
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
@SelectPackages(
    "com.koleff.kare_android.workout",
    "com.koleff.kare_android.exercise",
    "do_workout",
    "do_workout_performance_metrics",
    "authentication"
)
class UseCasesTestSuite {
}