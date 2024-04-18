package com.koleff.kare_android.workout

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeselectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.exercise.ExerciseUseCasesUnitTest
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.data.WorkoutDaoFake
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFake
import com.koleff.kare_android.workout.data.WorkoutMockupDataSource
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

typealias WorkoutFakeDataSource = WorkoutLocalDataSource

class WorkoutUseCasesUnitTest {
    private lateinit var exerciseDBManager: ExerciseDBManager

    private lateinit var workoutDao: WorkoutDaoFake
    private lateinit var workoutDetailsDao: WorkoutDetailsDaoFake
    private lateinit var exerciseDao: ExerciseDaoFake
    private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake
    private lateinit var exerciseSetDao: ExerciseSetDaoFake

    private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
    private lateinit var workoutMockupDataSource: WorkoutMockupDataSource
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var workoutUseCases: WorkoutUseCases

    private val useMockupDataSource = false
    private val isErrorTesting = false

    private val isLogging = true
    private lateinit var logger: TestLogger

    companion object {
        @JvmStatic
        fun provideSearchTexts(): Stream<Arguments> {
            val searchTexts = listOf(
                "Bench",
                "Squat",
                "Curl",
                "Press",
                "Dumbbell",
                "Bulgarian",
                "Push down",
                "Lateral",
                "Push up",
                "",
                " "
            )
           return searchTexts.map { text ->
                Arguments.of(text)
            }.stream()
        }

        private const val TAG = "WorkoutUseCasesUnitTest"
    }

    @BeforeEach
    fun setup() = runBlocking {
        logger = TestLogger(isLogging)

        exerciseSetDao = ExerciseSetDaoFake()
        exerciseDetailsDao = ExerciseDetailsDaoFake()
        exerciseDao = ExerciseDaoFake(
            exerciseSetDao = exerciseSetDao,
            exerciseDetailsDao = exerciseDetailsDao,
            logger = logger
        )

        workoutDao = WorkoutDaoFake()
        workoutDetailsDao = WorkoutDetailsDaoFake(exerciseDao = exerciseDao, logger = logger)

        workoutFakeDataSource = WorkoutFakeDataSource(
            workoutDao = workoutDao,
            exerciseDao = exerciseDao,
            workoutDetailsDao = workoutDetailsDao,
            exerciseSetDao = exerciseSetDao,
        )

        workoutMockupDataSource = WorkoutMockupDataSource(isError = isErrorTesting)

        workoutRepository =
            WorkoutRepositoryImpl(if (useMockupDataSource) workoutMockupDataSource else workoutFakeDataSource)

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

        //Initialize DB
        exerciseDBManager = ExerciseDBManager(
            exerciseSetDao = exerciseSetDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseDao = exerciseDao,
            hasInitializedDB = false
        )

        exerciseDBManager.initializeExerciseTable{
            logger.i(TAG, "DB initialized successfully!")
        }
    }

    @AfterEach
    fun tearDown() = runTest {
        exerciseDao.clearDB()
        exerciseSetDao.clearDB()
        workoutDetailsDao.clearDB()
        workoutDao.clearDB()

        logger.i("tearDown", "DB cleared!")
        logger.i("tearDown", "ExerciseDao: ${exerciseDao.getAllExercises()}")
        logger.i(
            "tearDown",
            "WorkoutDetailsDao: ${workoutDetailsDao.getWorkoutDetailsOrderedById()}"
        )
        logger.i(
            "tearDown",
            "WorkoutDetailsDao: ${workoutDetailsDao.getWorkoutExercisesWithSets()}"
        )
        logger.i(
            "tearDown",
            "WorkoutDao: ${workoutDao.getWorkoutsOrderedById()}"
        )
    }

