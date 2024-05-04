package com.koleff.kare_android.do_workout_performance_metrics

import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsLocalDataSource
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.repository.DoWorkoutPerformanceMetricsRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.manager.ExerciseDBManagerV2
import com.koleff.kare_android.do_workout.DoWorkoutUseCasesUnitTest
import com.koleff.kare_android.do_workout_performance_metrics.data.DoWorkoutExerciseSetDaoFake
import com.koleff.kare_android.do_workout_performance_metrics.data.DoWorkoutPerformanceMetricsDaoFake
import com.koleff.kare_android.do_workout_performance_metrics.data.DoWorkoutPerformanceMetricsMediator
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeselectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutPerformanceMetricsUseCases
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetAllDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SaveAllDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.UpdateDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.exercise.ExerciseFakeDataSource
import com.koleff.kare_android.exercise.data.ExerciseDaoFakeV2
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.WorkoutFakeDataSource
import com.koleff.kare_android.workout.data.CompositeExerciseSetChangeListener
import com.koleff.kare_android.workout.data.WorkoutConfigurationDaoFake
import com.koleff.kare_android.workout.data.WorkoutDaoFakeV2
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFakeV2
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.RepeatedTest
import java.util.Date
import java.util.UUID
import kotlin.random.Random

typealias DoWorkoutPerformanceMetricsFakeDataSource = DoWorkoutPerformanceMetricsLocalDataSource

class DoWorkoutPerformanceMetricsUseCasesUnitTest {

