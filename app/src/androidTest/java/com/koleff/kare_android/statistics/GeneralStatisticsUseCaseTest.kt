package com.koleff.kare_android.statistics

import RepeatRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.manager.data.MockupDataGeneratorV2
import com.koleff.kare_android.data.datasource.ExerciseStatisticsDataSource
import com.koleff.kare_android.data.datasource.ExerciseStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsLocalDataSource
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
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
import java.util.Date
import java.util.UUID

@RunWith(AndroidJUnit4::class)
class GeneralStatisticsUseCaseTest {

    companion object {
        private val TAG = "GeneralStatisticsUseCaseTest"
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
        ).copy(date = Date(System.currentTimeMillis() - (1000 * 60 * 60 * 24))) //Yesterdays date

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
    fun testWorkoutsCompletedUseCase() = runTest {

        //Test if DB is initialized
        val getWorkoutsCompletedState =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().toList()

        logger.i(TAG, "Get total workouts completed -> isLoading state raised.")
        assertTrue { getWorkoutsCompletedState[0].isLoading }

        logger.i(
            TAG,
            "Get total workouts completed -> isSuccessful state raised."
        )
        assertTrue { getWorkoutsCompletedState[1].isSuccessful }

        logger.i(
            TAG,
            "Get total workouts completed data: ${getWorkoutsCompletedState[1]}"
        )

        logger.i(TAG, "Assert get total workouts completed is 2.")
        assertTrue { getWorkoutsCompletedState[1].totalTimesCompleted == 2 }

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

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()
        logger.i(TAG, "Do workout exercise sets for workout 1: ${performanceMetrics3.doWorkoutExerciseSets}")

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        //Increase
        val getWorkoutsCompletedState2 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().toList()

        logger.i(TAG, "Get total workouts completed after the increase -> isLoading state raised.")
        assertTrue { getWorkoutsCompletedState2[0].isLoading }

        logger.i(
            TAG,
            "Get total workouts completed after the increase -> isSuccessful state raised."
        )
        assertTrue { getWorkoutsCompletedState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get total workouts completed data after the increase: ${getWorkoutsCompletedState2[1]}"
        )

        logger.i(TAG, "Assert get total workouts completed is 3 after the increase.")
        assertTrue { getWorkoutsCompletedState2[1].totalTimesCompleted == 3 }

        //Decrease
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getWorkoutsCompletedState3 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().toList()

        logger.i(TAG, "Get total workouts completed after the decrease -> isLoading state raised.")
        assertTrue { getWorkoutsCompletedState3[0].isLoading }

        logger.i(
            TAG,
            "Get total workouts completed after the decrease -> isSuccessful state raised."
        )
        assertTrue { getWorkoutsCompletedState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get total workouts completed data after the decrease: ${getWorkoutsCompletedState3[1]}"
        )

        logger.i(TAG, "Assert get total workouts completed is 2 after the decrease.")
        assertTrue { getWorkoutsCompletedState3[1].totalTimesCompleted == 2 }
    }

