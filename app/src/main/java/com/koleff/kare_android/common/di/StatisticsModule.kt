package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.ExerciseStatisticsDataSource
import com.koleff.kare_android.data.datasource.ExerciseStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsLocalDataSource
import com.koleff.kare_android.data.repository.ExerciseStatisticsRepositoryImpl
import com.koleff.kare_android.data.repository.GeneralStatisticsRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutStatisticsRepositoryImpl
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.StatisticsDao
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.repository.GeneralStatisticsRepository
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.usecases.statistics.ExerciseStatisticsUseCases
import com.koleff.kare_android.domain.usecases.statistics.GeneralStatisticsUseCases
import com.koleff.kare_android.domain.usecases.statistics.GetDatesOfCompletionUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetDistinctWorkoutsCompletedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetExercise1RepMaxUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetExercisePRUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetExerciseTotalRepsPerformedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetExerciseTotalSetsPerformedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetExerciseTotalWeightLiftedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetMostFrequentWorkoutUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetMostTrainedExerciseUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetMostTrainedMuscleGroupUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetStrongestMuscleGroupUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetTotalWeightLiftedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetWorkoutStreakUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetWorkoutTotalRepsPerformedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetWorkoutTotalSetsPerformedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetWorkoutTotalTimesCompletedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetWorkoutTotalWeightLiftedUseCase
import com.koleff.kare_android.domain.usecases.statistics.GetWorkoutsCompletedUseCase
import com.koleff.kare_android.domain.usecases.statistics.StatisticsUseCases
import com.koleff.kare_android.domain.usecases.statistics.WorkoutStatisticsUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StatisticsModule {

    /**
     * API
     */


    /**
     * Data source
     */

    @Provides
    @Singleton
    fun provideGeneralStatisticsDataSource(
        statisticsDao: StatisticsDao,
        doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
        doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao,
        exerciseDao: ExerciseDao,
    ): GeneralStatisticsDataSource {
        return GeneralStatisticsLocalDataSource(
            statisticsDao = statisticsDao,
            doWorkoutPerformanceMetricsDao = doWorkoutPerformanceMetricsDao,
            doWorkoutExerciseSetDao = doWorkoutExerciseSetDao,
            exerciseDao = exerciseDao
        )

        //TODO: add remote data source...
    }

    @Provides
    @Singleton
    fun provideExerciseStatisticsDataSource(
        statisticsDao: StatisticsDao
    ): ExerciseStatisticsDataSource {
        return ExerciseStatisticsLocalDataSource(
            statisticsDao = statisticsDao
        )

        //TODO: add remote data source...
    }

    @Provides
    @Singleton
    fun provideWorkoutStatisticsDataSource(
        statisticsDao: StatisticsDao
    ): WorkoutStatisticsDataSource {
        return WorkoutStatisticsLocalDataSource(
            statisticsDao = statisticsDao
        )

        //TODO: add remote data source...
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideGeneralRepository(generalStatisticsDataSource: GeneralStatisticsDataSource): GeneralStatisticsRepository {
        return GeneralStatisticsRepositoryImpl(generalStatisticsDataSource)
    }

    @Provides
    @Singleton
    fun provideExerciseStatisticsRepository(
        exerciseStatisticsDataSource: ExerciseStatisticsDataSource
    ): ExerciseStatisticsRepository {
        return ExerciseStatisticsRepositoryImpl(exerciseStatisticsDataSource)
    }

    @Provides
    @Singleton
    fun provideWorkoutStatisticsRepository(
        workoutStatisticsDataSource: WorkoutStatisticsDataSource
    ): WorkoutStatisticsRepository {
        return WorkoutStatisticsRepositoryImpl(workoutStatisticsDataSource)
    }

    /**
     * Use cases
     */

    @Provides
    @Singleton
    fun provideStatisticsUseCases(
        generalStatisticsUseCases: GeneralStatisticsUseCases,
        exerciseStatisticsUseCases: ExerciseStatisticsUseCases,
        workoutStatisticsUseCases: WorkoutStatisticsUseCases
    ): StatisticsUseCases {
        return StatisticsUseCases(
            generalStatisticsUseCases = generalStatisticsUseCases,
            exerciseStatisticsUseCases = exerciseStatisticsUseCases,
            workoutStatisticsUseCases = workoutStatisticsUseCases
        )
    }

    @Provides
    @Singleton
    fun provideGeneralStatisticsUseCases(
        generalStatisticsRepository: GeneralStatisticsRepository
    ): GeneralStatisticsUseCases {
        return GeneralStatisticsUseCases(
            getWorkoutsCompletedUseCase = GetWorkoutsCompletedUseCase(generalStatisticsRepository),
            getDistinctWorkoutsCompletedUseCase = GetDistinctWorkoutsCompletedUseCase(
                generalStatisticsRepository
            ),
            getWorkoutStreakUseCase = GetWorkoutStreakUseCase(generalStatisticsRepository),
            getMostFrequentWorkoutUseCase = GetMostFrequentWorkoutUseCase(
                generalStatisticsRepository
            ),
            getMostTrainedMuscleGroupUseCase = GetMostTrainedMuscleGroupUseCase(
                generalStatisticsRepository
            ),
            getMostTrainedExerciseUseCase = GetMostTrainedExerciseUseCase(
                generalStatisticsRepository
            ),
            getTotalWeightLiftedUseCase = GetTotalWeightLiftedUseCase(generalStatisticsRepository),
            getStrongestMuscleGroupUseCase = GetStrongestMuscleGroupUseCase(
                generalStatisticsRepository
            )
        )
    }

    @Provides
    @Singleton
    fun provideExerciseStatisticsUseCases(
        exerciseStatisticsRepository: ExerciseStatisticsRepository,
    ): ExerciseStatisticsUseCases {
        return ExerciseStatisticsUseCases(
            getExercisePRUseCase = GetExercisePRUseCase(exerciseStatisticsRepository),
            getExercise1RepMaxUseCase = GetExercise1RepMaxUseCase(exerciseStatisticsRepository),
            getExerciseTotalRepsPerformedUseCase = GetExerciseTotalRepsPerformedUseCase(
                exerciseStatisticsRepository
            ),
            getExerciseTotalSetsPerformedUseCase = GetExerciseTotalSetsPerformedUseCase(
                exerciseStatisticsRepository
            ),
            getExerciseTotalWeightLiftedUseCase = GetExerciseTotalWeightLiftedUseCase(
                exerciseStatisticsRepository
            )
        )
    }

    @Provides
    @Singleton
    fun provideWorkoutStatisticsUseCases(
        workoutStatisticsRepository: WorkoutStatisticsRepository
    ): WorkoutStatisticsUseCases {
        return WorkoutStatisticsUseCases(
            getWorkoutTotalTimesCompletedUseCase = GetWorkoutTotalTimesCompletedUseCase(
                workoutStatisticsRepository
            ),
            getWorkoutTotalRepsPerformedUseCase = GetWorkoutTotalRepsPerformedUseCase(
                workoutStatisticsRepository
            ),
            getWorkoutTotalSetsPerformedUseCase = GetWorkoutTotalSetsPerformedUseCase(
                workoutStatisticsRepository
            ),
            getWorkoutTotalWeightLiftedUseCase = GetWorkoutTotalWeightLiftedUseCase(
                workoutStatisticsRepository
            ),
            getDatesOfCompletionUseCase = GetDatesOfCompletionUseCase(workoutStatisticsRepository)
        )
    }
}