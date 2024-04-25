package com.koleff.kare_android.do_workout

import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.DoWorkoutLocalDataSource
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.repository.DoWorkoutRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.do_workout.data.TimerUtilFake
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
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.WorkoutFakeDataSource
import com.koleff.kare_android.workout.data.WorkoutDaoFake
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFake
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest

typealias DoWorkoutFakeDataSource = DoWorkoutLocalDataSource

class DoWorkoutUseCasesUnitTest {
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

    companion object {
        private const val TAG = "DoWorkoutUseCaseUnitText"
    }

    @BeforeEach
    fun setup() = runBlocking {
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

        timer = TimerUtilFake()
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
}