    @Test
    @Repeat(50)
    fun testDistinctWorkoutsCompletedUseCase() = runTest {

        //Test if DB is initialized
        val getDistinctWorkoutsCompletedState =
            statisticsUseCases.generalStatisticsUseCases.getDistinctWorkoutsCompletedUseCase()
                .toList()

        logger.i(TAG, "Get total distinct workouts completed -> isLoading state raised.")
        assertTrue { getDistinctWorkoutsCompletedState[0].isLoading }

        logger.i(
            TAG,
            "Get total distinct workouts completed -> isSuccessful state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState[1].isSuccessful }

        logger.i(
            TAG,
            "Get total distinct workouts completed data: ${getDistinctWorkoutsCompletedState[1]}"
        )

        logger.i(TAG, "Assert get total distinct workouts completed is 2.")
        assertTrue { getDistinctWorkoutsCompletedState[1].totalTimesCompleted == 2 }

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

        //Increase
        val getDistinctWorkoutsCompletedState2 =
            statisticsUseCases.generalStatisticsUseCases.getDistinctWorkoutsCompletedUseCase()
                .toList()

        logger.i(
            TAG,
            "Get total distinct workouts completed after the increase -> isLoading state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState2[0].isLoading }

        logger.i(
            TAG,
            "Get total distinct workouts completed after the increase -> isSuccessful state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get total distinct workouts completed data after the increase: ${getDistinctWorkoutsCompletedState2[1]}"
        )

        logger.i(
            TAG,
            "Assert get total workouts completed is 2 after the increase, because the same workout was used for the performance metrics that was added."
        )
        assertTrue { getDistinctWorkoutsCompletedState2[1].totalTimesCompleted == 2 }

        //Decrease (still workout 1 performance metrics in the DB)
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getDistinctWorkoutsCompletedState3 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().toList()

        logger.i(
            TAG,
            "Get total distinct workouts completed after the decrease -> isLoading state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState3[0].isLoading }

        logger.i(
            TAG,
            "Get total distinct workouts completed after the decrease -> isSuccessful state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get total workouts completed data after the decrease: ${getDistinctWorkoutsCompletedState3[1]}"
        )

        logger.i(TAG, "Assert get total workouts completed is 2 after the decrease.")
        assertTrue { getDistinctWorkoutsCompletedState3[1].totalTimesCompleted == 2 }

        //Decrease (no more workout 1 performance metrics)
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics1.id
        ).toList()

        val getDistinctWorkoutsCompletedState4 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutsCompletedUseCase().toList()

        logger.i(
            TAG,
            "Get total distinct workouts completed after the decrease -> isLoading state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState4[0].isLoading }

        logger.i(
            TAG,
            "Get total distinct workouts completed after the decrease -> isSuccessful state raised."
        )
        assertTrue { getDistinctWorkoutsCompletedState4[1].isSuccessful }

        logger.i(
            TAG,
            "Get total workouts completed data after the decrease: ${getDistinctWorkoutsCompletedState4[1]}"
        )

        logger.i(
            TAG,
            "Assert get total workouts completed is 1 after the decrease (no more workout 1 performance metrics)."
        )
        assertTrue { getDistinctWorkoutsCompletedState4[1].totalTimesCompleted == 1 }
    }

    @Test
    @Repeat(50)
    fun testGetWorkoutStreakUseCase() = runTest {

        //Test if DB is initialized
        val getWorkoutStreakState =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutStreakUseCase().toList()

        logger.i(TAG, "Get workout streak -> isLoading state raised.")
        assertTrue { getWorkoutStreakState[0].isLoading }

        logger.i(
            TAG,
            "Get workout streak -> isSuccessful state raised."
        )
        assertTrue { getWorkoutStreakState[1].isSuccessful }

        logger.i(
            TAG,
            "Get workout streak data: ${getWorkoutStreakState[1]}"
        )

        logger.i(TAG, "Assert get workout streak is 2.")
        assertTrue { getWorkoutStreakState[1].streak == 2 }

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

        //Increase
        val getWorkoutStreakState2 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutStreakUseCase().toList()

        logger.i(TAG, "Get workout streak after the increase -> isLoading state raised.")
        assertTrue { getWorkoutStreakState2[0].isLoading }

        logger.i(
            TAG,
            "Get workout streak after the increase-> isSuccessful state raised."
        )
        assertTrue { getWorkoutStreakState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get workout streak data after the increase: ${getWorkoutStreakState2[1]}"
        )

        logger.i(
            TAG,
            "Assert get total workouts completed is 2 after the increase, because the date difference is not more than a day."
        )
        assertTrue { getWorkoutStreakState2[1].streak == 2 }

        //Decrease (still workout 1 performance metrics in the DB)
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getWorkoutStreakState3 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutStreakUseCase().toList()

        logger.i(TAG, "Get workout streak after the decrease -> isLoading state raised.")
        assertTrue { getWorkoutStreakState3[0].isLoading }

        logger.i(
            TAG,
            "Get workout streak after the decrease -> isSuccessful state raised."
        )
        assertTrue { getWorkoutStreakState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get workout streak data after the decrease: ${getWorkoutStreakState3[1]}"
        )

        logger.i(TAG, "Assert get total workouts completed is 2 after the decrease.")
        assertTrue { getWorkoutStreakState3[1].streak == 2 }

        //Decrease (no more workout 1 performance metrics)
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics1.id
        ).toList()

        val getWorkoutStreakState4 =
            statisticsUseCases.generalStatisticsUseCases.getWorkoutStreakUseCase().toList()

        logger.i(TAG, "Get workout streak after the decrease -> isLoading state raised.")
        assertTrue { getWorkoutStreakState4[0].isLoading }

        logger.i(
            TAG,
            "Get workout streak after the decrease -> isSuccessful state raised."
        )
        assertTrue { getWorkoutStreakState4[1].isSuccessful }

        logger.i(
            TAG,
            "Get workout streak data after the decrease: ${getWorkoutStreakState4[1]}"
        )

        logger.i(
            TAG,
            "Assert get workout streak is 1 after the decrease (no more workout 1 performance metrics)."
        )
        assertTrue { getWorkoutStreakState4[1].streak == 1 }
    }

    @Test
    @Repeat(50)
    fun testGetMostFrequentWorkoutUseCase() = runTest {

        //Test if DB is initialized
        val getMostFrequentWorkoutState =
            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase().toList()

        logger.i(TAG, "Get most frequent workout -> isLoading state raised.")
        assertTrue { getMostFrequentWorkoutState[0].isLoading }

        logger.i(
            TAG,
            "Get most frequent workout -> isSuccessful state raised."
        )
        assertTrue { getMostFrequentWorkoutState[1].isSuccessful }

        logger.i(
            TAG,
            "Get most frequent workout data: ${getMostFrequentWorkoutState[1]}"
        )

        logger.i(TAG, "Assert get most frequent workout is workout 2 (added last).")
        assertTrue { getMostFrequentWorkoutState[1].workout == workout2.toWorkout() }

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

        //Changed to workout 1
        val getMostFrequentWorkoutState2 =
            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase().toList()

        logger.i(TAG, "Get most frequent workout 2 -> isLoading state raised.")
        assertTrue { getMostFrequentWorkoutState2[0].isLoading }

        logger.i(
            TAG,
            "Get most frequent workout 2 -> isSuccessful state raised."
        )
        assertTrue { getMostFrequentWorkoutState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get most frequent workout 2 data: ${getMostFrequentWorkoutState2[1]}"
        )

        logger.i(TAG, "Assert get most frequent workout is workout 1.")
        assertTrue { getMostFrequentWorkoutState2[1].workout == workout1.toWorkout() }

        //Remove workout 1 performance metrics -> workout 1 and 2 become equal frequencies
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getMostFrequentWorkoutState3 =
            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase().toList()

        logger.i(TAG, "Get most frequent workout 3 -> isLoading state raised.")
        assertTrue { getMostFrequentWorkoutState3[0].isLoading }

        logger.i(
            TAG,
            "Get most frequent workout 3 -> isSuccessful state raised."
        )
        assertTrue { getMostFrequentWorkoutState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get most frequent workout 3 data: ${getMostFrequentWorkoutState3[1]}"
        )

        logger.i(TAG, "Assert get most frequent workout is workout 2 after the decrease.")
        assertTrue { getMostFrequentWorkoutState3[1].workout == workout2.toWorkout() }

        //Decrease (no more workout 1 performance metrics)
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics1.id
        ).toList()

        val getMostFrequentWorkoutState4 =
            statisticsUseCases.generalStatisticsUseCases.getMostFrequentWorkoutUseCase().toList()

        logger.i(TAG, "Get most frequent workout 4 -> isLoading state raised.")
        assertTrue { getMostFrequentWorkoutState4[0].isLoading }

        logger.i(
            TAG,
            "Get most frequent workout 4 -> isSuccessful state raised."
        )
        assertTrue { getMostFrequentWorkoutState4[1].isSuccessful }

        logger.i(
            TAG,
            "Get most frequent workout 4 data: ${getMostFrequentWorkoutState4[1]}"
        )

        logger.i(TAG, "Assert get most frequent workout is workout 2 after the decrease.")
        assertTrue { getMostFrequentWorkoutState4[1].workout == workout2.toWorkout() }
    }

    @Test
    @Repeat(50)
    fun testGetMostTrainedExerciseUseCase() = runTest {
        val allExerciseIds = performanceMetrics1.doWorkoutExerciseSets.map { it.exerciseId } +
                performanceMetrics2.doWorkoutExerciseSets.map { it.exerciseId }

        val mostTrainedExercise = allExerciseIds
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key }

        val mostTrainedExerciseId = mostTrainedExercise.firstOrNull()

        logger.i(TAG, "Calculated most trained exercise is with id: $mostTrainedExerciseId.")

        //Test if DB is initialized
        val getMostTrainedExerciseState =
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedExerciseUseCase().toList()

        logger.i(TAG, "Get most trained exercise -> isLoading state raised.")
        assertTrue { getMostTrainedExerciseState[0].isLoading }

        logger.i(
            TAG,
            "Get most trained exercise -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedExerciseState[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained exercise data: ${getMostTrainedExerciseState[1]}"
        )

        logger.i(TAG, "Assert most trained exercise id is $mostTrainedExerciseId.")
        assertTrue { getMostTrainedExerciseState[1].exercise.exerciseId == mostTrainedExerciseId }

        //Increase the most trained exercise
        val selectedExercise = workout1.exercises.first()
        logger.i(TAG, "Selected exercise: $selectedExercise")
        logger.i(TAG, "Selected exercise muscle group: ${selectedExercise.muscleGroup}")

        val performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise
            ) //If there are 20 exercises, add 19 so you can make sure it is the most trained group
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()
        logger.i(TAG, "Do workout exercise sets for workout 1: ${performanceMetrics3.doWorkoutExerciseSets}")

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        //Add new most trained exercise
        val getMostTrainedExerciseState2 =
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedExerciseUseCase().toList()