    /**
     * Tested functions inside:
     *
     * CreateNewWorkoutUseCase()
     * WorkoutLocalDataSource.createNewWorkout()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.updateWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutsOrderedById()
     */
    @Test
    @DisplayName("Create workout using CreateNewWorkoutUseCase test")
    fun `create workout using CreateNewWorkoutUseCase test`() =
        runTest {
            val workouts = workoutDao.getWorkoutsOrderedById()
            val workoutsInDB = workouts.size

            val createWorkoutState =
                workoutUseCases.createNewWorkoutUseCase().toList()

            logger.i(TAG, "Create workout -> isLoading state raised.")
            assertTrue { createWorkoutState[0].isLoading }

            logger.i(TAG, "Create workout -> isSuccessful state raised.")
            assertTrue { createWorkoutState[1].isSuccessful }

            val workoutsAfterCreate = workoutDao.getWorkoutsOrderedById()
            val workoutsInDBAfterCreate = workoutsAfterCreate.size

            logger.i(TAG, "Assert new workout is created")
            assert(workoutsInDB + 1 == workoutsInDBAfterCreate)

            val createdWorkout = workoutsAfterCreate.stream()
                .filter {
                    !workouts.contains(it)
                }
                .findFirst()
                .orElseThrow()

            logger.i(TAG, "Assert Workout is not selected")
            assert(!createdWorkout.isSelected)

            logger.i(TAG, "Assert workoutId is the size of all workouts list")
            assert(createdWorkout.workoutId == workoutsInDBAfterCreate)

            logger.i(
                TAG,
                "Assert name is Workout $workoutsInDBAfterCreate -> all workouts in DB index"
            )
            assert(createdWorkout.name == "Workout $workoutsInDBAfterCreate")

            logger.i(TAG, "Assert no exercises are in Workout")
            assert(createdWorkout.totalExercises == 0)

            val createdWorkoutDetails =
                workoutDetailsDao.getWorkoutDetailsById(createdWorkout.workoutId)

            logger.i(TAG, "Assert no exercises are in WorkoutDetails")
            assert(createdWorkoutDetails?.exercises?.isEmpty() == true)

            logger.i(
                TAG,
                "Assert name is Workout $workoutsInDBAfterCreate -> all workouts in DB index"
            )
            assert(createdWorkoutDetails?.workoutDetails?.name == "Workout $workoutsInDBAfterCreate")

            //TODO: create workout when there is already workout in DB...
        }

    /**
     * Tested functions inside:
     *
     * GetAllWorkoutsUseCase()
     * WorkoutDao.getWorkoutsOrderedById()
     * WorkoutDao.insertWorkout()
     */
    @Test
    @DisplayName("Get workouts using GetAllWorkoutsUseCase test")
    fun `get workouts using GetAllWorkoutsUseCase test`() = runTest {

        //Fetch
        val getWorkoutsState = workoutUseCases.getAllWorkoutsUseCase().toList()

        logger.i(TAG, "Get workouts -> isLoading state raised.")
        assertTrue { getWorkoutsState[0].isLoading }

        logger.i(TAG, "Get workouts -> isSuccessful state raised.")
        assertTrue { getWorkoutsState[1].isSuccessful }

        logger.i(TAG, "Assert workout DB is initially empty.")
        assertTrue(getWorkoutsState[1].workoutList.isEmpty())

        //TODO: insert 2 workouts and fetch again...
    }

