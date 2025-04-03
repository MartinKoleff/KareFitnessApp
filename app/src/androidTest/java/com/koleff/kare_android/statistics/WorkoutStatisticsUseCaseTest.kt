package com.koleff.kare_android.statistics

import RepeatRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.datasource.ExerciseStatisticsDataSource
import com.koleff.kare_android.data.datasource.ExerciseStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsLocalDataSource
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.repository.DoWorkoutPerformanceMetricsRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseStatisticsRepositoryImpl
import com.koleff.kare_android.data.repository.GeneralStatisticsRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutStatisticsRepositoryImpl
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.StatisticsDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.database.KareDatabase
import com.koleff.kare_android.data.room.manager.ExerciseDBManagerV2
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.repository.ExerciseStatisticsRepository
import com.koleff.kare_android.domain.repository.GeneralStatisticsRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.repository.WorkoutStatisticsRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.AddMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutPerformanceMetricsUseCases
import com.koleff.kare_android.domain.usecases.FavoriteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.FindDuplicateExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetAllDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetFavoriteWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SaveAllDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.SubmitMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.UnfavoriteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.UpdateDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.usecases.statistics.ExerciseStatisticsUseCases
import com.koleff.kare_android.domain.usecases.statistics.GeneralStatisticsUseCases
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
import com.koleff.kare_android.utils.Repeat
import com.koleff.kare_android.utils.TestLogger
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WorkoutStatisticsUseCaseTest {

    companion object {
        private val TAG = "WorkoutStatisticsUseCaseTest"
    }

    private lateinit var exerciseDBManager: ExerciseDBManagerV2

    private lateinit var exerciseSetDao: ExerciseSetDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var exerciseDetailsDao: ExerciseDetailsDao
    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutDetailsDao: WorkoutDetailsDao
    private lateinit var workoutConfigurationDao: WorkoutConfigurationDao
    private lateinit var doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao
    private lateinit var doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao

    private lateinit var workoutRepository: WorkoutRepository
    private lateinit var doWorkoutPerformanceMetricsRepository: DoWorkoutPerformanceMetricsRepository
    private lateinit var generalStatisticsRepository: GeneralStatisticsRepository
    private lateinit var exerciseStatisticsRepository: ExerciseStatisticsRepository
    private lateinit var workoutStatisticsRepository: WorkoutStatisticsRepository

    private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
    private lateinit var doWorkoutPerformanceMetricsFakeDataSource: DoWorkoutPerformanceMetricsFakeDataSource
    private lateinit var generalStatisticsDataSource: GeneralStatisticsDataSource
    private lateinit var exerciseStatisticsDataSource: ExerciseStatisticsDataSource
    private lateinit var workoutStatisticsDataSource: WorkoutStatisticsDataSource

    private lateinit var doWorkoutPerformanceMetricsUseCases: DoWorkoutPerformanceMetricsUseCases
    private lateinit var workoutUseCases: WorkoutUseCases
    private lateinit var statisticsUseCases: StatisticsUseCases

    private lateinit var statisticsDao: StatisticsDao
    private lateinit var kareDatabase: KareDatabase

    lateinit var workout1: WorkoutDetailsDto
    lateinit var workout2: WorkoutDetailsDto
    lateinit var performanceMetrics1: DoWorkoutPerformanceMetricsDto
    lateinit var performanceMetrics2: DoWorkoutPerformanceMetricsDto

    private val isLogging = true
    private lateinit var logger: TestLogger

    @Before
    fun setup() = runTest {
        logger = TestLogger(isLogging)

        //DAOs
        kareDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            KareDatabase::class.java
        ).allowMainThreadQueries().build()

        statisticsDao = kareDatabase.statisticsDao
        workoutDao = kareDatabase.workoutDao
        exerciseDao = kareDatabase.exerciseDao
        exerciseDetailsDao = kareDatabase.exerciseDetailsDao
        workoutDetailsDao = kareDatabase.workoutDetailsDao
        exerciseSetDao = kareDatabase.exerciseSetDao
        workoutConfigurationDao = kareDatabase.workoutConfigurationDao
        doWorkoutPerformanceMetricsDao = kareDatabase.doWorkoutPerformanceMetricsDao
        doWorkoutExerciseSetDao = kareDatabase.doWorkoutExerciseSetDao

        logger.i(TAG, "DB created!")

        //Workout
        workoutFakeDataSource = WorkoutFakeDataSource(
            workoutDao = workoutDao,
            exerciseDao = exerciseDao,
            workoutDetailsDao = workoutDetailsDao,
            exerciseSetDao = exerciseSetDao,
            workoutConfigurationDao = workoutConfigurationDao
        )

        workoutRepository =
            WorkoutRepositoryImpl(workoutFakeDataSource)

        workoutUseCases = WorkoutUseCases(
            getWorkoutDetailsUseCase = GetWorkoutsDetailsUseCase(workoutRepository),
            getAllWorkoutsUseCase = GetAllWorkoutsUseCase(workoutRepository),
            getAllWorkoutDetailsUseCase = GetAllWorkoutDetailsUseCase(workoutRepository),
            getWorkoutUseCase = GetWorkoutUseCase(workoutRepository),
            updateWorkoutUseCase = UpdateWorkoutUseCase(workoutRepository),
            updateWorkoutDetailsUseCase = UpdateWorkoutDetailsUseCase(workoutRepository),
            onSearchWorkoutUseCase = OnSearchWorkoutUseCase(),
            deleteExerciseUseCase = DeleteExerciseUseCase(workoutRepository),
            deleteMultipleExercisesUseCase = DeleteMultipleExercisesUseCase(
                workoutRepository
            ),
            addExerciseUseCase = AddExerciseUseCase(workoutRepository),
            addMultipleExercisesUseCase = AddMultipleExercisesUseCase(workoutRepository),
            submitExerciseUseCase = SubmitExerciseUseCase(workoutRepository),
            submitMultipleExercisesUseCase = SubmitMultipleExercisesUseCase(
                workoutRepository
            ),
            findDuplicateExercisesUseCase = FindDuplicateExercisesUseCase(
                workoutRepository
            ),
            deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
            favoriteWorkoutUseCase = FavoriteWorkoutUseCase(workoutRepository),
            unfavoriteWorkoutUseCase = UnfavoriteWorkoutUseCase(workoutRepository),
            getFavoriteWorkoutsUseCase = GetFavoriteWorkoutsUseCase(workoutRepository),
            createNewWorkoutUseCase = CreateNewWorkoutUseCase(workoutRepository),
            createCustomWorkoutUseCase = CreateCustomWorkoutUseCase(workoutRepository),
            createCustomWorkoutDetailsUseCase = CreateCustomWorkoutDetailsUseCase(
                workoutRepository
            ),
            getWorkoutConfigurationUseCase = GetWorkoutConfigurationUseCase(
                workoutRepository
            ),
            createWorkoutConfigurationUseCase = CreateWorkoutConfigurationUseCase(
                workoutRepository
            ),
            updateWorkoutConfigurationUseCase = UpdateWorkoutConfigurationUseCase(
                workoutRepository
            ),
            deleteWorkoutConfigurationUseCase = DeleteWorkoutConfigurationUseCase(
                workoutRepository
            )
        )

        //DoWorkout
        doWorkoutPerformanceMetricsFakeDataSource = DoWorkoutPerformanceMetricsFakeDataSource(
            doWorkoutPerformanceMetricsDao = doWorkoutPerformanceMetricsDao,
            doWorkoutExerciseSetDao = doWorkoutExerciseSetDao,
            workoutDao = workoutDao
        )

        doWorkoutPerformanceMetricsRepository =
            DoWorkoutPerformanceMetricsRepositoryImpl(doWorkoutPerformanceMetricsFakeDataSource)
        doWorkoutPerformanceMetricsUseCases = DoWorkoutPerformanceMetricsUseCases(
            deleteDoWorkoutPerformanceMetricsUseCase = DeleteDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            updateDoWorkoutPerformanceMetricsUseCase = UpdateDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            getAllDoWorkoutPerformanceMetricsUseCase = GetAllDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            getDoWorkoutPerformanceMetricsUseCase = GetDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            saveAllDoWorkoutExerciseSetUseCase = SaveAllDoWorkoutExerciseSetUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            saveDoWorkoutExerciseSetUseCase = SaveDoWorkoutExerciseSetUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            saveDoWorkoutPerformanceMetricsUseCase = SaveDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            )
        )

        //Statistics
        generalStatisticsDataSource = GeneralStatisticsLocalDataSource(
            statisticsDao = statisticsDao,
            doWorkoutPerformanceMetricsDao = doWorkoutPerformanceMetricsDao,
            doWorkoutExerciseSetDao = doWorkoutExerciseSetDao,
            exerciseDao = exerciseDao
        )
        exerciseStatisticsDataSource = ExerciseStatisticsLocalDataSource(
            statisticsDao = statisticsDao
        )
        workoutStatisticsDataSource = WorkoutStatisticsLocalDataSource(
            statisticsDao = statisticsDao
        )

        generalStatisticsRepository = GeneralStatisticsRepositoryImpl(
            generalStatisticsDataSource = generalStatisticsDataSource
        )
        exerciseStatisticsRepository = ExerciseStatisticsRepositoryImpl(
            exerciseStatisticsDataSource = exerciseStatisticsDataSource
        )
        workoutStatisticsRepository = WorkoutStatisticsRepositoryImpl(
            workoutStatisticsDataSource = workoutStatisticsDataSource
        )

        statisticsUseCases = StatisticsUseCases(
            exerciseStatisticsUseCases = ExerciseStatisticsUseCases(
                getExercisePRUseCase = GetExercisePRUseCase(exerciseStatisticsRepository = exerciseStatisticsRepository),
                getExercise1RepMaxUseCase = GetExercise1RepMaxUseCase(exerciseStatisticsRepository = exerciseStatisticsRepository),
                getExerciseTotalWeightLiftedUseCase = GetExerciseTotalWeightLiftedUseCase(
                    exerciseStatisticsRepository = exerciseStatisticsRepository
                ),
                getExerciseTotalRepsPerformedUseCase = GetExerciseTotalRepsPerformedUseCase(
                    exerciseStatisticsRepository = exerciseStatisticsRepository
                ),
                getExerciseTotalSetsPerformedUseCase = GetExerciseTotalSetsPerformedUseCase(
                    exerciseStatisticsRepository = exerciseStatisticsRepository
                )
            ),
            workoutStatisticsUseCases = WorkoutStatisticsUseCases(
                getWorkoutTotalTimesCompletedUseCase = GetWorkoutTotalTimesCompletedUseCase(
                    workoutStatisticsRepository = workoutStatisticsRepository
                ),
                getWorkoutTotalWeightLiftedUseCase = GetWorkoutTotalWeightLiftedUseCase(
                    workoutStatisticsRepository = workoutStatisticsRepository
                ),
                getWorkoutTotalRepsPerformedUseCase = GetWorkoutTotalRepsPerformedUseCase(
                    workoutStatisticsRepository = workoutStatisticsRepository
                ),
                getWorkoutTotalSetsPerformedUseCase = GetWorkoutTotalSetsPerformedUseCase(
                    workoutStatisticsRepository = workoutStatisticsRepository
                )
            ),
            generalStatisticsUseCases = GeneralStatisticsUseCases(
                getWorkoutsCompletedUseCase = GetWorkoutsCompletedUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getDistinctWorkoutsCompletedUseCase = GetDistinctWorkoutsCompletedUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getWorkoutStreakUseCase = GetWorkoutStreakUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getMostFrequentWorkoutUseCase = GetMostFrequentWorkoutUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getMostTrainedMuscleGroupUseCase = GetMostTrainedMuscleGroupUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getMostTrainedExerciseUseCase = GetMostTrainedExerciseUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getTotalWeightLiftedUseCase = GetTotalWeightLiftedUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                ),
                getStrongestMuscleGroupUseCase = GetStrongestMuscleGroupUseCase(
                    generalStatisticsRepository = generalStatisticsRepository
                )
            )
        )

        //Initialize DB
        exerciseDBManager = ExerciseDBManagerV2(
            exerciseSetDao = exerciseSetDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseDao = exerciseDao,
            workoutDao = workoutDao,
            workoutDetailsDao = workoutDetailsDao,
            hasInitializedDB = false
        )
    }

    @Before
    fun initializeDB() = runTest {
        exerciseDBManager.initializeExerciseTable {
            Logger.getLogger().i("DB initialized successfully!")
        }
        Logger.getLogger().i(exerciseDao.getAllExercises().toString())

        //Workout DB
        workout1 =
            MockupDataGeneratorV2.generateWorkoutDetails(
                enableSetIdGeneration = true,
                workoutId = 1
            )
        Logger.getLogger().i("Workout 1: $workout1")
        workout2 =
            MockupDataGeneratorV2.generateWorkoutDetails(
                enableSetIdGeneration = true,
                workoutId = 2
            )
        Logger.getLogger().i("Workout 1: $workout2")

        workoutUseCases.createCustomWorkoutDetailsUseCase(workout1).toList()
        workoutUseCases.createCustomWorkoutDetailsUseCase(workout2).toList()

        val getWorkoutDetailsState1 =
            workoutUseCases.getWorkoutDetailsUseCase(workoutId = workout1.workoutId).toList()
        val getWorkoutDetailsState2 =
            workoutUseCases.getWorkoutDetailsUseCase(workoutId = workout2.workoutId).toList()
        Logger.getLogger().i("Get workout details for workout 1: ${getWorkoutDetailsState1[1]}")
        Logger.getLogger().i("Get workout details for workout 2: ${getWorkoutDetailsState2[1]}")

        //Do workout performance metrics and do workout exercise sets DB
        performanceMetrics1 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = workout1.workoutId,
            workout = workout1.toWorkout(),
            exercises = workout1.exercises
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics1
        ).toList()
        Logger.getLogger().i("Do workout performance metrics 1: ${performanceMetrics1}")

        val getDoWorkoutPerformanceMetrics1 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = workout1.workoutId
            ).toList()
        Logger.getLogger().i(
            "Get do workout performance metrics 1: ${getDoWorkoutPerformanceMetrics1[1].doWorkoutPerformanceMetrics}"
        )

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics1.doWorkoutExerciseSets
        ).toList()

        Logger.getLogger().i(
            "Get exercise data for workoutId: ${performanceMetrics1.doWorkoutExerciseSets[0].workoutId} and exerciseId: ${performanceMetrics1.doWorkoutExerciseSets[0].exerciseId}: ${
                exerciseDao.getExerciseWithSets(
                    workoutId = performanceMetrics1.doWorkoutExerciseSets[0].workoutId,
                    exerciseId = performanceMetrics1.doWorkoutExerciseSets[0].exerciseId
                )
            }"
        )

        performanceMetrics2 =
            MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
                id = workout2.workoutId,
                workout = workout2.toWorkout(),
                exercises = workout2.exercises
            )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics2
        ).toList()
        Logger.getLogger().i("Do workout performance metrics 2: ${performanceMetrics2}")

        val getDoWorkoutPerformanceMetrics2 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = workout2.workoutId
            ).toList()

        Logger.getLogger().i(
            "Get do workout performance metrics 2: ${getDoWorkoutPerformanceMetrics2[1].doWorkoutPerformanceMetrics}"
        )

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics2.doWorkoutExerciseSets
        ).toList()

        val doWorkoutExerciseSets = doWorkoutExerciseSetDao.findSetsByWorkoutId(workout1.workoutId)
        Logger.getLogger()
            .i("Do workout exercise sets for workout ${workout1.workoutId}: $doWorkoutExerciseSets")

        val doWorkoutExerciseSets2 = doWorkoutExerciseSetDao.findSetsByWorkoutId(workout2.workoutId)
        Logger.getLogger()
            .i("Do workout exercise sets for workout ${workout2.workoutId}: $doWorkoutExerciseSets2")
    }

    @AfterAll
    fun tearDown() {
        kareDatabase.close()
    }

    @get:Rule
    val repeatedRule = RepeatRule()

    @Test
    @Repeat(50)
    fun testGetWorkoutTotalTimesCompletedUseCase() = runTest {

        //Test if DB is initialized
        val getWorkoutTotalTimesCompletedState =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalTimesCompletedUseCase(
                workout1.workoutId
            ).toList()

        logger.i(TAG, "Get workout total times completed for workout 1 -> isLoading state raised.")
        assertTrue { getWorkoutTotalTimesCompletedState[0].isLoading }

        logger.i(
            TAG,
            "Get workout total times completed for workout 1 -> isSuccessful state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState[1].isSuccessful }

        logger.i(TAG, "Assert Get workout total times completed for workout 1 is 1.")
        assertTrue { getWorkoutTotalTimesCompletedState[1].totalTimesCompleted == 1 }

        //Increase the total times completed
        val performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = workout1.exercises
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics for workout 1: $performanceMetrics3")

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        val getWorkoutTotalTimesCompletedState2 =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalTimesCompletedUseCase(
                workout1.workoutId
            ).toList()

        logger.i(
            TAG,
            "Get workout total times completed for workout 1 after increase -> isLoading state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState2[0].isLoading }

        logger.i(
            TAG,
            "Get workout total times completed for workout 1 after increase -> isSuccessful state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState2[1].isSuccessful }

        logger.i(
            TAG,
            "Assert Get workout total times completed for workout 1 is 2 after the increase."
        )
        assertTrue { getWorkoutTotalTimesCompletedState2[1].totalTimesCompleted == 2 }

        //Decrease
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getWorkoutTotalTimesCompletedState3 =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalTimesCompletedUseCase(
                workout1.workoutId
            ).toList()

        logger.i(
            TAG,
            "Get workout total times completed for workout 1 after decrease -> isLoading state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState3[0].isLoading }

        logger.i(
            TAG,
            "Get workout total times completed for workout 1 after decrease -> isSuccessful state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState3[1].isSuccessful }

        logger.i(
            TAG,
            "Assert Get workout total times completed for workout 1 is 1 after the decrease."
        )
        assertTrue { getWorkoutTotalTimesCompletedState3[1].totalTimesCompleted == 1 }

        //Test with invalid id -1
        val getWorkoutTotalTimesCompletedState4 =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalTimesCompletedUseCase(
                -1
            ).toList()

        logger.i(
            TAG,
            "Get workout total times completed for invalid workout -> isLoading state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState4[0].isLoading }

        logger.i(
            TAG,
            "Get workout total times completed for invalid workout -> isSuccessful state raised."
        )
        assertTrue { getWorkoutTotalTimesCompletedState4[1].isSuccessful }

        logger.i(TAG, "Assert Get workout total times completed for invalid workout is 0.")
        assertTrue { getWorkoutTotalTimesCompletedState4[1].totalTimesCompleted == 0 }
    }

    @Test
    @Repeat(50)
    fun testGetWorkoutTotalRepsPerformedUseCase() = runTest {

        //Generate test exercise
        val selectedExercise = workout1.exercises.first()
        val performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(selectedExercise)
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics 3 for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics 3 for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        val repsSum = performanceMetrics3.doWorkoutExerciseSets.sumOf { it.reps } +
                performanceMetrics1.doWorkoutExerciseSets
                    .filter { it.exerciseId == selectedExercise.exerciseId }.sumOf { it.reps }
        logger.i(
            TAG,
            "Total reps for exercise with id ${selectedExercise.exerciseId}: $repsSum"
        )
        val getExerciseTotalRepsState =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalRepsPerformedUseCase(
                workout1.workoutId, selectedExercise.exerciseId
            ).toList()

        logger.i(
            TAG,
            "Get exercise with id ${selectedExercise.exerciseId} total reps for workout 1-> isLoading state raised."
        )
        assertTrue { getExerciseTotalRepsState[0].isLoading }

        logger.i(
            TAG,
            "Get exercise with id ${selectedExercise.exerciseId} total reps for workout 1-> isSuccessful state raised."
        )
        assertTrue { getExerciseTotalRepsState[1].isSuccessful }

        logger.i(TAG, "Get exercise total reps -> data: ${getExerciseTotalRepsState[1]}")
        logger.i(TAG, "Assert statistics total reps is the same as the calculated one.")
        assertTrue { getExerciseTotalRepsState[1].totalReps == repsSum }

        //Test with invalid exercise
        val getExerciseTotalRepsState2 =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalRepsPerformedUseCase(
                -1, -1
            ).toList()

        logger.i(
            TAG,
            "Get invalid workout and invalid exercise total reps -> isLoading state raised."
        )
        assertTrue { getExerciseTotalRepsState2[0].isLoading }

        logger.i(
            TAG,
            "Get invalid workout and invalid exercise total reps -> isSuccessful state raised."
        )
        assertTrue { getExerciseTotalRepsState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get invalid workout and invalid exercise total reps -> data: ${getExerciseTotalRepsState2[1]}"
        )
        logger.i(TAG, "Assert statistics total reps 2 is invalid.")
        assertTrue { getExerciseTotalRepsState2[1].totalReps == 0 }
    }


    @Test
    @Repeat(50)
    fun testGetWorkoutTotalSetsPerformedUseCase() = runTest {

        //Generate test exercise
        val selectedExercise = workout1.exercises.first()
        val performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(selectedExercise)
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics 3 for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics 3 for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        val setsSum = performanceMetrics3.doWorkoutExerciseSets.size +
                performanceMetrics1.doWorkoutExerciseSets
                    .filter { it.exerciseId == selectedExercise.exerciseId }.size
        logger.i(
            TAG,
            "Total sets for exercise with id ${selectedExercise.exerciseId}: $setsSum"
        )
        val getExerciseTotalSetsState =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalSetsPerformedUseCase(
                workout1.workoutId, selectedExercise.exerciseId
            ).toList()

        logger.i(
            TAG,
            "Get exercise with id ${selectedExercise.exerciseId} total sets for workout 1-> isLoading state raised."
        )
        assertTrue { getExerciseTotalSetsState[0].isLoading }

        logger.i(
            TAG,
            "Get exercise with id ${selectedExercise.exerciseId} total sets for workout 1-> isSuccessful state raised."
        )
        assertTrue { getExerciseTotalSetsState[1].isSuccessful }

        logger.i(TAG, "Get exercise total sets -> data: ${getExerciseTotalSetsState[1]}")
        logger.i(TAG, "Assert statistics total sets is the same as the calculated one.")
        assertTrue { getExerciseTotalSetsState[1].totalSets == setsSum }

        //Test with invalid exercise
        val getExerciseTotalSetsState2 =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalSetsPerformedUseCase(
                -1, -1
            ).toList()

        logger.i(
            TAG,
            "Get invalid workout and invalid exercise total sets -> isLoading state raised."
        )
        assertTrue { getExerciseTotalSetsState2[0].isLoading }

        logger.i(
            TAG,
            "Get invalid workout and invalid exercise total sets -> isSuccessful state raised."
        )
        assertTrue { getExerciseTotalSetsState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get invalid workout and invalid exercise total sets -> data: ${getExerciseTotalSetsState2[1]}"
        )
        logger.i(TAG, "Assert statistics total sets 2 is invalid.")
        assertTrue { getExerciseTotalSetsState2[1].totalSets == 0 }
    }

    @Test
    @Repeat(50)
    fun testGetWorkoutTotalWeightLiftedUseCase() = runTest {

        //Generate test exercise
        val selectedExercise = workout1.exercises.first()
        val performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(selectedExercise)
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics 3 for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics 3 for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        val weightSum: Float = performanceMetrics3.doWorkoutExerciseSets.sumOf { it.weight!!.toDouble() * it.reps}.toFloat() +
                performanceMetrics1.doWorkoutExerciseSets.sumOf { it.weight!!.toDouble() * it.reps }.toFloat()
        logger.i(
            TAG,
            "Total weight lifted for workout 1: $weightSum"
        )
        val getTotalWeightLiftedState =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalWeightLiftedUseCase(
                workout1.workoutId
            ).toList()

        logger.i(
            TAG,
            "Get total weight lifted for workout 1 -> isLoading state raised."
        )
        assertTrue { getTotalWeightLiftedState[0].isLoading }

        logger.i(
            TAG,
            "Get total weight lifted for workout 1 -> isSuccessful state raised."
        )
        assertTrue { getTotalWeightLiftedState[1].isSuccessful }

        logger.i(TAG, "Get total weight lifted for workout 1 -> data: ${getTotalWeightLiftedState[1]}")
        logger.i(TAG, "Assert statistics total weight lifted is the same as the calculated one.")
        assertTrue { getTotalWeightLiftedState[1].totalWeight == weightSum }

        //Test with invalid exercise
        val getTotalWeightLiftedState2 =
            statisticsUseCases.workoutStatisticsUseCases.getWorkoutTotalWeightLiftedUseCase(
                -1
            ).toList()

        logger.i(
            TAG,
            "Get invalid workout total weight lifted -> isLoading state raised."
        )
        assertTrue { getTotalWeightLiftedState2[0].isLoading }

        logger.i(
            TAG,
            "Get invalid workout total weight lifted -> isSuccessful state raised."
        )
        assertTrue { getTotalWeightLiftedState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get invalid workout total weight lifted total weight -> data: ${getTotalWeightLiftedState2[1]}"
        )
        logger.i(TAG, "Assert statistics total weight 2 is invalid.")
        assertTrue { getTotalWeightLiftedState2[1].totalWeight == 0.0f }
    }
}