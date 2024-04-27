package com.koleff.kare_android.do_workout

import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.DoWorkoutLocalDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseSetDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.repository.DoWorkoutRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.do_workout.data.TimerUtilFake
import com.koleff.kare_android.do_workout_performance_metrics.DoWorkoutPerformanceMetricsUseCasesUnitTest
import com.koleff.kare_android.domain.repository.DoWorkoutRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeselectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutInitialSetupUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutUseCases
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.ResetTimerUseCase
import com.koleff.kare_android.domain.usecases.SelectNextExerciseUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SkipNextExerciseUseCase
import com.koleff.kare_android.domain.usecases.StartTimerUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.UpdateExerciseSetsAfterTimerUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.exercise.ExerciseFakeDataSource
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.ui.state.DoWorkoutData
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.WorkoutFakeDataSource
import com.koleff.kare_android.workout.data.WorkoutDaoFake
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFake
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.RepeatedTest

typealias DoWorkoutFakeDataSource = DoWorkoutLocalDataSource

class DoWorkoutUseCasesUnitTest {

    companion object {
        private const val TAG = "DoWorkoutUseCaseUnitText"

        private lateinit var exerciseDBManager: ExerciseDBManager

        private lateinit var exerciseSetDao: ExerciseSetDaoFake
        private lateinit var exerciseDao: ExerciseDaoFake
        private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake
        private lateinit var workoutDao: WorkoutDaoFake
        private lateinit var workoutDetailsDao: WorkoutDetailsDaoFake

        private lateinit var exerciseFakeDataSource: ExerciseFakeDataSource
        private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
        private lateinit var doWorkoutFakeDataSource: DoWorkoutFakeDataSource

        private lateinit var exerciseRepository: ExerciseRepository
        private lateinit var workoutRepository: WorkoutRepository
        private lateinit var doWorkoutRepository: DoWorkoutRepository

        private lateinit var exerciseUseCases: ExerciseUseCases
        private lateinit var workoutUseCases: WorkoutUseCases
        private lateinit var doWorkoutUseCases: DoWorkoutUseCases

        private lateinit var timer: TimerUtilFake

        private val useMockupDataSource = false
        private val isErrorTesting = false

        private val isLogging = true
        private lateinit var logger: TestLogger

        private var hasInitializedWorkoutDetails = false
        @JvmStatic
        @BeforeAll
        fun initialSetup(): Unit = runBlocking {
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

            //Do workout
            doWorkoutFakeDataSource = DoWorkoutFakeDataSource()
            doWorkoutRepository = DoWorkoutRepositoryImpl(doWorkoutFakeDataSource)
            doWorkoutUseCases = DoWorkoutUseCases(
                doWorkoutInitialSetupUseCase = DoWorkoutInitialSetupUseCase(doWorkoutRepository),
                selectNextExerciseUseCase = SelectNextExerciseUseCase(doWorkoutRepository),
                skipNextExerciseUseCase = SkipNextExerciseUseCase(doWorkoutRepository),
                updateExerciseSetsAfterTimerUseCase = UpdateExerciseSetsAfterTimerUseCase(
                    doWorkoutRepository
                ),
                addNewExerciseSetUseCase = AddNewExerciseSetUseCase(exerciseRepository),
                deleteExerciseSetUseCase = DeleteExerciseSetUseCase(exerciseRepository),
                startTimerUseCase = StartTimerUseCase(),
                resetTimerUseCase = ResetTimerUseCase()
            )

            timer = TimerUtilFake()

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

            //Initialize 2 workout details used in tests
            if (!hasInitializedWorkoutDetails) {

                logger.i(TAG, "Workout details created successfully!")
                hasInitializedWorkoutDetails = true

                //Generate random workout details
                val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
                workoutUseCases.createCustomWorkoutDetailsUseCase(
                    workoutDetails
                ).toList()

                val workoutDetails2 = MockupDataGenerator.generateWorkoutDetails()
                workoutUseCases.createCustomWorkoutDetailsUseCase(
                    workoutDetails2
                ).toList()
            }
        }
    }