    companion object {
        private lateinit var exerciseDBManager: ExerciseDBManagerV2

        private lateinit var exerciseSetDao: ExerciseSetDaoFake
        private lateinit var exerciseDao: ExerciseDaoFakeV2
        private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake
        private lateinit var workoutDao: WorkoutDaoFakeV2
        private lateinit var workoutDetailsDao: WorkoutDetailsDaoFakeV2
        private lateinit var workoutConfigurationDao: WorkoutConfigurationDaoFake
        private lateinit var doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDaoFake
        private lateinit var doWorkoutExerciseSetDao: DoWorkoutExerciseSetDaoFake
        private lateinit var doWorkoutPerformanceMetricsMediator: DoWorkoutPerformanceMetricsMediator

        private lateinit var exerciseFakeDataSource: ExerciseFakeDataSource
        private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
        private lateinit var doWorkoutPerformanceMetricsDataSource: DoWorkoutPerformanceMetricsFakeDataSource
        private lateinit var exerciseRepository: ExerciseRepository
        private lateinit var workoutRepository: WorkoutRepository
        private lateinit var doWorkoutPerformanceMetricsRepository: DoWorkoutPerformanceMetricsRepository

        private lateinit var exerciseUseCases: ExerciseUseCases
        private lateinit var workoutUseCases: WorkoutUseCases
        private lateinit var doWorkoutPerformanceMetricsUseCases: DoWorkoutPerformanceMetricsUseCases

        private val useMockupDataSource = false
        private val isErrorTesting = false

        private lateinit var workout1: WorkoutDetailsDto
        private lateinit var workout2: WorkoutDetailsDto

        private val isLogging = true
        private lateinit var logger: TestLogger

        private const val TAG = "DoWorkoutPerformanceMetricsUseCasesUnitTest"

        @JvmStatic
        @BeforeAll
        fun setup() = runTest {
            logger = TestLogger(isLogging)

            //DAOs
            workoutDetailsDao = WorkoutDetailsDaoFakeV2()
            exerciseDao = ExerciseDaoFakeV2(workoutDetailsDao)

            val compositeExerciseSetChangeListener1 = CompositeExerciseSetChangeListener()
            compositeExerciseSetChangeListener1.addListener(exerciseDao)
            compositeExerciseSetChangeListener1.addListener(workoutDetailsDao)
            exerciseSetDao = ExerciseSetDaoFake(compositeExerciseSetChangeListener1)

            val compositeExerciseSetChangeListener2 = CompositeExerciseSetChangeListener()
            compositeExerciseSetChangeListener2.addListener(exerciseDao)
            compositeExerciseSetChangeListener2.addListener(exerciseSetDao)
            workoutDetailsDao.setExerciseSetChangeListeners(compositeExerciseSetChangeListener2)

            exerciseDetailsDao = ExerciseDetailsDaoFake()
            workoutDao = WorkoutDaoFakeV2(
                exerciseChangeListener = workoutDetailsDao,
                workoutConfigurationChangeListener = workoutDetailsDao,
                workoutDetailsChangeListener = workoutDetailsDao
            )
            workoutConfigurationDao = WorkoutConfigurationDaoFake(workoutDetailsDao)

            //Exercise
            exerciseFakeDataSource = ExerciseFakeDataSource(
                exerciseDao = exerciseDao,
                exerciseDetailsDao = exerciseDetailsDao,
                exerciseSetDao = exerciseSetDao
            )

            exerciseRepository = ExerciseRepositoryImpl(exerciseFakeDataSource)

            exerciseUseCases = ExerciseUseCases(
                onSearchExerciseUseCase = OnSearchExerciseUseCase(),
                onFilterExercisesUseCase = OnFilterExercisesUseCase(),
                getExerciseDetailsUseCase = GetExerciseDetailsUseCase(exerciseRepository),
                getCatalogExercisesUseCase = GetCatalogExercisesUseCase(exerciseRepository),
                getCatalogExerciseUseCase = GetCatalogExerciseUseCase(exerciseRepository),
                getExerciseUseCase = GetExerciseUseCase(exerciseRepository),
                addNewExerciseSetUseCase = AddNewExerciseSetUseCase(exerciseRepository),
                deleteExerciseSetUseCase = DeleteExerciseSetUseCase(exerciseRepository)
            )

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
                addExerciseUseCase = AddExerciseUseCase(workoutRepository),
                submitExerciseUseCase = SubmitExerciseUseCase(workoutRepository),
                deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
                selectWorkoutUseCase = SelectWorkoutUseCase(workoutRepository),
                deselectWorkoutUseCase = DeselectWorkoutUseCase(workoutRepository),
                getSelectedWorkoutUseCase = GetSelectedWorkoutUseCase(workoutRepository),
                createNewWorkoutUseCase = CreateNewWorkoutUseCase(workoutRepository),
                createCustomWorkoutUseCase = CreateCustomWorkoutUseCase(workoutRepository),
                createCustomWorkoutDetailsUseCase = CreateCustomWorkoutDetailsUseCase(
                    workoutRepository
                ),
                getWorkoutConfigurationUseCase = GetWorkoutConfigurationUseCase(workoutRepository),
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

            //Do workout performance metrics
//        doWorkoutExerciseSetDao = DoWorkoutExerciseSetDaoFake()
//        doWorkoutPerformanceMetricsDao = DoWorkoutPerformanceMetricsDaoFake(doWorkoutExerciseSetDao)
//        doWorkoutPerformanceMetricsDataSource = DoWorkoutPerformanceMetricsFakeDataSource(
//            doWorkoutPerformanceMetricsDao,
//            doWorkoutExerciseSetDao
//        )
            doWorkoutPerformanceMetricsMediator =
                DoWorkoutPerformanceMetricsMediator() //Fixes circular dependency
            doWorkoutPerformanceMetricsDataSource = DoWorkoutPerformanceMetricsFakeDataSource(
                doWorkoutPerformanceMetricsMediator,
                doWorkoutPerformanceMetricsMediator
            )

            doWorkoutPerformanceMetricsRepository =
                DoWorkoutPerformanceMetricsRepositoryImpl(doWorkoutPerformanceMetricsDataSource)
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
    }


    @BeforeEach
    fun initializeDB() = runTest {
        exerciseDBManager.initializeExerciseTable {
            logger.i(TAG, "DB initialized successfully!")
        }

        //Workout DB
        workout1 =
            MockupDataGeneratorV2.generateWorkoutDetails(
                enableSetIdGeneration = true,
                workoutId = 1
            )
        workout2 =
            MockupDataGeneratorV2.generateWorkoutDetails(
                enableSetIdGeneration = true,
                workoutId = 2
            )

        workoutUseCases.createCustomWorkoutDetailsUseCase(workout1).toList()
        workoutUseCases.createCustomWorkoutDetailsUseCase(workout2).toList()
    }

    @AfterEach
    fun tearDown() = runTest {
        exerciseDao.clearDB()
        exerciseSetDao.clearDB()
        workoutDetailsDao.clearDB()
        workoutDao.clearDB()
        workoutConfigurationDao.clearDB()

        logger.i("tearDown", "DB cleared!")
        logger.i("tearDown", "ExerciseDao: ${exerciseDao.getAllExercises()}")
        logger.i(
            "tearDown",
            "WorkoutDetailsDao: ${workoutDetailsDao.getWorkoutDetailsOrderedById()}"
        )
        logger.i(
            "tearDown",
            "WorkoutDao: ${workoutDao.getWorkoutsOrderedById()}"
        )
        logger.i(
            "tearDown",
            "WorkoutConfigurationDao: ${workoutConfigurationDao.getAllWorkoutConfigurations()}"
        )
    }

    @Nested
    inner class DoWorkoutPerformanceMetricsSimpleTests {

        @RepeatedTest(100)
        @DisplayName("Save performance metrics using saveDoWorkoutExerciseSetUseCase and fetch using getDoWorkoutPerformanceMetricsUseCase, get all performance metrics using getAllDoWorkoutPerformanceMetricsUseCase")
        fun testSaveAndFetchPerformanceMetrics() =
            runTest {

                //Generate workout performance metrics for generated workout
                val workoutPerformanceMetrics = DoWorkoutPerformanceMetricsDto(
                    id = 0,
                    workoutId = workout1.workoutId,
                    doWorkoutExerciseSets = emptyList(),
                    date = Date()
                )

                val savePerformanceMetricsState =
                    doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
                        workoutPerformanceMetrics
                    ).toList()

                logger.i(
                    TAG,
                    "Save do workout performance metrics for workout 1 -> isLoading state raised."
                )
                assertTrue { savePerformanceMetricsState[0].isLoading }

                logger.i(
                    TAG,
                    "Save do workout performance metrics for workout 1 -> isSuccessful state raised."
                )
                assertTrue { savePerformanceMetricsState[1].isSuccessful }

                //Fetch workout performance metrics from DB
                val getPerformanceMetricsState =
                    doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                        workoutId = workoutPerformanceMetrics.workoutId
                    ).toList()

                logger.i(
                    TAG,
                    "Get do workout performance metrics for workout 1 -> isLoading state raised."
                )
                assertTrue { savePerformanceMetricsState[0].isLoading }

                logger.i(
                    TAG,
                    "Get do workout performance metrics for workout 1 -> isSuccessful state raised."
                )
                assertTrue { savePerformanceMetricsState[1].isSuccessful }

                val fetchedWorkoutPerformanceMetrics =
                    getPerformanceMetricsState[1].doWorkoutPerformanceMetricsList.first()
                logger.i(
                    TAG,
                    "Fetched do workout performance metrics for workout 1: $fetchedWorkoutPerformanceMetrics"
                )

                logger.i(
                    TAG,
                    "Assert fetched do workout performance metrics from DB are the same as saved ones -> workout 1."
                )
                assertTrue { workoutPerformanceMetrics.copy(id = 1) == fetchedWorkoutPerformanceMetrics } //Checking for id auto generating

                //Save another do workout performance metrics
                val workoutPerformanceMetrics2 = DoWorkoutPerformanceMetricsDto(
                    id = 0,
                    workoutId = workout2.workoutId,
                    doWorkoutExerciseSets = emptyList(),
                    date = Date()
                )

                val savePerformanceMetricsState2 =
                    doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
                        workoutPerformanceMetrics2
                    ).toList()