        logger.i(TAG, "Get most trained exercise 2 -> isLoading state raised.")
        assertTrue { getMostTrainedExerciseState2[0].isLoading }

        logger.i(
            TAG,
            "Get most trained exercise 2 -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedExerciseState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained exercise 2 data: ${getMostTrainedExerciseState2[1]}"
        )

        logger.i(TAG, "Assert get most trained exercise id is ${selectedExercise.exerciseId}.")
        assertTrue { getMostTrainedExerciseState2[1].exercise.exerciseId == selectedExercise.exerciseId }

        //Delete current most trained exercise
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getMostTrainedExerciseState3 =
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedExerciseUseCase().toList()

        logger.i(TAG, "Get most trained exercise 3 -> isLoading state raised.")
        assertTrue { getMostTrainedExerciseState3[0].isLoading }

        logger.i(
            TAG,
            "Get most trained exercise 3 -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedExerciseState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained exercise 3 data: ${getMostTrainedExerciseState3[1]}"
        )

        logger.i(TAG, "Assert get most trained exercise id is $mostTrainedExerciseId.")
        assertTrue { getMostTrainedExerciseState3[1].exercise.exerciseId == mostTrainedExerciseId }
    }

    @Test
    @Repeat(50)
    fun testGetMostTrainedMuscleGroupUseCase() = runTest {

        //Calculate most trained muscle group
        val allExerciseSets = performanceMetrics1.doWorkoutExerciseSets + performanceMetrics2.doWorkoutExerciseSets
        val catalogExercises =
            exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)

        val exerciseIdCount = mutableMapOf<Int, Int>()
        for (exerciseSet in allExerciseSets) {
            exerciseIdCount[exerciseSet.exerciseId] =
                exerciseIdCount.getOrDefault(exerciseSet.exerciseId, 0) + 1
        }

        val mostTrainedExerciseId = exerciseIdCount.maxByOrNull { it.value }?.key
        val mostTrainedExercise = mostTrainedExerciseId?.let { exerciseId ->
            catalogExercises.find { it.exerciseId == exerciseId }
        }
        val mostTrainedMuscleGroup = mostTrainedExercise?.muscleGroup ?: MuscleGroup.NONE

        logger.i(TAG, "Calculated most trained muscle group is with id: ${mostTrainedMuscleGroup}")

        //Test if DB is initialized
        val getMostTrainedMuscleGroupState =
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedMuscleGroupUseCase().toList()

        logger.i(TAG, "Get most trained muscle group -> isLoading state raised.")
        assertTrue { getMostTrainedMuscleGroupState[0].isLoading }

        logger.i(
            TAG,
            "Get most trained muscle group -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedMuscleGroupState[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained muscle group data: ${getMostTrainedMuscleGroupState[1]}"
        )

        logger.i(TAG, "Assert most trained muscle group is ${mostTrainedMuscleGroup}.")
        assertTrue { getMostTrainedMuscleGroupState[1].muscleGroup == mostTrainedMuscleGroup }

        //Increase the most trained exercise
        val selectedExercise = workout1.exercises.first()
        logger.i(TAG, "Selected exercise: $selectedExercise")
        logger.i(TAG, "Selected exercise muscle group: ${selectedExercise.muscleGroup}")

        val performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise,
                selectedExercise
            ) //If there are 20 exercises, add 19 so you can make sure it is the most trained group
        )
        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()
        logger.i(TAG, "Do workout exercise sets for workout 1: ${performanceMetrics3.doWorkoutExerciseSets}")

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        //Add new most trained exercise
        val getMostTrainedMuscleGroupState2 =
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedMuscleGroupUseCase().toList()

        logger.i(TAG, "Get most trained muscle group 2 -> isLoading state raised.")
        assertTrue { getMostTrainedMuscleGroupState2[0].isLoading }

        logger.i(
            TAG,
            "Get most trained muscle group 2 -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedMuscleGroupState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained muscle group 2 data: ${getMostTrainedMuscleGroupState2[1]}"
        )

        logger.i(TAG, "Assert most trained muscle group 2 is ${selectedExercise.muscleGroup}.")
        assertTrue { getMostTrainedMuscleGroupState2[1].muscleGroup == selectedExercise.muscleGroup }

        //Delete current most trained exercise
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getMostTrainedMuscleGroupState3 =
            statisticsUseCases.generalStatisticsUseCases.getMostTrainedMuscleGroupUseCase().toList()

        logger.i(TAG, "Get most trained muscle group 3 -> isLoading state raised.")
        assertTrue { getMostTrainedMuscleGroupState3[0].isLoading }

        logger.i(
            TAG,
            "Get most trained muscle group 3 -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedMuscleGroupState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained muscle group 3 data: ${getMostTrainedMuscleGroupState3[1]}"
        )

        logger.i(TAG, "Assert most trained muscle group 3 is $mostTrainedMuscleGroup.")
        assertTrue { getMostTrainedMuscleGroupState3[1].muscleGroup == mostTrainedMuscleGroup }
    }

    @Test
    @Repeat(50)
    fun testGetStrongestMuscleGroupUseCase() = runTest {

        //Calculating the strongest muscle group
        val allExerciseSets =
            performanceMetrics1.doWorkoutExerciseSets + performanceMetrics2.doWorkoutExerciseSets

        val catalogExercises =
            exerciseDao.getAllCatalogExercises(workoutId = Constants.CATALOG_EXERCISE_ID)
        val exerciseIdToMuscleGroup =
            catalogExercises.associateBy({ it.exerciseId }, { it.muscleGroup })

        val maxWeightLifted = mutableMapOf<MuscleGroup, Float>()
        for (exerciseSet in allExerciseSets) {
            val muscleGroup =
                exerciseIdToMuscleGroup[exerciseSet.exerciseId] ?: continue  //Skip if not found

            maxWeightLifted[muscleGroup] = maxOf(
                maxWeightLifted.getOrDefault(muscleGroup, 0f),
                exerciseSet.weight ?: 0f
            )
        }

        val strongestMuscleGroup = maxWeightLifted.maxByOrNull { it.value }?.key
        logger.i(TAG, "Calculated strongest muscle group is: $strongestMuscleGroup")
        maxWeightLifted.forEach { (muscleGroup, weight) ->
            logger.i(TAG, "Muscle group: $muscleGroup, Max weight lifted: $weight")
        }

        //Test if DB is initialized
        val getMostTrainedMuscleGroupState =
            statisticsUseCases.generalStatisticsUseCases.getStrongestMuscleGroupUseCase().toList()

        logger.i(TAG, "Get most trained muscle group -> isLoading state raised.")
        assertTrue { getMostTrainedMuscleGroupState[0].isLoading }

        logger.i(
            TAG,
            "Get most trained muscle group -> isSuccessful state raised."
        )
        assertTrue { getMostTrainedMuscleGroupState[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained muscle group data: ${getMostTrainedMuscleGroupState[1]}"
        )

        logger.i(TAG, "Assert most trained muscle group is ${strongestMuscleGroup}.")
        assertTrue { getMostTrainedMuscleGroupState[1].muscleGroup == strongestMuscleGroup }

        //Increase the most trained exercise
        val selectedExercise = workout1.exercises.first()
        logger.i(TAG, "Selected exercise: $selectedExercise")
        logger.i(TAG, "Selected exercise muscle group: ${selectedExercise.muscleGroup}")

        var performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(selectedExercise),
        )

        performanceMetrics3 = performanceMetrics3.copy(
            doWorkoutExerciseSets = listOf(
                DoWorkoutExerciseSetDto(
                    workoutPerformanceMetricsId = performanceMetrics3.id,
                    instanceId = UUID.randomUUID(),
                    templateSetId = selectedExercise.sets.first().setId ?: UUID.randomUUID(),
                    workoutId = workout1.workoutId,
                    exerciseId = selectedExercise.exerciseId,
                    reps = 12,
                    weight = 500f,
                    time = null,
                    date = Date(),
                    isDone = false
                )
            )
        )

        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()
        logger.i(TAG, "Do workout exercise sets for workout 1: ${performanceMetrics3.doWorkoutExerciseSets}")

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        //Add new most trained exercise
        val getStrongestMuscleGroupState2 =
            statisticsUseCases.generalStatisticsUseCases.getStrongestMuscleGroupUseCase().toList()

        logger.i(TAG, "Get strongest muscle group 2 -> isLoading state raised.")
        assertTrue { getStrongestMuscleGroupState2[0].isLoading }

        logger.i(
            TAG,
            "Get strongest muscle group 2 -> isSuccessful state raised."
        )
        assertTrue { getStrongestMuscleGroupState2[1].isSuccessful }

        logger.i(
            TAG,
            "Get most trained muscle group 2 data: ${getStrongestMuscleGroupState2[1]}"
        )

        logger.i(TAG, "Assert strongest muscle group 2 is ${selectedExercise.muscleGroup}.")
        assertTrue { getStrongestMuscleGroupState2[1].muscleGroup == selectedExercise.muscleGroup }

        //Delete current most trained exercise
        doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3.id
        ).toList()

        val getStrongestMuscleGroupState3 =
            statisticsUseCases.generalStatisticsUseCases.getStrongestMuscleGroupUseCase().toList()

        logger.i(TAG, "Get strongest muscle group 3 -> isLoading state raised.")
        assertTrue { getStrongestMuscleGroupState3[0].isLoading }

        logger.i(
            TAG,
            "Get strongest muscle group 3 -> isSuccessful state raised."
        )
        assertTrue { getStrongestMuscleGroupState3[1].isSuccessful }

        logger.i(
            TAG,
            "Get strongest muscle group 3 data: ${getStrongestMuscleGroupState3[1]}"
        )

        logger.i(TAG, "Assert strongest muscle group 3 is $strongestMuscleGroup.")
        assertTrue { getStrongestMuscleGroupState3[1].muscleGroup == strongestMuscleGroup }
    }

    @Test
    @Repeat(50)
    fun testGetTotalWeightLiftedUseCase() = runTest {
        val allExerciseSets = performanceMetrics1.doWorkoutExerciseSets +
                performanceMetrics2.doWorkoutExerciseSets

        val totalWeight = allExerciseSets.sumOf {
            it.weight!!.toDouble() * it.reps
        }.toFloat()

        val getTotalWeightLiftedState = statisticsUseCases.generalStatisticsUseCases
            .getTotalWeightLiftedUseCase().toList()

        logger.i(TAG, "Get total weight lifted -> isLoading state raised.")
        assertTrue { getTotalWeightLiftedState[0].isLoading }

        logger.i(TAG, "Get total weight lifted -> isSuccessful state raised.")
        assertTrue { getTotalWeightLiftedState[1].isSuccessful }

        logger.i(TAG, "Get total weight lifted data: ${getTotalWeightLiftedState[1]}")

        logger.i(TAG, "Assert total weight lifted is $totalWeight.")
        assertTrue { getTotalWeightLiftedState[1].totalWeight == totalWeight }

        //Increase weight
        val selectedExercise = workout1.exercises.first()
        var performanceMetrics3 = MockupDataGeneratorV2.generateDoWorkoutPerformanceMetrics(
            id = 3,
            workout = workout1.toWorkout(),
            exercises = listOf(selectedExercise),
        )

        performanceMetrics3 = performanceMetrics3.copy(
            doWorkoutExerciseSets = listOf(
                DoWorkoutExerciseSetDto(
                    workoutPerformanceMetricsId = performanceMetrics3.id,
                    instanceId = UUID.randomUUID(),
                    templateSetId = selectedExercise.sets.first().setId ?: UUID.randomUUID(),
                    workoutId = workout1.workoutId,
                    exerciseId = selectedExercise.exerciseId,
                    reps = 12,
                    weight = 500f,
                    time = null,
                    date = Date(),
                    isDone = false
                )
            )
        )
        val newTotalWeight = totalWeight + (performanceMetrics3.doWorkoutExerciseSets.first().weight!! * performanceMetrics3.doWorkoutExerciseSets.first().reps)
        logger.i(TAG, "New total weight: $newTotalWeight")

        doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
            performanceMetrics3
        ).toList()
        logger.i(TAG, "Do workout performance metrics for workout 1: $performanceMetrics3")

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics3.doWorkoutExerciseSets
        ).toList()
        logger.i(TAG, "Do workout exercise sets for workout 1: ${performanceMetrics3.doWorkoutExerciseSets}")

        val getDoWorkoutPerformanceMetrics3 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = 3
            ).toList()
        logger.i(
            TAG,
            "Get do workout performance metrics for workout 1: ${getDoWorkoutPerformanceMetrics3[1].doWorkoutPerformanceMetrics}"
        )

        val getTotalWeightLiftedState2 = statisticsUseCases.generalStatisticsUseCases
            .getTotalWeightLiftedUseCase().toList()

        logger.i(TAG, "Get total weight lifted 2 -> isLoading state raised.")
        assertTrue { getTotalWeightLiftedState2[0].isLoading }

        logger.i(TAG, "Get total weight lifted 2 -> isSuccessful state raised.")
        assertTrue { getTotalWeightLiftedState2[1].isSuccessful }

        logger.i(TAG, "Get total weight lifted 2 data: ${getTotalWeightLiftedState2[1]}")

        logger.i(TAG, "Assert total weight lifted is $newTotalWeight.")
        assertTrue { getTotalWeightLiftedState2[1].totalWeight == newTotalWeight }
    }
}

//doWorkoutExerciseSet=[] is not returned because saveAllExerciseSets is not called in some places...
