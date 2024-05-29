package com.koleff.kare_android.suite

import com.koleff.kare_android.ui.view_model.SplashScreenViewModel
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName


@Suite
@SelectPackages(
    "com.koleff.kare_android.workout",
    "com.koleff.kare_android.exercise",
    "do_workout",
    "do_workout_performance_metrics",
    "authentication"
)
@SelectClasses(
   SplashScreenViewModel::class
)
@SuiteDisplayName("View model test suite")
class ViewModelTestSuite {
}