    /**
     * Tested functions inside:
     *
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
    @DisplayName("Get workout using GetWorkoutUseCase test")
    fun `get workout using GetWorkoutUseCase test`() = runTest {

        //Generate workout
        val workout = MockupDataGenerator.generateWorkout()
        logger.i(TAG, "Mocked workout: $workout")

        //Insert
        val id = workoutDao.insertWorkout(workout.toEntity())
        logger.i(TAG, "Mocked workout inserted successfully. Workout id: $id")

        //Fetch
        val getWorkoutState = workoutUseCases.getWorkoutUseCase(id.toInt())
            .toList() //Calls getWorkoutById internally...

        logger.i(TAG, "Get workout -> isLoading state raised.")
        assertTrue { getWorkoutState[0].isLoading }

        logger.i(TAG, "Get workout -> isSuccessful state raised.")
        assertTrue { getWorkoutState[1].isSuccessful }

        val fetchedWorkout = getWorkoutState[1].workout
        logger.i(TAG, "Fetched workout: $fetchedWorkout")

        logger.i(TAG, "Assert fetched workout is the same as inserted one.")
        assertTrue(fetchedWorkout == workout)
    }

    //TODO: [Test] get workout using GetWorkoutUseCase -> Throw error for invalid ID test

    /**
     * Tested functions inside:
     *
     * GetWorkoutDetailsUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * ExerciseDao.getExerciseByExerciseAndWorkoutId()
     * ---------------------
     * CreateCustomWorkoutDetailsUseCase()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * WorkoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef()
     * ExerciseSetDao.saveSet()
     * ExerciseSetDao.updateSet()
     * ExerciseDao.insertAllExerciseSetCrossRef()
     */
    @RepeatedTest(50)
    @DisplayName("Get workout details using GetWorkoutsDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test")
    fun `get workout details using GetWorkoutsDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test`() =
        runTest {

            //Generate workout details and workout
            val data = MockupDataGenerator.generateWorkoutAndWorkoutDetails(isGenerateSetId = true)

            val workout = data.first
            logger.i(TAG, "Mocked workout: $workout")

            val workoutDetails = data.second
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

            //Fetch
            val getWorkoutDetailsState =
                workoutUseCases.getWorkoutDetailsUseCase(savedWorkoutDetails.workoutId).toList()

            logger.i(TAG, "Get workout details -> isLoading state raised.")
            assertTrue { getWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Get workout details -> isSuccessful state raised.")
            assertTrue { getWorkoutDetailsState[1].isSuccessful }

            val fetchedWorkoutDetails = getWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Fetched workout details: $fetchedWorkoutDetails")

            val exercisesWithSets = workoutDetailsDao.getWorkoutExercisesWithSets().filter { it.workoutId == workout.workoutId }
            logger.i(TAG, "Fetched exercise with sets for the fetched workout: $exercisesWithSets")

            val fetchedWorkoutDetailsWithExercisesWithSets = fetchedWorkoutDetails.copy(
                exercises = exercisesWithSets as MutableList<ExerciseDto>
            )
            logger.i(
                TAG,
                "Fetched workout details with exercises with sets: $fetchedWorkoutDetailsWithExercisesWithSets"
            )

            logger.i(TAG, "Assert fetched workout details is the same as inserted one.")
            assertTrue(fetchedWorkoutDetailsWithExercisesWithSets == workoutDetails)
        }


    /**
     * Tested functions inside:
     *
     * CreateCustomWorkoutDetailsUseCase()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * WorkoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef()
     * ExerciseSetDao.saveSet()
     * ExerciseSetDao.updateSet()
     * ExerciseDao.insertAllExerciseSetCrossRef()
     * ---------------------
     * UpdateWorkoutDetailsUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById(workoutId)
     * WorkoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(crossRefs)
     * ExerciseSetDao.saveSet(exerciseSet)
     * ExerciseSetDao.updateSet(exerciseSet)
     * ExerciseDao.insertAllExerciseSetCrossRef(exerciseSetCrossRefs)
     * WorkoutDetailsDao.updateWorkoutDetails(workoutDetails.toEntity())
     * ---------------------
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
    @DisplayName("Update workout details using UpdateWorkoutDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test")
    fun `update workout details using UpdateWorkoutDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test`() =
        runTest {

            //Generate WorkoutDetails
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(TAG, "Mocked workout details: $workoutDetails")

            //Insert WorkoutDetails in DB
            val createCustomWorkoutDetailsState =
                workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

            logger.i(TAG, "Create custom workout details -> isLoading state raised.")
            assertTrue { createCustomWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Create custom workout details -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutDetailsState[1].isSuccessful }

            val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

            //Modify workoutDetails
            val muscleGroup = ExerciseGenerator.SUPPORTED_MUSCLE_GROUPS.random()
            val newExercise =
                MockupDataGenerator.generateExercise(muscleGroup = muscleGroup, workoutId = workoutDetails.workoutId, isWorkout = true)
                    .copy(name = "Test Exercise")
            val modifiedExercises = ArrayList(savedWorkoutDetails.exercises).apply {
                add(newExercise)
            }

            val modifiedWorkoutDetails = savedWorkoutDetails.copy(
                name = "Test Workout ${savedWorkoutDetails.workoutId}",
                exercises = modifiedExercises
            )

            //Update WorkoutDetails entry in DB with modified data
            val updateWorkoutDetailsState =
                workoutUseCases.updateWorkoutDetailsUseCase(modifiedWorkoutDetails).toList()

            logger.i(TAG, "Update workout details -> isLoading state raised.")
            assertTrue { updateWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Update workout details -> isSuccessful state raised.")
            assertTrue { updateWorkoutDetailsState[1].isSuccessful }

            val updateWorkoutDetails = updateWorkoutDetailsState[1].workoutDetails
            logger.i(
                TAG,
                "Mocked workout details inserted successfully. Updated workout details: $updateWorkoutDetails"
            )

            logger.i(
                TAG,
                "Assert modified workout details is the same as DB entry:"
            )
            assertTrue { modifiedWorkoutDetails == updateWorkoutDetails }

            //Check the Workout for the modified WorkoutDetails if updated
            val getWorkoutState =
                workoutUseCases.getWorkoutUseCase(updateWorkoutDetails.workoutId).toList()

            logger.i(TAG, "Get workout -> isLoading state raised.")
            assertTrue { getWorkoutState[0].isLoading }

            logger.i(TAG, "Get workout -> isSuccessful state raised.")
            assertTrue { getWorkoutState[1].isSuccessful }

            val fetchedWorkout = getWorkoutState[1].workout
            logger.i(TAG, "Fetched workout -> $fetchedWorkout")
            logger.i(TAG, "Fetched workout total exercises -> ${fetchedWorkout.totalExercises}")
            logger.i(
                TAG,
                "Updated workout details exercises -> ${updateWorkoutDetails.exercises.size}"
            )
            logger.i(
                TAG,
                "Workout details before update exercises -> ${workoutDetails.exercises.size}"
            )

            logger.i(
                TAG,
                "Assert fetched updated workout details from DB has the same exercises size as the workout DB entry totalExercises:"
            )
            assertTrue { updateWorkoutDetails.exercises.size == fetchedWorkout.totalExercises }

            logger.i(
                TAG,
                "Assert fetched updated workout details from DB has the same name as the workout DB entry name:"
            )
            assertTrue { updateWorkoutDetails.name == fetchedWorkout.name }
        }

    /**
     * Tested functions inside:
     *
     * CreateCustomWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * ----------------------------
     * UpdateWorkoutUseCase()
     * WorkoutDao.updateWorkout()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * WorkoutDetailsDao.updateWorkoutDetails()
     * ----------------------------
     * GetWorkoutDetailsUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * ExerciseDao.getExerciseByExerciseAndWorkoutId()
     */
    @RepeatedTest(50)
    @DisplayName("Update workout using UpdateWorkoutUseCase test and CreateCustomWorkoutUseCase test")
    fun `update workout using UpdateWorkoutUseCase test and CreateCustomWorkoutUseCase test`() =
        runTest {

            //Generate Workout
            val workout = MockupDataGenerator.generateWorkout()
            logger.i(TAG, "Mocked workout: $workout")

            //Insert Workout in DB
            val createCustomWorkoutState =
                workoutUseCases.createCustomWorkoutUseCase(workout).toList()

            logger.i(TAG, "Create custom workout -> isLoading state raised.")
            assertTrue { createCustomWorkoutState[0].isLoading }

            logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState[1].isSuccessful }

            val savedWorkout = createCustomWorkoutState[1].workout
            logger.i(TAG, "Saved workout: $savedWorkout")

            //Modify workout
            val modifiedWorkout = savedWorkout.copy(
                name = "Test Workout ${savedWorkout.workoutId}",
                totalExercises = savedWorkout.totalExercises + 7
            )

            //Update Workout entry in DB with modified data
            val updateWorkoutState =
                workoutUseCases.updateWorkoutUseCase(modifiedWorkout).toList()

            logger.i(TAG, "Update workout -> isLoading state raised.")
            assertTrue { updateWorkoutState[0].isLoading }

            logger.i(TAG, "Update workout -> isSuccessful state raised.")
            assertTrue { updateWorkoutState[1].isSuccessful }

            val updatedWorkout = updateWorkoutState[1].workout
            logger.i(
                TAG,
                "Mocked workout inserted successfully. Updated workout details: $updatedWorkout"
            )

            logger.i(
                TAG,
                "Assert modified workout is the same as DB entry:"
            )
            assertTrue { modifiedWorkout == updatedWorkout }

            //Check the WorkoutDetails for the modified Workout if updated
            val getWorkoutDetailsState =
                workoutUseCases.getWorkoutDetailsUseCase(updatedWorkout.workoutId).toList()

            logger.i(TAG, "Get workout details -> isLoading state raised.")
            assertTrue { getWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Get workout details -> isSuccessful state raised.")
            assertTrue { getWorkoutDetailsState[1].isSuccessful }

            val fetchedWorkoutDetails = getWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Fetched workout details -> $fetchedWorkoutDetails")
            logger.i(
                TAG,
                "Fetched workout details exercises -> ${fetchedWorkoutDetails.exercises.size}"
            )
            logger.i(TAG, "Updated workout total exercises -> ${updatedWorkout.totalExercises}")

            logger.i(
                TAG,
                "Assert fetched updated workout from DB has the same name as the workout details DB entry name:"
            )
            assertTrue { updatedWorkout.name == fetchedWorkoutDetails.name }
        }

    /**
     * Tested functions inside:
     *
     * CreateCustomWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * ------------------------------
     * DeleteWorkoutUseCase()
     * WorkoutDao.deleteWorkout(workoutId)
     * ------------------------
     * GetAllWorkoutsUseCase()
     * WorkoutLocalDataSource.getAllWorkouts()
     * WorkoutDao.getWorkoutsOrderedById()
     * WorkoutDao.insertWorkout()
     * ------------------------
     * GetAllWorkoutDetailsUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsOrderedById()
     * */
    @RepeatedTest(50)
    @DisplayName("Delete workout using DeleteWorkoutUseCase test")
    fun `delete workout using DeleteWorkoutUseCase test`() = runTest {

        //Generate Workout
        val workout = MockupDataGenerator.generateWorkout()
        logger.i(TAG, "Mocked workout: $workout")

        //Insert Workout in DB
        val createCustomWorkoutState = workoutUseCases.createCustomWorkoutUseCase(workout).toList()

        logger.i(TAG, "Create custom workout -> isLoading state raised.")
        assertTrue { createCustomWorkoutState[0].isLoading }

        logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
        assertTrue { createCustomWorkoutState[1].isSuccessful }

        val savedWorkout = createCustomWorkoutState[1].workout
        logger.i(TAG, "Saved workout: $savedWorkout")

        //Delete workout
        val deleteWorkoutState =
            workoutUseCases.deleteWorkoutUseCase(savedWorkout.workoutId).toList()

        logger.i(TAG, "Delete workout -> isLoading state raised.")
        assertTrue { deleteWorkoutState[0].isLoading }

        logger.i(TAG, "Delete workout -> isSuccessful state raised.")
        assertTrue { deleteWorkoutState[1].isSuccessful }

        //Fetch all workout DB entries
        val getAllWorkoutsState = workoutUseCases.getAllWorkoutsUseCase().toList()

        logger.i(TAG, "Get all workouts -> isLoading state raised.")
        assertTrue { getAllWorkoutsState[0].isLoading }

        logger.i(TAG, "Get all workouts -> isSuccessful state raised.")
        assertTrue { getAllWorkoutsState[1].isSuccessful }

        val workoutDB = getAllWorkoutsState[1].workoutList
        logger.i(TAG, "Workout DB: $workoutDB")

        logger.i(TAG, "Assert workout DB is empty.")
        assertTrue { workoutDB.isEmpty() }

        //Fetch all workout details DB entries
        val getAllWorkoutDetailsState = workoutUseCases.getAllWorkoutDetailsUseCase().toList()

        logger.i(TAG, "Get all workout details -> isLoading state raised.")
        assertTrue { getAllWorkoutDetailsState[0].isLoading }

        logger.i(TAG, "Get all workout details -> isSuccessful state raised.")
        assertTrue { getAllWorkoutDetailsState[1].isSuccessful }

        val workoutDetailsDB = getAllWorkoutDetailsState[1].workoutDetailsList
        logger.i(TAG, "WorkoutDetails DB: $workoutDetailsDB")

        logger.i(TAG, "Assert workout details DB is empty.")
        assertTrue { workoutDetailsDB.isEmpty() }

        //TODO: [Test] when 2 entries are added and 1 deleted if 1 stays in DB...
    }

    /**
     * Tested functions inside:
     *
     * CreateCustomWorkoutDetailsUseCase()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * WorkoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef()
     * ExerciseSetDao.saveSet()
     * ExerciseSetDao.updateSet()
     * ExerciseDao.insertAllExerciseSetCrossRef()
     * ---------------------------
     * AddExerciseUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * ExerciseDao.getExerciseByExerciseAndWorkoutId()
     * WorkoutDetailsDao.insertWorkoutDetailsExerciseCrossRef()
     * ExerciseSetDao.saveSet()
     * ExerciseDao.insertExerciseSetCrossRef()
     * WorkoutDao.updateWorkout()
     * ---------------------------
     * DeleteExerciseUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * ExerciseDao.getExerciseByExerciseAndWorkoutId()
     * ExerciseDao.deleteExerciseSetCrossRef()
     * ExerciseSetDao.deleteSet()
     * WorkoutDetailsDao.deleteWorkoutDetailsExerciseCrossRef()
     * ExerciseDao.getExerciseByExerciseAndWorkoutId()
     * WorkoutDao.getWorkoutById()
     * WorkoutDao.updateWorkout()
     * ---------------------------
     * ExerciseSetDao.getSetById()
     */
    @RepeatedTest(50)
    @DisplayName("Add exercise using AddExerciseUseCase test and delete exercise using DeleteExerciseUseCase test")
    fun `add exercise using AddExerciseUseCase test and delete exercise using DeleteExerciseUseCase test`() =
        runTest {

            //Generate workout details
            val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
            logger.i(
                TAG,
                "Generated workout: $workoutDetails.\nExercises: ${workoutDetails.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetails.exercises.size}")

            //Save workout details to DB
            val createCustomWorkoutDetailsState =
                workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

            logger.i(TAG, "Create custom workout details -> isLoading state raised.")
            assertTrue { createCustomWorkoutDetailsState[0].isLoading }

            logger.i(TAG, "Create custom workout details -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutDetailsState[1].isSuccessful }

            val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

            //Generate exercise that is not already in the workoutDetails.exercise list
            val excludedIds = workoutDetails.exercises.map { it.exerciseId }

            val exercise =
                MockupDataGenerator.generateExercise(excludedIds = excludedIds, workoutId = workoutDetails.workoutId, isWorkout = true)
            logger.i(TAG, "Generated exercise: $exercise.")

            //Insert in DB
            val addExerciseState = workoutUseCases.addExerciseUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exercise = exercise
            ).toList()

            logger.i(
                TAG,
                "Add exercise to workout details ${workoutDetails.name} -> isLoading state raised."
            )
            assertTrue { addExerciseState[0].isLoading }

            logger.i(
                TAG,
                "Add exercise to workout details ${workoutDetails.name} -> isSuccessful state raised."
            )
            assertTrue { addExerciseState[1].isSuccessful }

            logger.i(
                TAG,
                "Assert exercise sets data is not lost."
            )
            assertTrue { addExerciseState[1].workoutDetails.exercises.map { it.sets }.isNotEmpty() }

            val workoutDetailsAfterAdd = addExerciseState[1].workoutDetails
            logger.i(
                TAG,
                "Workout details after new exercise was added: $workoutDetailsAfterAdd\n. Exercises list: ${workoutDetailsAfterAdd.exercises}"
            )

            logger.i(TAG, "Assert the exercise is added in workout details")
            assertTrue { workoutDetailsAfterAdd.exercises.size == workoutDetails.exercises.size + 1 }

            //TODO: [Test] assert workout.totalExercises has incremented...

            //Delete from DB
            val deleteExerciseState = workoutUseCases.deleteExerciseUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exerciseId = exercise.exerciseId
            ).toList()

            logger.i(
                TAG,
                "Delete exercise from workout details ${workoutDetails.name} -> isLoading state raised."
            )
            assertTrue { deleteExerciseState[0].isLoading }

            logger.i(
                TAG,
                "Delete exercise from workout details ${workoutDetails.name} -> isSuccessful state raised."
            )
            assertTrue { deleteExerciseState[1].isSuccessful }

            val workoutDetailsAfterDelete = deleteExerciseState[1].workoutDetails
            logger.i(
                TAG,
                "Workout details after deleted exercise: $workoutDetailsAfterDelete.\nExercises: ${workoutDetails.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetails.exercises.size}")

            logger.i(
                TAG,
                "Assert initial workout details before added exercise is the same as the workout after deleted the added exercise."
            )
            assertTrue { workoutDetailsAfterDelete == workoutDetails }

            logger.i(
                TAG,
                "Assert sets from new generated exercise are deleted and not in DB."
            )

            val exception = try {
                exercise.sets.forEach { set ->
                    set.setId ?: throw NoSuchElementException()

                    exerciseSetDao.getSetById(set.setId!!)
                }

                null
            } catch (exception: NoSuchElementException) {
                exception
            }

            assertThrows(NoSuchElementException::class.java) {
                throw exception ?: return@assertThrows
            }
        }

