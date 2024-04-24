package com.koleff.kare_android

import com.koleff.kare_android.authentication.AuthenticationTest
import com.koleff.kare_android.do_workout.DoWorkoutUseCasesUnitTest
import com.koleff.kare_android.do_workout_performance_metrics.DoWorkoutPerformanceMetricsUseCasesUnitTest
import com.koleff.kare_android.exercise.ExerciseUseCasesUnitTest
import com.koleff.kare_android.workout.WorkoutUseCasesUnitTest
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite

@Suite
//@SelectClasses(
//    WorkoutUseCasesUnitTest::class,
//    ExerciseUseCasesUnitTest::class,
//    DoWorkoutUseCasesUnitTest::class,
//    DoWorkoutPerformanceMetricsUseCasesUnitTest::class,
//    AuthenticationTest::class
//)
@SelectPackages(
    "com.koleff.kare_android.workout",
    "com.koleff.kare_android.exercise",
    "com.koleff.kare_android.do_workout",
    "com.koleff.kare_android.do_workout_performance_metrics",
    "com.koleff.kare_android.authentication"
)
class TestAllSelectPackage {
}