                logger.i(
                    TAG,
                    "Save do workout performance metrics for workout 2 -> isLoading state raised."
                )
                assertTrue { savePerformanceMetricsState2[0].isLoading }

                logger.i(
                    TAG,
                    "Save do workout performance metrics for workout 2 -> isSuccessful state raised."
                )
                assertTrue { savePerformanceMetricsState2[1].isSuccessful }

                //Fetch the whole DB
                val getAllPerformanceMetricsState =
                    doWorkoutPerformanceMetricsUseCases.getAllDoWorkoutPerformanceMetricsUseCase()
                        .toList()

                logger.i(TAG, "Get all do workout performance metrics -> isLoading state raised.")
                assertTrue { getAllPerformanceMetricsState[0].isLoading }

                logger.i(
                    TAG,
                    "Get all do workout performance metrics -> isSuccessful state raised."
                )
                assertTrue { getAllPerformanceMetricsState[1].isSuccessful }

                val performanceMetricsDB =
                    getAllPerformanceMetricsState[1].doWorkoutPerformanceMetricsList

                logger.i(TAG, "Assert there are 2 entries in the do workout performance metrics DB")
                assertTrue { performanceMetricsDB.size == 2 }

                //Fetch only for workout 2
                val getPerformanceMetricsState2 =
                    doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                        workoutId = workoutPerformanceMetrics2.workoutId
                    ).toList()

                logger.i(
                    TAG,
                    "Get do workout performance metrics for workout 2 -> isLoading state raised."
                )
                assertTrue { getPerformanceMetricsState2[0].isLoading }

                logger.i(
                    TAG,
                    "Get do workout performance metrics for workout 2 -> isSuccessful state raised."
                )
                assertTrue { getPerformanceMetricsState2[1].isSuccessful }

                val fetchedWorkoutPerformanceMetrics2 =
                    getPerformanceMetricsState2[1].doWorkoutPerformanceMetricsList.first()
                logger.i(
                    TAG,
                    "Fetched do workout performance metrics for workout 2: $fetchedWorkoutPerformanceMetrics2"
                )

                logger.i(
                    TAG,
                    "Assert auto generate id is working for fetched do workout performance metrics for workout 2."
                )
                assertTrue { workoutPerformanceMetrics2.copy(id = 2) == fetchedWorkoutPerformanceMetrics2 } //Checking for id auto generating

                logger.i(
                    TAG,
                    "Assert only one entry is fetched for workout 2"
                )
                assertTrue { getPerformanceMetricsState2[1].doWorkoutPerformanceMetricsList.size == 1 }
            }


        @RepeatedTest(50)
        @DisplayName("Delete invalid performance metrics using DeleteDoWorkoutExerciseSetUseCase")
        fun testDeleteInvalidPerformanceMetrics() =
            runTest {
                val deletePerformanceMetricsState =
                    doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
                        -1
                    ).toList()

                logger.i(
                    TAG,
                    "Delete do workout performance metrics for invalid workout -> isSuccess state raised. [Silent failure action]"
                )
                assertTrue { deletePerformanceMetricsState[0].isSuccessful }