    //TODO: [Test] create workout, submit already existing exercise in workout -> check if exercise is replaced with new exercise...
    //TODO: [Test] submit new exercise in workout -> check if exercise is added and exercise list is increased...

    /**
     * Tested functions inside:
     *
     * GetSelectedWorkoutUseCase()
     * WorkoutDao.getWorkoutByIsSelected()
     * ----------------------------
     * CreateCustomWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * ----------------------------
     * DeselectWorkoutUseCase()
     * WorkoutDao.getWorkoutById()
     *
     * updateWorkoutUseCase()
     * WorkoutDao.updateWorkout()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * WorkoutDetailsDao.updateWorkoutDetails()
     * ----------------------------
     * SelectWorkoutUseCase()
     * WorkoutDao.getWorkoutByIsSelected()
     * WorkoutDao.selectWorkoutById()
     */
    @RepeatedTest(50)
    @DisplayName("Select workout using SelectWorkoutUseCase test and get selected workout using GetSelectedWorkoutUseCase test")
    fun `select workout using SelectWorkoutUseCase test and get selected workout using GetSelectedWorkoutUseCase test`() =
        runTest {

            logger.i(TAG, "Test 1 -> get selected workout on empty DB.")

            //Fetch with empty DB -> no selected workout
            val getSelectedWorkoutState = workoutUseCases.getSelectedWorkoutUseCase().toList()

            logger.i(TAG, "Get selected workout -> isLoading state raised.")
            assertTrue { getSelectedWorkoutState[0].isLoading }

            logger.i(TAG, "Get selected workout -> isSuccessful state raised.")
            assertTrue { getSelectedWorkoutState[1].isSuccessful }

            val selectedWorkout = getSelectedWorkoutState[1].selectedWorkout
            logger.i(TAG, "Selected workout 1: $selectedWorkout")

            logger.i(TAG, "Assert no workout is currently selected. Workout DB is empty.")
            assertNull(selectedWorkout)

            //Generate workout
            val workout = MockupDataGenerator.generateWorkout(isSelected = true)
            logger.i(TAG, "Mocked workout: $workout")

            //Insert Workout in DB
            val createCustomWorkoutState =
                workoutUseCases.createCustomWorkoutUseCase(workout).toList()

            logger.i(TAG, "Create custom workout -> isLoading state raised.")
            assertTrue { createCustomWorkoutState[0].isLoading }

            logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState[1].isSuccessful }

