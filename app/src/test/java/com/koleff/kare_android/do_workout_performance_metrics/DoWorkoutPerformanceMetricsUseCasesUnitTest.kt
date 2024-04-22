package com.koleff.kare_android.do_workout_performance_metrics

import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsLocalDataSource
import com.koleff.kare_android.data.model.dto.DoWorkoutExerciseSetDto
import com.koleff.kare_android.data.model.dto.DoWorkoutPerformanceMetricsDto
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.repository.DoWorkoutPerformanceMetricsRepositoryImpl
import com.koleff.kare_android.data.repository.DoWorkoutRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.entity.DoWorkoutExerciseSet
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
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
import com.koleff.kare_android.domain.usecases.DeleteDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
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
import com.koleff.kare_android.domain.usecases.GetExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
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
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.exercise.ExerciseFakeDataSource
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.WorkoutFakeDataSource
import com.koleff.kare_android.workout.WorkoutUseCasesUnitTest
import com.koleff.kare_android.workout.data.WorkoutDaoFake
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFake
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.RepeatedTest
import java.util.Date
import java.util.UUID

typealias DoWorkoutPerformanceMetricsFakeDataSource = DoWorkoutPerformanceMetricsLocalDataSource

class DoWorkoutPerformanceMetricsUseCasesUnitTest {


    companion object {
        private lateinit var exerciseDBManager: ExerciseDBManager

        private lateinit var exerciseSetDao: ExerciseSetDaoFake
        private lateinit var exerciseDao: ExerciseDaoFake
        private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake
        private lateinit var workoutDao: WorkoutDaoFake
        private lateinit var workoutDetailsDao: WorkoutDetailsDaoFake
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
    }

    @BeforeEach
    fun setup() = runTest {
        logger = TestLogger(isLogging)

        //Exercise
        exerciseSetDao = ExerciseSetDaoFake()
        exerciseDetailsDao = ExerciseDetailsDaoFake()
        exerciseDao = ExerciseDaoFake(
            exerciseSetDao = exerciseSetDao,
            exerciseDetailsDao = exerciseDetailsDao,
            logger = logger
        )

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
            getExercisesUseCase = GetExercisesUseCase(exerciseRepository),
            addNewExerciseSetUseCase = AddNewExerciseSetUseCase(exerciseRepository),
            deleteExerciseSetUseCase = DeleteExerciseSetUseCase(exerciseRepository)
        )

        //Workout
        workoutDao = WorkoutDaoFake()
        workoutDetailsDao = WorkoutDetailsDaoFake(exerciseDao = exerciseDao, logger = logger)

        workoutFakeDataSource = WorkoutFakeDataSource(
            workoutDao = workoutDao,
            exerciseDao = exerciseDao,
            workoutDetailsDao = workoutDetailsDao,
            exerciseSetDao = exerciseSetDao,
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
            createCustomWorkoutDetailsUseCase = CreateCustomWorkoutDetailsUseCase(workoutRepository)
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
        exerciseDBManager = ExerciseDBManager(
            exerciseSetDao = exerciseSetDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseDao = exerciseDao,
            hasInitializedDB = false
        )

        exerciseDBManager.initializeExerciseTable {
            logger.i(TAG, "DB initialized successfully!")
        }

        //Workout DB
        workout1 = MockupDataGenerator.generateWorkoutDetails(isGenerateSetId = true).copy(workoutId = 1)
        workout2 = MockupDataGenerator.generateWorkoutDetails(isGenerateSetId = true).copy(workoutId = 2)

        workoutUseCases.createCustomWorkoutDetailsUseCase(workout1).collect()
        workoutUseCases.createCustomWorkoutDetailsUseCase(workout2).collect()
    }

    @Nested
    inner class DoWorkoutPerformanceMetricsSimpleTests {


        //TODO: same workoutId issue for workout1 and workout2...
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
                assertTrue { savePerformanceMetricsState2[0].isLoading }

                logger.i(
                    TAG,
                    "Get do workout performance metrics for workout 2 -> isSuccessful state raised."
                )
                assertTrue { savePerformanceMetricsState2[1].isSuccessful }

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
                    workoutDetails.workoutId
                )
                    .toList()[1]
                    .doWorkoutPerformanceMetricsList.first()