//                logger.i(
//                    TAG,
//                    "Delete do workout performance metrics for invalid workout -> isError state raised."
//                )
//                assertTrue { deletePerformanceMetricsState[0].isError }
//                assertTrue { deletePerformanceMetricsState[0].error == KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND }
            }

        @RepeatedTest(50)
        @DisplayName("Fetch invalid performance metrics using GetDoWorkoutExerciseSetUseCase")
        fun testFetchInvalidPerformanceMetrics() =
            runTest {
                val fetchPerformanceMetricsState =
                    doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                        workoutId = -1,
                        null,
                        null
                    ).toList()

                logger.i(
                    TAG,
                    "Fetch do workout performance metrics for invalid workout via workoutId -> isLoading state raised."
                )
                assertTrue { fetchPerformanceMetricsState[0].isLoading }

//                logger.i(
//                    TAG,
//                    "Fetch do workout performance metrics for invalid workout via workoutId -> isSuccessful state raised."
//                )
//                assertTrue { fetchPerformanceMetricsState[1].isSuccessful }
//
//                logger.i(TAG, "Assert do workout performance metrics list is empty.")
//                assertTrue { fetchPerformanceMetricsState[1].doWorkoutPerformanceMetricsList.isEmpty() }

                logger.i(
                    TAG,
                    "Fetch do workout performance metrics for invalid workout via workoutId -> isError state raised."
                )
                assertTrue { fetchPerformanceMetricsState[1].isError }
                assertTrue { fetchPerformanceMetricsState[1].error == KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND }

                val fetchPerformanceMetricsState2 =
                    doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                        performanceMetricsId = -1
                    ).toList()

                logger.i(
                    TAG,
                    "Fetch do workout performance metrics for invalid workout via performanceMetricsId -> isLoading state raised."
                )
                assertTrue { fetchPerformanceMetricsState2[0].isLoading }

                logger.i(
                    TAG,
                    "Fetch do workout performance metrics for invalid workout via performanceMetricsId -> isError state raised."
                )
                assertTrue { fetchPerformanceMetricsState2[1].isError }

                logger.i(
                    TAG,
                    "Assert error is KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND."
                )
                assertTrue { fetchPerformanceMetricsState2[1].error == KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND }
            }
    }

    @Nested
    inner class DoWorkoutPerformanceMetricsComplexTests {

        private lateinit var performanceMetricsWorkout1: DoWorkoutPerformanceMetricsDto
        private lateinit var performanceMetricsWorkout2: DoWorkoutPerformanceMetricsDto

        @BeforeEach
        fun setup() = runTest {
            performanceMetricsWorkout1 = preparePerformanceMetrics(workout1)
            performanceMetricsWorkout2 = preparePerformanceMetrics(workout2)
        }

        private suspend fun preparePerformanceMetrics(workoutDetails: WorkoutDetailsDto): DoWorkoutPerformanceMetricsDto {
            val performanceMetrics = DoWorkoutPerformanceMetricsDto(
                id = 0,
                workoutId = workoutDetails.workoutId,
                doWorkoutExerciseSets = emptyList(),
                date = Date()
            )

            doWorkoutPerformanceMetricsUseCases.saveDoWorkoutPerformanceMetricsUseCase(
                performanceMetrics
            ).toList()

            //Auto generate id in DB
            val fetchedDBEntry =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    workoutId = workoutDetails.workoutId,
                    null,
                    null
                )
                    .toList()[1]
                    .doWorkoutPerformanceMetricsList.first()

            return fetchedDBEntry
        }

        @RepeatedTest(50)
        @DisplayName("validate that do workout performance metrics are set up correctly")
        fun validateComplexSetup() = runTest {
            assertNotNull(performanceMetricsWorkout1)
            assertNotNull(performanceMetricsWorkout2)
            assertEquals(workout1.workoutId, performanceMetricsWorkout1.workoutId)
            assertEquals(workout2.workoutId, performanceMetricsWorkout2.workoutId)
            assertEquals(performanceMetricsWorkout1.id, 1)
            assertEquals(performanceMetricsWorkout2.id, 2)
        }

        @RepeatedTest(50)
        @DisplayName("save, saveAll, update and delete DoWorkoutExerciseSet and DoWorkoutPerformanceMetrics using use cases")
        fun testSaveWorkoutExerciseSet() = runTest {

            /**
             * Add single DoWorkoutExerciseSet using SaveDoWorkoutExerciseSetUseCase test
             */

            //Save workout exercise set for workout 1
            val selectedExerciseWorkout1 = workout1.exercises.random()
            val firstSetWorkout1 = selectedExerciseWorkout1.sets[0]
            val workoutExerciseSet = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout1.id,
                workoutId = performanceMetricsWorkout1.workoutId,
                exerciseId = selectedExerciseWorkout1.exerciseId,
                templateSetId = firstSetWorkout1.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = Random.nextInt(1, 20),
                weight = Random.nextInt(1, 200).toFloat(),
                time = null,
                isDone = false,
                date = Date()
            )

            val saveWorkoutExerciseSetState =
                doWorkoutPerformanceMetricsUseCases.saveDoWorkoutExerciseSetUseCase(
                    workoutExerciseSet
                )
                    .toList()
            logger.i(TAG, "Save do workout exercise set for workout 1 -> isLoading state raised.")
            assertTrue { saveWorkoutExerciseSetState[0].isLoading }

            logger.i(
                TAG,
                "Save do workout exercise set for workout 1 -> isSuccessful state raised."
            )
            assertTrue { saveWorkoutExerciseSetState[1].isSuccessful }

            //Fetch do workout exercise set for workout 1
            val fetchWorkoutExerciseSetsState1 =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    workoutId = performanceMetricsWorkout1.workoutId,
                    null,
                    null
                ).toList()

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 -> isLoading state raised."
            )
            assertTrue { fetchWorkoutExerciseSetsState1[0].isLoading }

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 -> isSuccessful state raised."
            )
            assertTrue { fetchWorkoutExerciseSetsState1[1].isSuccessful }

            val fetchedWorkoutPerformanceMetrics =
                fetchWorkoutExerciseSetsState1[1].doWorkoutPerformanceMetricsList.first()
            logger.i(
                TAG,
                "Fetched do workout performance metrics after exercise sets update for workout 1: $fetchedWorkoutPerformanceMetrics"
            )

            logger.i(
                TAG,
                "Assert do workout exercise sets are not empty for workout 2 -> update has happened"
            )
            assertTrue { fetchedWorkoutPerformanceMetrics.doWorkoutExerciseSets.isNotEmpty() }


            //Save workout exercise set for workout 2
            val selectedExerciseWorkout2 = workout2.exercises.random()
            val firstSetWorkout2 = selectedExerciseWorkout2.sets[0]
            val workoutExerciseSet2 = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout2.id,
                workoutId = performanceMetricsWorkout2.workoutId,
                exerciseId = selectedExerciseWorkout2.exerciseId,
                templateSetId = firstSetWorkout2.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = Random.nextInt(1, 20),
                weight = Random.nextInt(1, 200).toFloat(),
                time = null,
                isDone = false,
                date = Date()
            )

            val saveWorkoutExerciseSetState2 =
                doWorkoutPerformanceMetricsUseCases.saveDoWorkoutExerciseSetUseCase(
                    workoutExerciseSet2
                )
                    .toList()
            logger.i(TAG, "Save do workout exercise set for workout 2 -> isLoading state raised.")
            assertTrue { saveWorkoutExerciseSetState2[0].isLoading }

            logger.i(
                TAG,
                "Save do workout exercise set for workout 2 -> isSuccessful state raised."
            )
            assertTrue { saveWorkoutExerciseSetState2[1].isSuccessful }

            //Fetch do workout exercise set for workout 1
            val fetchWorkoutExerciseSetsState2 =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    workoutId = performanceMetricsWorkout2.workoutId,
                    null,
                    null
                ).toList()

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 2 -> isLoading state raised."
            )
            assertTrue { fetchWorkoutExerciseSetsState2[0].isLoading }

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 2 -> isSuccessful state raised."
            )
            assertTrue { fetchWorkoutExerciseSetsState2[1].isSuccessful }

            val fetchedWorkoutPerformanceMetrics4 =
                fetchWorkoutExerciseSetsState2[1].doWorkoutPerformanceMetricsList.first()
            logger.i(
                TAG,
                "Fetched do workout performance metrics after exercise sets update for workout 2: $fetchedWorkoutPerformanceMetrics4"
            )

            logger.i(
                TAG,
                "Assert do workout exercise sets are not empty for workout 2 -> update has happened"
            )
            assertTrue { fetchedWorkoutPerformanceMetrics4.doWorkoutExerciseSets.isNotEmpty() }

            val fetchWorkoutExerciseSetsState3 =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    workoutId = performanceMetricsWorkout1.workoutId,
                    null,
                    null
                ).toList()

            logger.i(
                TAG,
                "Assert workout 1 is not changed after insertion in workout 2"
            )
            assertTrue { fetchWorkoutExerciseSetsState3[1].doWorkoutPerformanceMetricsList == fetchWorkoutExerciseSetsState1[1].doWorkoutPerformanceMetricsList }

            /**
             * update DoWorkoutPerformanceMetrics using UpdateDoWorkoutPerformanceMetricsUseCase test
             */

            val selectedExercise2Workout2 = workout2.exercises.random()
            val firstSet2Workout2 = selectedExercise2Workout2.sets[0]
            val workoutExerciseSet3 = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout2.id,
                workoutId = performanceMetricsWorkout2.workoutId,
                exerciseId = selectedExercise2Workout2.exerciseId,
                templateSetId = firstSet2Workout2.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = Random.nextInt(1, 20),
                weight = Random.nextInt(1, 200).toFloat(),
                time = null,
                isDone = false,
                date = Date()
            )
            logger.i(
                TAG,
                "New set to be added to workout 2 do workout performance metrics: $workoutExerciseSet3"
            )

            val updatedExerciseSets =
                fetchedWorkoutPerformanceMetrics4.doWorkoutExerciseSets as MutableList
            updatedExerciseSets.add(workoutExerciseSet3)
            val updatedDoWorkoutPerformanceMetrics = fetchedWorkoutPerformanceMetrics4.copy(
                doWorkoutExerciseSets = updatedExerciseSets
            )

            val updatePerformanceMetricsState =
                doWorkoutPerformanceMetricsUseCases.updateDoWorkoutPerformanceMetricsUseCase(
                    updatedDoWorkoutPerformanceMetrics
                ).toList()

            logger.i(
                TAG,
                "Update do workout performance metrics for workout 2 with new do workout exercise set -> isLoading state raised."
            )
            assertTrue { updatePerformanceMetricsState[0].isLoading }

            logger.i(
                TAG,
                "Update do workout performance metrics for workout 2 with new do workout exercise set -> isSuccessful state raised."
            )
            assertTrue { updatePerformanceMetricsState[1].isSuccessful }

            logger.i(
                TAG,
                "Do workout performance metrics fetched from DB after update has happened and new set was added: ${updatePerformanceMetricsState[1].doWorkoutPerformanceMetrics}"
            )
            logger.i(
                TAG,
                "Assert performance metrics for workout 2 have been updated with new do workout exercise set"
            )
            assertTrue { updatePerformanceMetricsState[1].doWorkoutPerformanceMetrics == updatedDoWorkoutPerformanceMetrics }

            /**
             * Add multiple DoWorkoutExerciseSets using SaveAllDoWorkoutExerciseSetUseCase test
             */

            val selectedExercise2Workout1 = workout1.exercises.random()
            val firstSet2Workout1 = selectedExercise2Workout1.sets[0]
            val workoutExerciseSet4 = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout1.id,
                workoutId = performanceMetricsWorkout1.workoutId,
                exerciseId = selectedExercise2Workout1.exerciseId,
                templateSetId = firstSet2Workout1.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = Random.nextInt(1, 20),
                weight = Random.nextInt(1, 200).toFloat(),
                time = null,
                isDone = false,
                date = Date()
            )

            val selectedExercise3Workout1 = workout1.exercises.random()
            val firstSet3Workout1 = selectedExercise3Workout1.sets[0]
            val workoutExerciseSet5 = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout1.id,
                workoutId = performanceMetricsWorkout1.workoutId,
                exerciseId = selectedExercise3Workout1.exerciseId,
                templateSetId = firstSet3Workout1.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = Random.nextInt(1, 20),
                weight = Random.nextInt(1, 200).toFloat(),
                time = null,
                isDone = false,
                date = Date()
            )

            val newSetsWorkout1List = listOf(workoutExerciseSet4, workoutExerciseSet5)

            val saveAllSetsState =
                doWorkoutPerformanceMetricsUseCases.saveAllDoWorkoutExerciseSetUseCase(
                    newSetsWorkout1List
                ).toList()

            logger.i(
                TAG,
                "Save multiple do workout exercise sets for workout 1 -> isLoading state raised."
            )
            assertTrue { saveAllSetsState[0].isLoading }

            logger.i(
                TAG,
                "Save multiple do workout exercise sets for workout 1 -> isSuccessful state raised."
            )
            assertTrue { saveAllSetsState[1].isSuccessful }

            //Fetch to see the update in workout 1 do workout performance metrics
            val getPerformanceMetricsState =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    workoutId = performanceMetricsWorkout1.workoutId,
                    null,
                    null
                ).toList()

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 after multiple exercise sets insertion -> isLoading state raised."
            )
            assertTrue { getPerformanceMetricsState[0].isLoading }

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 after multiple exercise sets insertion -> isSuccessful state raised."
            )
            assertTrue { getPerformanceMetricsState[1].isSuccessful }

            val fetchedWorkoutPerformanceMetrics2 =
                getPerformanceMetricsState[1].doWorkoutPerformanceMetricsList.first()
            logger.i(
                TAG,
                "Fetched do workout performance metrics for workout 1 after multiple exercise sets insertion: $fetchedWorkoutPerformanceMetrics2"
            )

            logger.i(
                TAG,
                "Assert do workout performance metrics for workout 1 contains the newly added exercise sets."
            )
            assertTrue {
                fetchedWorkoutPerformanceMetrics2.doWorkoutExerciseSets.containsAll(
                    newSetsWorkout1List
                )
            }

            /**
             * Delete DoWorkoutPerformanceMetrics using DeleteDoWorkoutPerformanceMetricsUseCase test
             */

            val deletePerformanceMetricsState =
                doWorkoutPerformanceMetricsUseCases.deleteDoWorkoutPerformanceMetricsUseCase(
                    performanceMetricsWorkout1.id
                ).toList()

            logger.i(
                TAG,
                "Delete do workout performance metrics for workout 1 -> isSuccessful state raised."
            )
            assertTrue { deletePerformanceMetricsState[0].isSuccessful }

            //Fetch do workout performance metrics for deleted workout
            val getPerformanceMetricsState2 =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    performanceMetricsId = performanceMetricsWorkout1.id
                ).toList()

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 after deletion  -> isLoading state raised."
            )
            assertTrue { getPerformanceMetricsState2[0].isLoading }

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 after multiple exercise sets insertion -> isError state raised."
            )
            assertTrue { getPerformanceMetricsState2[1].isError }
            logger.i(
                TAG,
                "Assert error is KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND"
            )
            assertTrue { getPerformanceMetricsState2[1].error == KareError.DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND }

            //Check if do workout exercise sets are also deleted
            logger.i(
                TAG,
                "Assert do workout exercise sets are deleted for do workout performance metrics for workout 1 with id ${performanceMetricsWorkout1.id}"
            )
            val exerciseSetsAfterDelete =
                doWorkoutPerformanceMetricsMediator.findSetByPerformanceMetricsId(
                    performanceMetricsWorkout1.id
                )
            assertTrue {
                exerciseSetsAfterDelete.isEmpty()
            }
        }
    }
}