            val savedWorkout = createCustomWorkoutState[1].workout
            logger.i(TAG, "Saved workout: $savedWorkout")

            logger.i(TAG, "Test 2 -> get selected workout after created a new one.")

            //Check which workout is currently selected
            val getSelectedWorkoutState2 = workoutUseCases.getSelectedWorkoutUseCase().toList()

            logger.i(TAG, "Get selected workout 2 -> isLoading state raised.")
            assertTrue { getSelectedWorkoutState2[0].isLoading }

            logger.i(TAG, "Get selected workout 2 -> isSuccessful state raised.")
            assertTrue { getSelectedWorkoutState2[1].isSuccessful }

            val selectedWorkout2 = getSelectedWorkoutState2[1].selectedWorkout
            logger.i(TAG, "Selected workout 2: $selectedWorkout2")

            logger.i(TAG, "Assert workout 1 is currently selected.")
            assertTrue { selectedWorkout2 == workout }

            logger.i(
                TAG,
                "Test 3 -> get selected workout after created a new one and DB contains selected workout."
            )

            val workout2 = MockupDataGenerator.generateWorkout(isSelected = true, excludedIds = listOf(workout.workoutId))
            logger.i(TAG, "Mocked workout 2: $workout2")

            //Insert another selected workout in DB
            val createCustomWorkoutState2 =
                workoutUseCases.createCustomWorkoutUseCase(workout2).toList()

