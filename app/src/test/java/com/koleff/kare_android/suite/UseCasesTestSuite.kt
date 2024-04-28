package com.koleff.kare_android.suite

import com.koleff.kare_android.authentication.AuthenticationTest
import com.koleff.kare_android.do_workout.DoWorkoutUseCasesUnitTest
import com.koleff.kare_android.do_workout_performance_metrics.DoWorkoutPerformanceMetricsUseCasesUnitTest
import com.koleff.kare_android.exercise.ExerciseUseCasesUnitTest
import com.koleff.kare_android.workout.WorkoutUseCasesUnitTest
import org.junit.platform.suite.api.IncludePackages
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName
import org.junit.runner.RunWith

@Suite
//@SelectPackages(
//    "com.koleff.kare_android.workout",
//    "com.koleff.kare_android.exercise",
//    "do_workout",
//    "do_workout_performance_metrics",
//    "authentication"
//)
@SelectClasses(
    WorkoutUseCasesUnitTest::class,
    ExerciseUseCasesUnitTest::class,
    DoWorkoutUseCasesUnitTest::class,
    DoWorkoutPerformanceMetricsUseCasesUnitTest::class,
    AuthenticationTest::class
)
@SuiteDisplayName("Use cases test suite")
class UseCasesTestSuite {
}