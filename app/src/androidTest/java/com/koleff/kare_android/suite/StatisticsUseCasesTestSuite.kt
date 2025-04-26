package com.koleff.kare_android.suite

import com.koleff.kare_android.statistics.ExerciseStatisticsUseCaseTest
import com.koleff.kare_android.statistics.GeneralStatisticsUseCaseTest
import com.koleff.kare_android.statistics.WorkoutStatisticsUseCaseTest
import org.junit.platform.suite.api.SelectClasses
import org.junit.platform.suite.api.SelectPackages
import org.junit.platform.suite.api.Suite
import org.junit.platform.suite.api.SuiteDisplayName

@Suite
@SelectPackages(
    "statistics"
)
@SelectClasses(
    ExerciseStatisticsUseCaseTest::class,
    WorkoutStatisticsUseCaseTest::class,
    GeneralStatisticsUseCaseTest::class
)
@SuiteDisplayName("Statistics Use cases test suite")
class StatisticsUseCasesTestSuite {
}