            logger.i(TAG, "Create custom workout 2 -> isLoading state raised.")
            assertTrue { createCustomWorkoutState2[0].isLoading }

            logger.i(TAG, "Create custom workout 2 -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState2[1].isSuccessful }

            val savedWorkout2 = createCustomWorkoutState2[1].workout
            logger.i(TAG, "Saved workout 2: $savedWorkout2")

            //Check which workout is currently selected -> deselect workout and select workout 2
            val getSelectedWorkoutState3 = workoutUseCases.getSelectedWorkoutUseCase().toList()

            logger.i(TAG, "Get selected workout 3 -> isLoading state raised.")
            assertTrue { getSelectedWorkoutState3[0].isLoading }

            logger.i(TAG, "Get selected workout 3 -> isSuccessful state raised.")
            assertTrue { getSelectedWorkoutState3[1].isSuccessful }

            val selectedWorkout3 = getSelectedWorkoutState3[1].selectedWorkout
            logger.i(TAG, "Selected workout 3: $selectedWorkout3")

            logger.i(TAG, "Assert workout 2 is currently selected.")
            assertTrue { selectedWorkout3 == workout2 }

            logger.i(TAG, "Test 4 -> deselect workout 2.")

            //Deselect workout
            val deselectWorkoutState =
                workoutUseCases.deselectWorkoutUseCase(workout2.workoutId).toList()
            logger.i(TAG, "Deselected workout 2 -> isLoading state raised.")
            assertTrue { deselectWorkoutState[0].isLoading }

            logger.i(TAG, "Deselected workout 2 -> isSuccessful state raised.")
            assertTrue { deselectWorkoutState[1].isSuccessful }

            //Check if DB has no selected workout
            val getSelectedWorkoutState4 = workoutUseCases.getSelectedWorkoutUseCase().toList()

            logger.i(TAG, "Get selected workout 4 -> isLoading state raised.")
            assertTrue { getSelectedWorkoutState4[0].isLoading }

            logger.i(TAG, "Get selected workout 4 -> isSuccessful state raised.")
            assertTrue { getSelectedWorkoutState4[1].isSuccessful }

            val selectedWorkout4 = getSelectedWorkoutState4[1].selectedWorkout
            logger.i(TAG, "Selected workout 4: $selectedWorkout4")

            logger.i(TAG, "Assert no workout is currently selected.")
            assertNull(selectedWorkout4)

            logger.i(TAG, "Test 5 -> select workout 1.")

            //Fetch workout 1 from DB
            val getWorkoutState = workoutUseCases.getWorkoutUseCase(workout.workoutId)
                .toList()

            logger.i(TAG, "Get workout 1 -> isLoading state raised.")
            assertTrue { getWorkoutState[0].isLoading }

            logger.i(TAG, "Get workout 1 -> isSuccessful state raised.")
            assertTrue { getWorkoutState[1].isSuccessful }

            val fetchedWorkout = getWorkoutState[1].workout
            logger.i(TAG, "Assert workout 1 is not selected in the DB.")
            assertFalse { fetchedWorkout.isSelected }

            //Select workout 1
            logger.i(TAG, "Workout 1 is now selected.")
            val selectWorkoutState =
                workoutUseCases.selectWorkoutUseCase(workout.workoutId).toList()
            logger.i(TAG, "Select workout 1 -> isLoading state raised.")
            assertTrue { selectWorkoutState[0].isLoading }

            logger.i(TAG, "Select workout 1 -> isSuccessful state raised.")
            assertTrue { selectWorkoutState[1].isSuccessful }

            //Fetch current selected workout
            val getSelectedWorkoutState5 = workoutUseCases.getSelectedWorkoutUseCase().toList()

            logger.i(TAG, "Get selected workout 5 -> isLoading state raised.")
            assertTrue { getSelectedWorkoutState5[0].isLoading }

            logger.i(TAG, "Get selected workout 5 -> isSuccessful state raised.")
            assertTrue { getSelectedWorkoutState5[1].isSuccessful }

            val selectedWorkout5 = getSelectedWorkoutState5[1].selectedWorkout
            logger.i(TAG, "Selected workout 5: $selectedWorkout5")

            logger.i(TAG, "Assert workout 1 is currently selected.")
            assertTrue { selectedWorkout5 == workout }

            //TODO: [Test] select workout with workoutID not in DB and fetch...
            //TODO: [Test] select current selected workout and fetch...
        }