            return fetchedDBEntry
        }

        @RepeatedTest(50)
        @DisplayName("validate that do workout performance metrics are set up correctly")
        fun complexSetup() = runTest {
            assertNotNull(performanceMetricsWorkout1)
            assertNotNull(performanceMetricsWorkout2)
            assertEquals(workout1.workoutId, performanceMetricsWorkout1.workoutId)
            assertEquals(workout2.workoutId, performanceMetricsWorkout2.workoutId)
            assertEquals(performanceMetricsWorkout1.id, 1)
            assertEquals(performanceMetricsWorkout2.id, 2)
        }

        @RepeatedTest(50)
        @DisplayName("save workout exercise set using saveDoWorkoutExerciseSetUseCase")
        fun testSaveWorkoutExerciseSet() = runTest {

            //Save workout exercise set for workout 1
            val selectedExercise = workout1.exercises.random()
            val firstSet = selectedExercise.sets[0]
            val workoutExerciseSet = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout1.id,
                workoutId = performanceMetricsWorkout1.workoutId,
                exerciseId = selectedExercise.exerciseId,
                templateSetId = firstSet.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = 999,
                weight = 777f,
                time = null,
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
            val fetchWorkoutExerciseSet =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    performanceMetricsWorkout1.workoutId
                ).toList()

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 -> isLoading state raised."
            )
            assertTrue { fetchWorkoutExerciseSet[0].isLoading }

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 1 -> isSuccessful state raised."
            )
            assertTrue { fetchWorkoutExerciseSet[1].isSuccessful }

            val fetchedWorkoutPerformanceMetrics =
                fetchWorkoutExerciseSet[1].doWorkoutPerformanceMetricsList.first()
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
            val firstSetWorkout2 = selectedExercise.sets[0]
            val workoutExerciseSet2 = DoWorkoutExerciseSetDto(
                instanceId = UUID.randomUUID(),
                workoutPerformanceMetricsId = performanceMetricsWorkout2.id,
                workoutId = performanceMetricsWorkout2.workoutId,
                exerciseId = selectedExerciseWorkout2.exerciseId,
                templateSetId = firstSetWorkout2.setId
                    ?: throw IllegalArgumentException("Invalid exercise set id"),
                reps = 123,
                weight = 321f,
                time = null,
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
            val fetchWorkoutExerciseSet2 =
                doWorkoutPerformanceMetricsUseCases.getDoWorkoutPerformanceMetricsUseCase(
                    performanceMetricsWorkout2.workoutId
                ).toList()

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 2 -> isLoading state raised."
            )
            assertTrue { fetchWorkoutExerciseSet2[0].isLoading }

            logger.i(
                TAG,
                "Get do workout performance metrics for workout 2 -> isSuccessful state raised."
            )
            assertTrue { fetchWorkoutExerciseSet2[1].isSuccessful }

            val fetchedWorkoutPerformanceMetrics4 =
                fetchWorkoutExerciseSet2[1].doWorkoutPerformanceMetricsList.first()
            logger.i(
                TAG,
                "Fetched do workout performance metrics after exercise sets update for workout 2: $fetchedWorkoutPerformanceMetrics4"
            )

            logger.i(
                TAG,
                "Assert do workout exercise sets are not empty for workout 2-> update has happened"
            )
            assertTrue { fetchedWorkoutPerformanceMetrics4.doWorkoutExerciseSets.isNotEmpty() }

            //TODO: Assert workout 1 is not changed after insertion in workout 2...

            //TODO: update performance metrics with new set using UpdateDoWorkoutPerformanceMetricsUseCase

            //TODO: Add two more sets using SaveAllDoWorkoutExerciseSetUseCase to workout 1 and fetch again

            //TODO: delete performance metrics and check if sets are also deleted (after fetch) using DeleteDoWorkoutPerformanceMetricsUseCase
        }
    }
}