    @RepeatedTest(50)
    fun `add set using AddNewExerciseSetUseCase and remove set using DeleteExerciseSetUseCase`() =
        runTest {

            //Generate workout details and workout
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails(isGenerateSetId = true)
            logger.i(TAG, "Mocked workout details: $workoutDetails")

            //Insert workout to generate WorkoutDetails in DB
            val createCustomWorkoutDetailsState =
                workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

            logger.i(TAG, "Create custom workout details -> isLoading state raised.")
            assertTrue { createCustomWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Create custom workout details -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutDetailsState[1].isSuccessful }

            val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

            //Fetch workout details from DB
            val getWorkoutDetailsState =
                workoutUseCases.getWorkoutDetailsUseCase(savedWorkoutDetails.workoutId).toList()

            logger.i(TAG, "Get workout details -> isLoading state raised.")
            assertTrue { getWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Get workout details -> isSuccessful state raised.")
            assertTrue { getWorkoutDetailsState[1].isSuccessful }

            val fetchedWorkoutDetails = getWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Fetched workout details: $fetchedWorkoutDetails")

            //Choose exercise to add set
            val selectedExercise = fetchedWorkoutDetails.exercises[0]

            val addNewSetState =
                exerciseUseCases.addNewExerciseSetUseCase(
                    selectedExercise.exerciseId,
                    workoutDetails.workoutId
                ).toList()

            logger.i(TAG, "Add new exercise set -> isLoading state raised.")
            assertTrue { addNewSetState[0].isLoading }

            logger.i(TAG, "Add new exercise set -> isSuccessful state raised.")
            assertTrue { addNewSetState[1].isSuccessful }

            logger.i(TAG, "Assert new exercise set was added.")
            assertTrue { addNewSetState[1].exercise.sets.size == selectedExercise.sets.size + 1 }

            //Fetch the exercise from DB to see the changes
            val getExerciseState =
                exerciseUseCases.getExerciseUseCase(
                    selectedExercise.exerciseId,
                    workoutDetails.workoutId
                ).toList()

            logger.i(
                TAG,
                "Get exercise for exerciseId ${selectedExercise.exerciseId} -> isLoading state raised."
            )
            assertTrue { getExerciseState[0].isLoading }

            logger.i(
                TAG,
                "Get exercise for exerciseId ${selectedExercise.exerciseId} -> isSuccessful state raised."
            )
            assertTrue { getExerciseState[1].isSuccessful }

            val fetchedExercise = getExerciseState[1].exercise
            logger.i(TAG, "Fetched exercise: $fetchedExercise")

            logger.i(TAG, "Assert new set was saved in DB.")
            assertTrue { fetchedExercise.sets.size == selectedExercise.sets.size + 1 }

            logger.i(TAG, "Assert the same set was added as the payload.")
            assertTrue { fetchedExercise.sets.last() == addNewSetState[1].exercise.sets.last() }

            //Remove set
            val removeSetState =
                exerciseUseCases.deleteExerciseSetUseCase(
                    selectedExercise.exerciseId,
                    workoutDetails.workoutId,
                    selectedExercise.sets[0].setId
                        ?: throw NoSuchElementException("Invalid exercise set")
                ).toList()

            logger.i(TAG, "Delete exercise set -> isLoading state raised.")
            assertTrue { removeSetState[0].isLoading }

            logger.i(TAG, "Delete exercise set -> isSuccessful state raised.")
            assertTrue { removeSetState[1].isSuccessful }

            logger.i(TAG, "Exercise set was deleted.")
            assertTrue { removeSetState[1].exercise.sets.size == selectedExercise.sets.size }

            logger.i(TAG, "Assert first exercise set was deleted.")
            assertTrue { !removeSetState[1].exercise.sets.contains(selectedExercise.sets[0]) }

            //Fetch the exercise from DB to see the changes
            val getExerciseState2 =
                exerciseUseCases.getExerciseUseCase(
                    selectedExercise.exerciseId,
                    workoutDetails.workoutId
                ).toList()

            logger.i(
                TAG,
                "Get exercise for exerciseId ${selectedExercise.exerciseId} -> isLoading state raised."
            )
            assertTrue { getExerciseState2[0].isLoading }

            logger.i(
                TAG,
                "Get exercise for exerciseId ${selectedExercise.exerciseId} -> isSuccessful state raised."
            )
            assertTrue { getExerciseState2[1].isSuccessful }

            val fetchedExercise2 = getExerciseState2[1].exercise
            logger.i(TAG, "Fetched exercise: $fetchedExercise2")

            logger.i(TAG, "Assert exercise set was deleted in DB.")
            assertTrue { fetchedExercise2.sets.size == selectedExercise.sets.size }

            logger.i(TAG, "Assert first exercise set was not in payload.")
            assertTrue { !fetchedExercise2.sets.contains(selectedExercise.sets[0]) }
        }

    @RepeatedTest(3)
    fun `start timer using StartTimerUseCase test and reset timer using ResetTimerUseCase test`() =
        runTest {

            /**
             * Start timer test
             */
            val defaultTime = ExerciseTime(0, 1, 0)

            val startTimerState = doWorkoutUseCases.startTimerUseCase(timer, defaultTime)
                .take(5).toList()

            logger.i(TAG, "Assert there are exactly 5 updates.")
            assertTrue { startTimerState.size == 5 }

            logger.i(TAG, "Assert all states are successful.")
            assertTrue { startTimerState.all { it is ResultWrapper.Success } }

            val times = listOf(
                (startTimerState[0] as ResultWrapper.Success).data.time,
                (startTimerState[1] as ResultWrapper.Success).data.time,
                (startTimerState[2] as ResultWrapper.Success).data.time,
                (startTimerState[3] as ResultWrapper.Success).data.time,
                (startTimerState[4] as ResultWrapper.Success).data.time,
            )
            val expectedTimes = listOf(
                ExerciseTime(0, 1, 0),
                ExerciseTime(0, 0, 59),
                ExerciseTime(0, 0, 58),
                ExerciseTime(0, 0, 57),
                ExerciseTime(0, 0, 56)
            )

            logger.i(
                TAG,
                "Assert all times are as expected values.\nExpected times: $expectedTimes.\nTimes received from Timer: $times"
            )
            assertTrue { times == expectedTimes }

            /**
             * Reset timer test
             */
            val resetTimerState = doWorkoutUseCases.resetTimerUseCase(timer, defaultTime)
                .toList()

            logger.i(TAG, "Assert reset state is successful.")
            assertTrue { resetTimerState[0] is ResultWrapper.Success }

            val resetTime = (resetTimerState[0] as ResultWrapper.Success).data.time
            logger.i(TAG, "Assert reset time is as starting time. Reset time: $resetTime.")
            assertTrue { resetTime == defaultTime }
        }

    @RepeatedTest(5)
    @DisplayName("validate that do workout is set up correctly")
    fun validateComplexSetup() = runTest {
        val workoutDB =
            workoutUseCases.getAllWorkoutDetailsUseCase().toList()[1].workoutDetailsList

        logger.i(TAG, "Assert workout DB contains 2 workouts: Workout DB: $workoutDB.")
        assertTrue { workoutDB.size == 2 }

        val workoutExercises = workoutDB.map { it.exercises }
        logger.i(
            TAG,
            "Assert workout DB contains exercises for each workout: Workout exercises: $workoutExercises."
        )

        workoutExercises.forEach { exercises ->
            assertTrue(exercises.isNotEmpty(), "The list of exercises should not be empty")
        }
    }

    @RepeatedTest(50)
    fun `initial setup using DoWorkoutInitialSetupUseCase test`() = runTest {

        //Get workout details
        val randomWorkoutDetails = workoutUseCases.getAllWorkoutDetailsUseCase()
            .toList()[1].workoutDetailsList.random()
        logger.i(TAG, "Selected workout details for the test: $randomWorkoutDetails")

        //Validation before test
        logger.i(TAG, "Assert selected workout details has more than 1 exercise")
        assertTrue { randomWorkoutDetails.exercises.size > 1 }

        logger.i(TAG, "Assert first exercise has more than 1 set")
        assertTrue { randomWorkoutDetails.exercises.first().sets.size > 1 }

        //Do workout initial setup
        val doWorkoutInitialSetupState =
            doWorkoutUseCases.doWorkoutInitialSetupUseCase(randomWorkoutDetails).toList()
        logger.i(
            TAG,
            "Do workout initial setup for workout details with id ${randomWorkoutDetails.workoutId} -> isLoading state raised."
        )
        assertTrue { doWorkoutInitialSetupState[0].isLoading }

        logger.i(
            TAG,
            "Do workout initial setup for workout details with id ${randomWorkoutDetails.workoutId} -> isSuccessful state raised."
        )
        assertTrue { doWorkoutInitialSetupState[1].isSuccessful }

        val doWorkoutInitialSetupData = doWorkoutInitialSetupState[1].doWorkoutData
        logger.i(
            TAG,
            "Do workout initial setup data: $doWorkoutInitialSetupData"
        )

        logger.i(
            TAG,
            "Assert initially isWorkoutCompleted is false"
        )
        assertTrue { !doWorkoutInitialSetupData.isWorkoutCompleted }

        logger.i(
            TAG,
            "Assert workout details are not changed"
        )
        assertTrue { doWorkoutInitialSetupData.workout == randomWorkoutDetails }

        logger.i(
            TAG,
            "Assert current set is first set for the first exercise of the workout details"
        )
        val firstExerciseFirstSet = randomWorkoutDetails.exercises.first().sets.first()
        assertTrue { doWorkoutInitialSetupData.currentSet == firstExerciseFirstSet }

        logger.i(
            TAG,
            "Assert next set is second set for the first exercise of the workout details"
        )
        val firstExerciseSecondSet = randomWorkoutDetails.exercises.first().sets[1]
        assertTrue { doWorkoutInitialSetupData.nextSet == firstExerciseSecondSet }

        logger.i(
            TAG,
            "Assert current exercise is the same as the first exercise of the workout details"
        )
        val firstExercise = randomWorkoutDetails.exercises.first()
        assertTrue { doWorkoutInitialSetupData.currentExercise == firstExercise }

        logger.i(
            TAG,
            "Assert next exercise is the same as the second exercise of the workout details"
        )
        val secondExercise = randomWorkoutDetails.exercises[1]
        assertTrue { doWorkoutInitialSetupData.nextExercise == secondExercise }
    }

    //TODO: Update next exercise -> normal case test...

    @RepeatedTest(50)
    fun `initial setup for workout with 1 exercise using DoWorkoutInitialSetupUseCase test`() =
        runTest {
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(
                TAG,
                "Workout details: $workoutDetails"
            )

            val updatedExercises = mutableListOf(workoutDetails.exercises.first())

            logger.i(TAG, "Assert exercise has more than 1 set")
            assertTrue { updatedExercises.first().sets.size > 1 }

            val updatedWorkoutDetails =
                workoutDetails.copy(exercises = updatedExercises) //Only 1 exercise
            logger.i(
                TAG,
                "Updated workout details: $updatedWorkoutDetails"
            )

            val doWorkoutInitialSetupState =
                doWorkoutUseCases.doWorkoutInitialSetupUseCase(updatedWorkoutDetails).toList()
            logger.i(
                TAG,
                "Do workout initial setup for workout details with id ${updatedWorkoutDetails.workoutId} -> isLoading state raised."
            )
            assertTrue { doWorkoutInitialSetupState[0].isLoading }

            logger.i(
                TAG,
                "Do workout initial setup for workout details with id ${workoutDetails.workoutId} -> isSuccessful state raised."
            )
            assertTrue { doWorkoutInitialSetupState[1].isSuccessful }

            val doWorkoutInitialSetupData = doWorkoutInitialSetupState[1].doWorkoutData
            logger.i(
                TAG,
                "Do workout initial setup data: $doWorkoutInitialSetupData"
            )
        }

    /**
     * After Update next exercise for workout with 1 exercise with 1 set case:
     * - Current exercise stays the same
     * - Current set becomes next set (from initial setup)
     * - Next exercise stays the same
     * - Assert next set is 3rd set (if there are 3 sets) -> otherwise 1st set of nextExercise...
     * - Workout is not completed
     */
    @RepeatedTest(50)
    fun `update next exercise for workout with 1 exercise using UpdateExerciseSetsAfterTimerUseCase test`() =
        runTest {
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(
                TAG,
                "Workout details: $workoutDetails"
            )

            val updatedExercises = mutableListOf(workoutDetails.exercises.first())
            val updatedWorkoutDetails =
                workoutDetails.copy(exercises = updatedExercises) //1 exercise with multiple sets
            logger.i(
                TAG,
                "Updated workout details: $updatedWorkoutDetails"
            )

            logger.i(TAG, "Assert exercise has more than 1 set")
            assertTrue { updatedExercises.first().sets.size > 1 }

            val doWorkoutInitialSetupState =
                doWorkoutUseCases.doWorkoutInitialSetupUseCase(updatedWorkoutDetails).toList()
            val doWorkoutInitialSetupData = doWorkoutInitialSetupState[1].doWorkoutData
            logger.i(
                TAG,
                "Do workout initial setup data: $doWorkoutInitialSetupData"
            )

            val updateNextExerciseState =
                doWorkoutUseCases.updateExerciseSetsAfterTimerUseCase(doWorkoutInitialSetupData)
                    .toList()

            logger.i(
                TAG,
                "Update next exercise -> isSuccessful state raised."
            )
            assertTrue { updateNextExerciseState[0].isSuccessful }

            val selectNextExerciseData = updateNextExerciseState[0].doWorkoutData
            logger.i(
                TAG,
                "Update next exercise data: $selectNextExerciseData"
            )

            logger.i(
                TAG,
                "Assert current exercise has not changed."
            )
            assertEquals(
                updatedWorkoutDetails.exercises.first(),
                selectNextExerciseData.currentExercise
            ) //doWorkoutInitialSetupData.currentExercise

            logger.i(
                TAG,
                "Assert currentSet is second set."
            )
            val secondSet = updatedWorkoutDetails.exercises.first().sets[1]
            assertEquals(secondSet, selectNextExerciseData.currentExercise.sets[1])

            logger.i(
                TAG,
                "Assert currentSet is initial setup nextSet"
            )
            assertEquals(doWorkoutInitialSetupData.nextSet, selectNextExerciseData.currentSet)

            //Assert nextSet is 3rd set (if there is 3 sets) -> otherwise 1st set of nextExercise...
            logger.i(
                TAG,
                "Assert nextSet is third set."
            )
            val thirdSet = updatedWorkoutDetails.exercises.first().sets[2]
            assertEquals(thirdSet, selectNextExerciseData.currentExercise.sets[2])

            logger.i(
                TAG,
                "Assert there is no next exercise."
            )
            assertEquals(ExerciseDto(), selectNextExerciseData.nextExercise)


            logger.i(
                TAG,
                "Assert workout is not completed."
            )
            assertTrue { !selectNextExerciseData.isWorkoutCompleted }
        }


    /**
     * After update next exercise for workout with 2 exercises with 1 set each case:
     * - Current exercise becomes next exercise (from initial setup)
     * - Current set becomes next set (first set of next exercise)
     * - No next exercise
     * - No next set
     * - Workout is not completed
     */
    @RepeatedTest(50)
    fun `update next exercise for workout with 2 exercises with 1 set each using UpdateExerciseSetsAfterTimerUseCase test`() =
        runTest {
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(
                TAG,
                "Workout details: $workoutDetails"
            )

            logger.i(
                TAG,
                "Assert workout details has at least 2 exercises"
            )
            assertTrue { workoutDetails.exercises.size > 1 }

            val firstExercise = workoutDetails.exercises.first()
            val secondExercise = workoutDetails.exercises[1]
            val updatedSetsFirstExercise = mutableListOf(firstExercise.sets.first())
            val updatedSetsSecondExercise = mutableListOf(secondExercise.sets.first())

            val updatedExercises = mutableListOf(
                firstExercise.copy(
                    sets = updatedSetsFirstExercise
                ),
                secondExercise.copy(
                    sets = updatedSetsSecondExercise
                )
            )

            val updatedWorkoutDetails =
                workoutDetails.copy(exercises = updatedExercises) //2 exercises with 1 set each
            val doWorkoutInitialSetupState =
                doWorkoutUseCases.doWorkoutInitialSetupUseCase(updatedWorkoutDetails).toList()
            val doWorkoutInitialSetupData = doWorkoutInitialSetupState[1].doWorkoutData
            logger.i(
                TAG,
                "Do workout initial setup data: $doWorkoutInitialSetupData"
            )

            val updateNextExerciseState =
                doWorkoutUseCases.updateExerciseSetsAfterTimerUseCase(doWorkoutInitialSetupData)
                    .toList()

            logger.i(
                TAG,
                "Update next exercise -> isSuccessful state raised."
            )
            assertTrue { updateNextExerciseState[0].isSuccessful }

            val selectNextExerciseData = updateNextExerciseState[0].doWorkoutData
            logger.i(
                TAG,
                "Update next exercise data: $selectNextExerciseData"
            )

            logger.i(
                TAG,
                "Assert current exercise has changed to initial setup nextExercise."
            )
            assertEquals(
                doWorkoutInitialSetupData.nextExercise,
                selectNextExerciseData.currentExercise
            )

            //Only raised by SelectNextExerciseUseCase
//            logger.i(
//                TAG,
//                "Assert isNextExercise flag is raised."
//            )
//            assertTrue(selectNextExerciseData.isNextExercise) //This raises the flag for UpdateExerciseSetsAfterTimerUseCase

            logger.i(
                TAG,
                "Assert currentSet is initial setup nextSet"
            )
            assertEquals(doWorkoutInitialSetupData.nextSet, selectNextExerciseData.currentSet)

            //Assert nextSet is 3rd set (if there is 3 sets) -> otherwise 1st set of nextExercise...
            logger.i(
                TAG,
                "Assert there is no nextSet"
            )
            assertEquals(-1, selectNextExerciseData.nextSetNumber)

            logger.i(
                TAG,
                "Assert there is no next exercise."
            )
            assertEquals(ExerciseDto(), selectNextExerciseData.nextExercise)


            logger.i(
                TAG,
                "Assert workout is not completed"
            )
            assertTrue { !selectNextExerciseData.isWorkoutCompleted }
        }

    @RepeatedTest(50)
    fun `initial setup for workout with 1 set using DoWorkoutInitialSetupUseCase test`() =
        runTest {
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(
                TAG,
                "Workout details: $workoutDetails"
            )

            val firstExercise = workoutDetails.exercises.first()
            val updatedSets = mutableListOf(firstExercise.sets.first())
            val updatedExercises = mutableListOf(
                firstExercise.copy(
                    sets = updatedSets
                )
            )

            val updatedWorkoutDetails =
                workoutDetails.copy(exercises = updatedExercises) //Only 1 exercise with 1 set
            logger.i(
                TAG,
                "Updated workout details: $updatedWorkoutDetails"
            )

            val doWorkoutInitialSetupState =
                doWorkoutUseCases.doWorkoutInitialSetupUseCase(updatedWorkoutDetails).toList()
            logger.i(
                TAG,
                "Do workout initial setup for workout details with id ${updatedWorkoutDetails.workoutId} -> isLoading state raised."
            )
            assertTrue { doWorkoutInitialSetupState[0].isLoading }

            logger.i(
                TAG,
                "Do workout initial setup for workout details with id ${updatedWorkoutDetails.workoutId} -> isSuccessful state raised."
            )
            assertTrue { doWorkoutInitialSetupState[1].isSuccessful }

            val doWorkoutInitialSetupData = doWorkoutInitialSetupState[1].doWorkoutData
            logger.i(
                TAG,
                "Do workout initial setup data: $doWorkoutInitialSetupData"
            )

            //TODO: nextSet should be -1
            //TODO: currentSet should be 1
            //TODO: currentExercise should be the exercise (the only one)
            //TODO: nextExercise should be ExerciseDto()
        }

    /**
     * After update next exercise for workout with 1 exercise with 1 set case:
     * - No current exercise
     * - No current set
     * - No next exercise
     * - No next set
     * - Workout is completed
     */
    @RepeatedTest(50)
    fun `update next exercise for workout with 1 set using UpdateExerciseSetsAfterTimerUseCase test`() =
        runTest {
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(
                TAG,
                "Workout details: $workoutDetails"
            )

            val firstExercise = workoutDetails.exercises.first()
            val updatedSets = mutableListOf(firstExercise.sets.first())
            val updatedExercises = mutableListOf(
                firstExercise.copy(
                    sets = updatedSets
                )
            )

            val updatedWorkoutDetails =
                workoutDetails.copy(exercises = updatedExercises) //Only 1 exercise with 1 set
            val doWorkoutInitialSetupState =
                doWorkoutUseCases.doWorkoutInitialSetupUseCase(updatedWorkoutDetails).toList()
            val doWorkoutInitialSetupData = doWorkoutInitialSetupState[1].doWorkoutData
            logger.i(
                TAG,
                "Do workout initial setup data: $doWorkoutInitialSetupData"
            )

            val updateNextExerciseState =
                doWorkoutUseCases.updateExerciseSetsAfterTimerUseCase(doWorkoutInitialSetupData)
                    .toList()

            logger.i(
                TAG,
                "Update next exercise -> isSuccessful state raised."
            )
            assertTrue { updateNextExerciseState[0].isSuccessful }

            val selectNextExerciseData = updateNextExerciseState[0].doWorkoutData
            logger.i(
                TAG,
                "Update next exercise data: $selectNextExerciseData"
            )

            logger.i(
                TAG,
                "Assert there is no current exercise."
            )
            assertEquals(ExerciseDto(), selectNextExerciseData.currentExercise)

            logger.i(
                TAG,
                "Assert there is no currentSet"
            )
            assertTrue(selectNextExerciseData.currentSetNumber == -1)

            logger.i(
                TAG,
                "Assert there is no nextSet"
            )
            assertTrue(selectNextExerciseData.nextSetNumber == -1)

            logger.i(
                TAG,
                "Assert there is no next exercise."
            )
            assertEquals(ExerciseDto(), selectNextExerciseData.nextExercise)

            logger.i(
                TAG,
                "Assert workout is completed"
            )
            assertTrue { selectNextExerciseData.isWorkoutCompleted }
        }

    @RepeatedTest(50)
    fun `select next exercise with invalid data using SelectNextExerciseUseCase test`() = runTest {
        val invalidDoWorkoutData = DoWorkoutData()
        val updateNextExerciseState =
            doWorkoutUseCases.selectNextExerciseUseCase(invalidDoWorkoutData).toList()

        logger.i(
            TAG,
            "Update next exercise -> isError state raised."
        )
        assertTrue { updateNextExerciseState[0].isError }

        logger.i(
            TAG,
            "Assert error is KareError.INVALID_WORKOUT"
        )
        assertTrue { updateNextExerciseState[0].error == KareError.INVALID_WORKOUT }
    }

    @RepeatedTest(50)
    fun `update next exercise with invalid data using UpdateExerciseSetsAfterTimerUseCase test`() =
        runTest {
            val invalidDoWorkoutData = DoWorkoutData()
            val updateNextExerciseState =
                doWorkoutUseCases.updateExerciseSetsAfterTimerUseCase(invalidDoWorkoutData).toList()

            logger.i(
                TAG,
                "Update next exercise -> isError state raised."
            )
            assertTrue { updateNextExerciseState[0].isError }

            logger.i(
                TAG,
                "Assert error is KareError.INVALID_EXERCISE"
            )
            assertTrue { updateNextExerciseState[0].error == KareError.INVALID_EXERCISE }
        }

    @RepeatedTest(50)
    fun `initial setup with invalid data using DoWorkoutInitialSetupUseCase test`() = runTest {
        val invalidWorkoutDetails = WorkoutDetailsDto()
        logger.i(
            TAG,
            "Invalid workout details: $invalidWorkoutDetails"
        )

        val doWorkoutInitialSetupState =
            doWorkoutUseCases.doWorkoutInitialSetupUseCase(invalidWorkoutDetails).toList()
        logger.i(
            TAG,
            "Do workout initial setup with invalid workout details -> isLoading state raised."
        )
        assertTrue { doWorkoutInitialSetupState[0].isLoading }

        logger.i(
            TAG,
            "Do workout initial setup with invalid workout details -> isError state raised."
        )
        assertTrue { doWorkoutInitialSetupState[1].isError }

        logger.i(
            TAG,
            "Assert error is KareError.WORKOUT_HAS_NO_EXERCISES"
        )
        assertTrue { doWorkoutInitialSetupState[1].error == KareError.WORKOUT_HAS_NO_EXERCISES }
    }
}