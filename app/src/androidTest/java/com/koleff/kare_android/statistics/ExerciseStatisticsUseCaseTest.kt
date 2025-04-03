package com.koleff.kare_android.statistics

import RepeatRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.crashlytics.internal.Logger
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsLocalDataSource
import com.koleff.kare_android.data.datasource.ExerciseStatisticsDataSource
import com.koleff.kare_android.data.datasource.ExerciseStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsDataSource
import com.koleff.kare_android.data.datasource.GeneralStatisticsLocalDataSource
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSourceV2
import com.koleff.kare_android.data.datasource.WorkoutStatisticsDataSource
import com.koleff.kare_android.data.datasource.WorkoutStatisticsLocalDataSource
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
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
import java.util.Date
import java.util.UUID
import kotlin.random.Random

typealias WorkoutFakeDataSource = WorkoutLocalDataSourceV2
typealias DoWorkoutPerformanceMetricsFakeDataSource = DoWorkoutPerformanceMetricsLocalDataSource

@RunWith(AndroidJUnit4::class)
class ExerciseStatisticsUseCaseTest {

    companion object {
        private val TAG = "ExerciseStatisticsUseCaseTest"
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

//        workoutUseCases.createCustomWorkoutUseCase(workout1.toWorkout()).toList()
//        workoutUseCases.createCustomWorkoutUseCase(workout2.toWorkout()).toList()
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

        //Insert 1 set debugging test
//        Logger.getLogger()
//            .i("Do workout exercise set: ${doWorkoutPerformanceMetrics.doWorkoutExerciseSets[0].toEntity()}")
//        doWorkoutExerciseSetDao.insertSet(
//            doWorkoutPerformanceMetrics.doWorkoutExerciseSets[0].toEntity()
//        )

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
                workoutId = workout2.workoutId
            ).toList()

        Logger.getLogger().i(
            "Get do workout performance metrics 2: ${getDoWorkoutPerformanceMetrics2[1].doWorkoutPerformanceMetricsList}"
        )

        doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
            performanceMetrics2.doWorkoutExerciseSets
        ).toList()
    }

    @AfterAll
    fun tearDown() {
        kareDatabase.close()
    }

    @get:Rule
    val repeatedRule = RepeatRule()

    @Test
    @Repeat(50)
    fun testExercisePRUseCase() = runTest {
        val firstExercise = workout1.exercises.first()
        val exercisePerformanceSets =
            doWorkoutExerciseSetDao.findSetsByExerciseId(firstExercise.exerciseId)
        val setPR = exercisePerformanceSets.maxBy {
            it.weight!!
        }
        logger.i(TAG, "Selected exercise for the test: $firstExercise")
        logger.i(TAG, "Current set PR: $setPR")

        val getExercisePRState = statisticsUseCases.exerciseStatisticsUseCases.getExercisePRUseCase(
            firstExercise.exerciseId,
        ).toList()

        logger.i(TAG, "Get exercise PR -> isLoading state raised.")
        assertTrue { getExercisePRState[0].isLoading }

        logger.i(TAG, "Get exercise PR -> isSuccessful state raised.")
        assertTrue { getExercisePRState[1].isSuccessful }

        logger.i(TAG, "Get exercise PR -> data: ${getExercisePRState[1].pr}")
        logger.i(TAG, "Assert statistics pr is the same as the calculated one.")
        assertTrue { getExercisePRState[1].pr == setPR.weight }

        //Invalid / no exercises test
        val getExercisePRState2 = statisticsUseCases.exerciseStatisticsUseCases.getExercisePRUseCase(
            -1,
        ).toList()

        logger.i(TAG, "Get exercise PR 2 -> isLoading state raised.")
        assertTrue { getExercisePRState2[0].isLoading }

        logger.i(TAG, "Get exercise PR 2 -> isSuccessful state raised.")
        assertTrue { getExercisePRState2[1].isSuccessful }

        logger.i(TAG, "Get exercise PR 2 -> data: ${getExercisePRState2[1].pr}")
        logger.i(TAG, "Assert statistics pr is the same as the calculated one.")
        assertTrue { getExercisePRState2[1].pr == 0.0f }
    }

    @Test
    @Repeat(50)
    fun testExercise1RepMaxUseCase() = runTest {
        val selectedExercise = workoutUseCases.getWorkoutDetailsUseCase(performanceMetrics1.workout.workoutId)
            .toList()[1].workoutDetails.exercises.first()
        val selectedExerciseSet = selectedExercise.sets.first()

        val exercisePerformanceSets =
            doWorkoutExerciseSetDao.findSetsByExerciseId(selectedExercise.exerciseId)
        val set1RepMax = exercisePerformanceSets
            .filter { it.reps == 1 }
            .maxByOrNull {
                it.weight!!
            }
        logger.i(TAG, "Selected exercise for the test: $selectedExercise")
        logger.i(TAG, "Current set 1 rep max: $set1RepMax")

        val getExercise1RepMaxState = statisticsUseCases.exerciseStatisticsUseCases.getExercise1RepMaxUseCase(
            selectedExercise.exerciseId,
        ).toList()

        logger.i(TAG, "Get exercise 1 rep max -> isLoading state raised.")
        assertTrue { getExercise1RepMaxState[0].isLoading }

        logger.i(TAG, "Get exercise 1 rep max -> isSuccessful state raised.")
        assertTrue { getExercise1RepMaxState[1].isSuccessful }

        logger.i(TAG, "Get exercise 1 rep max -> data: ${getExercise1RepMaxState[1].pr}")
        logger.i(TAG, "Assert statistics 1 rep max is the same as the calculated one.")
        assertTrue { getExercise1RepMaxState[1].pr == (set1RepMax?.weight ?: 0.0f) }

        //Test with found 1 rep MAX
        val maxWeight = 150f
        val doWorkout1RepMaxSet = DoWorkoutExerciseSetDto(
            instanceId = UUID.randomUUID(),
            workoutPerformanceMetricsId = performanceMetrics1.id,
            workoutId = performanceMetrics1.workout.workoutId,
            exerciseId = selectedExercise.exerciseId,
            templateSetId = selectedExerciseSet.setId!!,
            reps = 1,
            weight = maxWeight,
            isDone = Random.nextBoolean(),
            time = null,
            date = Date()
        )
        val save1RepMaxSetState = doWorkoutPerformanceMetricsUseCases.saveDoWorkoutExerciseSetUseCase(
            doWorkout1RepMaxSet
        ).toList()
        logger.i(TAG, "1 rep Max exercise set -> $doWorkout1RepMaxSet")

        logger.i(TAG, "Save do workout exercise set -> isLoading state raised.")
        assertTrue { save1RepMaxSetState[0].isLoading }

        logger.i(
            TAG,
            "Save do workout exercise set -> isSuccessful state raised."
        )
        assertTrue { save1RepMaxSetState[1].isSuccessful }

        //Debugging
        val getDoWorkoutPerformanceMetrics1 =
            doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                performanceMetricsId = selectedExercise.workoutId
            ).toList()
        logger.i(TAG, "Get do workout performance metrics 1: ${getDoWorkoutPerformanceMetrics1[1].doWorkoutPerformanceMetrics.doWorkoutExerciseSets}")
       val dbEntry = doWorkoutExerciseSetDao.findSetById(
           instanceId = doWorkout1RepMaxSet.instanceId
        )
        logger.i(TAG, "1 rep max set DB entry -> $dbEntry")

        val getExercise1RepMaxState2 = statisticsUseCases.exerciseStatisticsUseCases.getExercise1RepMaxUseCase(
            doWorkout1RepMaxSet.exerciseId,
        ).toList()

        logger.i(TAG, "Get exercise 1 rep max 2 -> isLoading state raised.")
        assertTrue { getExercise1RepMaxState2[0].isLoading }

        logger.i(TAG, "Get exercise 1 rep max 2 -> isSuccessful state raised.")
        assertTrue { getExercise1RepMaxState[1].isSuccessful }

        logger.i(TAG, "Get exercise 1 rep max 2 -> data: ${getExercise1RepMaxState2[1].pr}")
        logger.i(TAG, "Assert statistics 1 rep max is the same as the newly added 1 rep max set.")
        assertTrue { getExercise1RepMaxState2[1].pr == maxWeight }
    }

    @Test
    @Repeat(50)
    fun testGetExerciseTotalRepsUseCase() = runTest {
        val selectedExercise = workoutUseCases.getWorkoutDetailsUseCase(performanceMetrics1.workout.workoutId)
            .toList()[1].workoutDetails.exercises.random()

        val exercisePerformanceSets =
            doWorkoutExerciseSetDao.findSetsByExerciseId(selectedExercise.exerciseId)
        val repsSum = exercisePerformanceSets.sumOf { it.reps }

        logger.i(TAG, "Selected exercise for the test: $selectedExercise")
        logger.i(TAG, "Total reps: $repsSum")

        val getExerciseTotalRepsState = statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalRepsPerformedUseCase(
            selectedExercise.exerciseId,
        ).toList()

        logger.i(TAG, "Get exercise total reps -> isLoading state raised.")
        assertTrue { getExerciseTotalRepsState[0].isLoading }

        logger.i(TAG, "Get exercise total reps -> isSuccessful state raised.")
        assertTrue { getExerciseTotalRepsState[1].isSuccessful }

        logger.i(TAG, "Get exercise total reps -> data: ${getExerciseTotalRepsState[1]}")
        logger.i(TAG, "Assert statistics total reps is the same as the calculated one.")
        assertTrue { getExerciseTotalRepsState[1].totalReps == repsSum }

        //Test with invalid exercise
        val getExerciseTotalRepsState2 = statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalRepsPerformedUseCase(
            -1,
        ).toList()

        logger.i(TAG, "Get exercise total reps 2 -> isLoading state raised.")
        assertTrue { getExerciseTotalRepsState2[0].isLoading }

        logger.i(TAG, "Get exercise total reps 2 -> isSuccessful state raised.")
        assertTrue { getExerciseTotalRepsState2[1].isSuccessful }

        logger.i(TAG, "Get exercise total reps 2 -> data: ${getExerciseTotalRepsState2[1]}")
        logger.i(TAG, "Assert statistics total reps 2 is invalid.")
        assertTrue { getExerciseTotalRepsState2[1].totalReps == 0 }
    }


    @Test
    @Repeat(50)
    fun testGetExerciseTotalSetsUseCase() = runTest {
        val selectedExercise = workoutUseCases.getWorkoutDetailsUseCase(performanceMetrics1.workout.workoutId)
            .toList()[1].workoutDetails.exercises.random()

        val exercisePerformanceSets =
            doWorkoutExerciseSetDao.findSetsByExerciseId(selectedExercise.exerciseId)
        val setsSum = exercisePerformanceSets.size

        logger.i(TAG, "Selected exercise for the test: $selectedExercise")
        logger.i(TAG, "Total sets: $setsSum")

        val getExerciseTotalSetsState = statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalSetsPerformedUseCase(
            selectedExercise.exerciseId,
        ).toList()

        logger.i(TAG, "Get exercise total sets -> isLoading state raised.")
        assertTrue { getExerciseTotalSetsState[0].isLoading }

        logger.i(TAG, "Get exercise total sets -> isSuccessful state raised.")
        assertTrue { getExerciseTotalSetsState[1].isSuccessful }

        logger.i(TAG, "Get exercise total sets -> data: ${getExerciseTotalSetsState[1]}")
        logger.i(TAG, "Assert statistics total sets is the same as the calculated one.")
        assertTrue { getExerciseTotalSetsState[1].totalSets == setsSum }

        //Test with invalid exercise
        val getExerciseTotalSetsState2 = statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalSetsPerformedUseCase(
            -1,
        ).toList()

        logger.i(TAG, "Get exercise total sets 2 -> isLoading state raised.")
        assertTrue { getExerciseTotalSetsState2[0].isLoading }

        logger.i(TAG, "Get exercise total sets 2 -> isSuccessful state raised.")
        assertTrue { getExerciseTotalSetsState2[1].isSuccessful }

        logger.i(TAG, "Get exercise total sets 2 -> data: ${getExerciseTotalSetsState2[1]}")
        logger.i(TAG, "Assert statistics total sets 2 is invalid.")
        assertTrue { getExerciseTotalSetsState2[1].totalSets == 0 }
    }

    @Test
    @Repeat(50)
    fun testGetExerciseTotalWeightLiftedCase() = runTest {
        val selectedExercise = workoutUseCases.getWorkoutDetailsUseCase(performanceMetrics1.workout.workoutId)
            .toList()[1].workoutDetails.exercises.random()

        val exercisePerformanceSets =
            doWorkoutExerciseSetDao.findSetsByExerciseId(selectedExercise.exerciseId)
        val weightLifted = exercisePerformanceSets.sumOf { it.weight!!.toDouble() * it.reps}.toFloat()

        logger.i(TAG, "Selected exercise for the test: $selectedExercise")
        logger.i(TAG, "Total weight lifted: $weightLifted")

        val getExerciseTotalWeightLiftedState = statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalWeightLiftedUseCase(
            selectedExercise.exerciseId,
        ).toList()

        logger.i(TAG, "Get exercise total weight lifted -> isLoading state raised.")
        assertTrue { getExerciseTotalWeightLiftedState[0].isLoading }

        logger.i(TAG, "Get exercise total weight lifted -> isSuccessful state raised.")
        assertTrue { getExerciseTotalWeightLiftedState[1].isSuccessful }

        logger.i(TAG, "Get exercise total weight lifted -> data: ${getExerciseTotalWeightLiftedState[1]}")
        logger.i(TAG, "Assert statistics total weight lifted is the same as the calculated one.")
        assertTrue { getExerciseTotalWeightLiftedState[1].totalWeight == weightLifted }

        //Test with invalid exercise
        val getExerciseTotalWeightLiftedState2 = statisticsUseCases.exerciseStatisticsUseCases.getExerciseTotalWeightLiftedUseCase(
            -1,
        ).toList()

        logger.i(TAG, "Get exercise total weight lifted 2 -> isLoading state raised.")
        assertTrue { getExerciseTotalWeightLiftedState2[0].isLoading }

        logger.i(TAG, "Get exercise total weight lifted 2 -> isSuccessful state raised.")
        assertTrue { getExerciseTotalWeightLiftedState2[1].isSuccessful }

        logger.i(TAG, "Get exercise total weight lifted 2 -> data: ${getExerciseTotalWeightLiftedState2[1]}")
        logger.i(TAG, "Assert statistics total weight lifted 2 is invalid.")
        assertTrue { getExerciseTotalWeightLiftedState2[1].totalWeight == 0.0f }
    }
}