    @ParameterizedTest(name = "OnSearchWorkouts for search text {0}")
    @MethodSource("provideSearchTexts")
    @DisplayName("Search workout using OnSearchWorkoutUseCase test")
    fun `search workout using OnSearchWorkoutUseCase test`(searchText: String) = runTest {
        logger.i(TAG, "Search text for this test: {$searchText}.")

        //Generate workout list
        val workoutList = MockupDataGenerator.generateWorkoutList(5)

        val event = OnSearchWorkoutEvent.OnSearchTextChange(searchText, workoutList)
        val onSearchState = workoutUseCases.onSearchWorkoutUseCase(event).last()

//            logger.i(TAG, "On search filter workouts -> isLoading state raised.")
//            assertTrue { onSearchState[0].isLoading }
//
//            logger.i(TAG, "On search filter workouts -> isSuccessful state raised.")
//            assertTrue { onSearchState[1].isSuccessful }

        val filteredWorkoutList = onSearchState.workoutList
        logger.i(TAG, "Filtered workouts list by search text {$searchText}: $filteredWorkoutList")

        logger.i(
            TAG,
            "Assert all workouts contain the search text {$searchText} in their names or the workout list is empty: ${workoutList.isEmpty()}."
        )
        assertTrue(filteredWorkoutList.any {
            it.name.contains(
                searchText,
                ignoreCase = true
            )
        } || filteredWorkoutList.isEmpty())

        //TODO: [Test] OnToggleSearch...
    }
}