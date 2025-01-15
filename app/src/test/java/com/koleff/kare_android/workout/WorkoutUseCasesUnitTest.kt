package com.koleff.kare_android.workout

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.MockupDataGeneratorV2
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSourceV2
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.dto.WorkoutConfigurationDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.manager.ExerciseDBManagerV2
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.AddMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.UnfavoriteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.FindDuplicateExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetFavoriteWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.FavoriteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.SubmitMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.exercise.data.ExerciseDaoFakeV2
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.ui.event.OnSearchWorkoutEvent
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.data.CompositeExerciseSetChangeListener
import com.koleff.kare_android.workout.data.WorkoutConfigurationDaoFake
import com.koleff.kare_android.workout.data.WorkoutDaoFakeV2
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFakeV2
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

typealias WorkoutFakeDataSource = WorkoutLocalDataSourceV2

class WorkoutUseCasesUnitTest {
    companion object {
        private lateinit var exerciseDBManager: ExerciseDBManagerV2

        private lateinit var workoutDao: WorkoutDaoFakeV2
        private lateinit var workoutDetailsDao: WorkoutDetailsDaoFakeV2
        private lateinit var workoutConfigurationDao: WorkoutConfigurationDaoFake
        private lateinit var exerciseDao: ExerciseDaoFakeV2
        private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake
        private lateinit var exerciseSetDao: ExerciseSetDaoFake

        private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
        private lateinit var workoutRepository: WorkoutRepository

        private lateinit var workoutUseCases: WorkoutUseCases

        private val useMockupDataSource = false
        private val isErrorTesting = false

        private val isLogging = true
        private lateinit var logger: TestLogger

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

        @JvmStatic
        @BeforeAll
        fun setup() = runBlocking {
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
                deleteMultipleExercisesUseCase = DeleteMultipleExercisesUseCase(workoutRepository),
                addExerciseUseCase = AddExerciseUseCase(workoutRepository),
                addMultipleExercisesUseCase = AddMultipleExercisesUseCase(workoutRepository),
                submitExerciseUseCase = SubmitExerciseUseCase(workoutRepository),
                submitMultipleExercisesUseCase = SubmitMultipleExercisesUseCase(workoutRepository),
                findDuplicateExercisesUseCase = FindDuplicateExercisesUseCase(workoutRepository),
                deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
                favoriteWorkoutUseCase = FavoriteWorkoutUseCase(workoutRepository),
                unfavoriteWorkoutUseCase = UnfavoriteWorkoutUseCase(workoutRepository),
                getFavoriteWorkoutsUseCase = GetFavoriteWorkoutsUseCase(workoutRepository),
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

            //Create workout on empty DB
            val workouts = workoutUseCases.getAllWorkoutsUseCase().toList()[1].workoutList
            val workoutsInDB = workouts.size

            val createWorkoutState =
                workoutUseCases.createNewWorkoutUseCase().toList()

            logger.i(TAG, "Create workout -> isLoading state raised.")
            assertTrue { createWorkoutState[0].isLoading }

            logger.i(TAG, "Create workout -> isSuccessful state raised.")
            assertTrue { createWorkoutState[1].isSuccessful }

            val workoutsAfterCreate =
                workoutUseCases.getAllWorkoutsUseCase().toList()[1].workoutList
            val workoutsInDBAfterCreate = workoutsAfterCreate.size

            logger.i(TAG, "Assert new workout is created")
            assert(workoutsInDB + 1 == workoutsInDBAfterCreate)

            val createdWorkout = workoutsAfterCreate.stream()
                .filter {
                    !workouts.contains(it)
                }
                .findFirst()
                .orElseThrow()

            logger.i(TAG, "Assert Workout is not favorited")
            assert(!createdWorkout.isFavorite)

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

            //Create workout when there is already workout in DB...
            val createWorkoutState2 =
                workoutUseCases.createNewWorkoutUseCase().toList()

            logger.i(TAG, "Create workout -> isLoading state raised.")
            assertTrue { createWorkoutState2[0].isLoading }

            logger.i(TAG, "Create workout -> isSuccessful state raised.")
            assertTrue { createWorkoutState2[1].isSuccessful }

            val workoutsAfterCreate2 =
                workoutUseCases.getAllWorkoutsUseCase().toList()[1].workoutList
            val workoutsInDBAfterCreate2 = workoutsAfterCreate2.size

            logger.i(TAG, "Assert new workout is created on not empty DB")
            assert(workoutsInDB + 2 == workoutsInDBAfterCreate2)
        }

    /**
     * Tested functions inside:
     *
     * CreateCustomWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * ----------------
     * GetAllWorkoutsUseCase()
     * WorkoutDao.getWorkoutsOrderedById()
     * WorkoutDao.insertWorkout()
     */
    @RepeatedTest(50)
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

        //Insert 2 workouts and fetch again...
        val workout1 = MockupDataGeneratorV2.generateWorkout(isFavorite = false)
        logger.i(TAG, "Mocked workout 1: $workout1")

        val workout2 = MockupDataGeneratorV2.generateWorkout(isFavorite = false)
        logger.i(TAG, "Mocked workout 2: $workout2")

        val workoutList = listOf(workout1, workout2)

        val createCustomWorkoutState = workoutUseCases.createCustomWorkoutUseCase(workout1).toList()

        logger.i(TAG, "Create custom workout 1 -> isLoading state raised.")
        assertTrue { createCustomWorkoutState[0].isLoading }

        logger.i(TAG, "Create custom workout 1 -> isSuccessful state raised.")
        assertTrue { createCustomWorkoutState[1].isSuccessful }

        val createCustomWorkoutState2 =
            workoutUseCases.createCustomWorkoutUseCase(workout2).toList()

        logger.i(TAG, "Create custom workout 2 -> isLoading state raised.")
        assertTrue { createCustomWorkoutState2[0].isLoading }

        logger.i(TAG, "Create custom workout 2 -> isSuccessful state raised.")
        assertTrue { createCustomWorkoutState2[1].isSuccessful }

        val getWorkoutsState2 = workoutUseCases.getAllWorkoutsUseCase().toList()

        logger.i(TAG, "Get workouts after insert -> isLoading state raised.")
        assertTrue { getWorkoutsState2[0].isLoading }

        logger.i(TAG, "Get workouts after insert -> isSuccessful state raised.")
        assertTrue { getWorkoutsState2[1].isSuccessful }

        assertTrue { getWorkoutsState2[1].workoutList.size == 2 }
        assertTrue { getWorkoutsState2[1].workoutList.containsAll(workoutList) }
    }

    /**
     * Tested functions inside:
     *
     * CreateCustomWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * --------------------------
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
    @DisplayName("Get workout using GetWorkoutUseCase test")
    fun `get workout using GetWorkoutUseCase test`() = runTest {

        //Generate workout
        val workout = MockupDataGeneratorV2.generateWorkout()
        logger.i(TAG, "Mocked workout: $workout")

        //Insert
        val id = workoutUseCases.createCustomWorkoutUseCase(workout).toList()[1].workout.workoutId
        logger.i(TAG, "Mocked workout inserted successfully. Workout id: $id")

        //Fetch
        val getWorkoutState = workoutUseCases.getWorkoutUseCase(id)
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

    /**
     * Tested functions inside:
     *
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
    @DisplayName("Get invalid workout using GetWorkoutUseCase test")
    fun `get invalid workout using GetWorkoutUseCase test`() = runTest {

        //Fetch
        val getWorkoutState = workoutUseCases.getWorkoutUseCase(-1)
            .toList()

        logger.i(TAG, "Get workout -> isLoading state raised.")
        assertTrue { getWorkoutState[0].isLoading }

        logger.i(TAG, "Get workout -> isError state raised.")
        assertTrue { getWorkoutState[1].isError }

        logger.i(TAG, "Assert error is KareError.WORKOUT_NOT_FOUND.")
        assertTrue { getWorkoutState[1].error == KareError.WORKOUT_NOT_FOUND }
    }

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
    //TODO: document tests...
    @RepeatedTest(50)
    @DisplayName("Get workout details using GetWorkoutsDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test")
    fun `get workout details using GetWorkoutsDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test`() =
        runTest {

            //Generate workout details and workout
            val data =
                MockupDataGeneratorV2.generateWorkoutAndWorkoutDetails(enableSetIdGeneration = true)

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

            logger.i(TAG, "Assert fetched workout details is the same as inserted one.")
            assertTrue(fetchedWorkoutDetails == workoutDetails)
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
    //TODO: document tests...
    @RepeatedTest(50)
    @DisplayName("Update workout details using UpdateWorkoutDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test")
    fun `update workout details using UpdateWorkoutDetailsUseCase test and CreateCustomWorkoutDetailsUseCase test`() =
        runTest {

            //Generate WorkoutDetails
            val workoutDetails = MockupDataGeneratorV2.generateWorkoutDetails()
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
                MockupDataGeneratorV2.generateExercise(
                    muscleGroup = muscleGroup,
                    workoutId = workoutDetails.workoutId
                )
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

            logger.i(TAG, "Update workout details -> isSuccessful state raised.")
            assertTrue { updateWorkoutDetailsState[0].isSuccessful }

            val updateWorkoutDetails = updateWorkoutDetailsState[0].workoutDetails
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
    //TODO: document tests...
    @RepeatedTest(50)
    @DisplayName("Update workout using UpdateWorkoutUseCase test and CreateCustomWorkoutUseCase test")
    fun `update workout using UpdateWorkoutUseCase test and CreateCustomWorkoutUseCase test`() =
        runTest {

            //Generate Workout
            val workout = MockupDataGeneratorV2.generateWorkout()
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
        val workout = MockupDataGeneratorV2.generateWorkout()
        logger.i(TAG, "Mocked workout: $workout")

        //Insert Workout in DB
        val createCustomWorkoutState = workoutUseCases.createCustomWorkoutUseCase(workout).toList()

        logger.i(TAG, "Create custom workout -> isLoading state raised.")
        assertTrue { createCustomWorkoutState[0].isLoading }

        logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
        assertTrue { createCustomWorkoutState[1].isSuccessful }

        val savedWorkout = createCustomWorkoutState[1].workout
        logger.i(TAG, "Saved workout: $savedWorkout")

        //TODO: [Test] if ExerciseSet-WorkoutDetails cross refs are also deleted...
        //TODO: [Test] if WorkoutConfiguration is also deleted

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
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
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
            val workoutDetails = MockupDataGeneratorV2.generateWorkoutDetails()
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
                MockupDataGeneratorV2.generateExercise(
                    excludedIds = excludedIds,
                    workoutId = workoutDetails.workoutId
                )
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
            logger.i(TAG, "Total exercises: ${workoutDetailsAfterAdd.exercises.size}")

            logger.i(TAG, "Assert the exercise is added in workout details")
            assertTrue { workoutDetailsAfterAdd.exercises.size == workoutDetails.exercises.size + 1 }

            //Check for totalExercises increment
            val getWorkoutState = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
                .toList()

            logger.i(TAG, "Get workout -> isLoading state raised.")
            assertTrue { getWorkoutState[0].isLoading }

            logger.i(TAG, "Get workout -> isSuccessful state raised.")
            assertTrue { getWorkoutState[1].isSuccessful }

            val fetchedWorkout = getWorkoutState[1].workout
            logger.i(TAG, "Fetched workout: $fetchedWorkout")
            logger.i(TAG, "Assert totalExercises in workout has incremented")
            assertTrue { fetchedWorkout.totalExercises == workoutDetailsAfterAdd.exercises.size }

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
                "Workout details after deleted exercise: $workoutDetailsAfterDelete.\nExercises: ${workoutDetailsAfterDelete.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetailsAfterDelete.exercises.size}")

            logger.i(
                TAG,
                "Assert initial workout details before added exercise is the same as the workout after deleted the added exercise."
            )
            assertTrue { workoutDetailsAfterDelete == workoutDetails }

            val getWorkoutState2 = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
                .toList()

            logger.i(TAG, "Get workout after deleted exercise -> isLoading state raised.")
            assertTrue { getWorkoutState2[0].isLoading }

            logger.i(TAG, "Get workout after deleted exercise -> isSuccessful state raised.")
            assertTrue { getWorkoutState2[1].isSuccessful }

            val fetchedWorkout2 = getWorkoutState2[1].workout
            logger.i(TAG, "Fetched workout after deleted exercise: $fetchedWorkout2")

            logger.i(
                TAG,
                "Assert totalExercises in workout have decreased and are equal to initial workout details."
            )
            assertTrue { fetchedWorkout2.totalExercises == workoutDetails.exercises.size }

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
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     * --------------------------
     * SubmitExerciseUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * ExerciseDao.getSetsForExercise()
     * ExerciseDao.insertExercise()
     * WorkoutDetailsDao.insertWorkoutDetailsExerciseCrossRef()
     * ExerciseSetDao.saveSet()
     * ExerciseDao.insertExerciseSetCrossRef()
     * WorkoutDao.updateWorkout()
     */

    /**
     * Test 1 - Generate exercise that is already in the workoutDetails.exercise list with no sets
     * Test 2 - Generate exercise that is not already in the workoutDetails.exercise list with some sets
     * Test 3 - Generate exercise that is already in the workoutDetails.exercise list with some sets
     */
    @RepeatedTest(50)
    @DisplayName("Submit exercise using SubmitExerciseUseCase test")
    fun `submit exercise using SubmitExerciseUseCase`() = runTest {

        /**
         * Test 1 - Generate exercise that is already in the workoutDetails.exercise list with no sets
         */

        //Generate workout details
        val workoutDetails = MockupDataGeneratorV2.generateWorkoutDetails()
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

        //Generate exercise that is in the DB with different ExerciseSets and submit
        val selectedExercise = savedWorkoutDetails.exercises.random()
            .copy(sets = emptyList())
        logger.i(
            TAG,
            "Selected exercise that is already in workout to be submitted: $selectedExercise"
        )

        val submitExerciseState = workoutUseCases.submitExerciseUseCase(
            workoutId = savedWorkoutDetails.workoutId,
            exercise = selectedExercise
        ).toList()

        logger.i(
            TAG,
            "Submit exercise that is already in workout to workout details ${workoutDetails.name} -> isLoading state raised."
        )
        assertTrue { submitExerciseState[0].isLoading }

        logger.i(
            TAG,
            "Submit exercise that is already in workout to workout details ${workoutDetails.name} -> isSuccessful state raised."
        )
        assertTrue { submitExerciseState[1].isSuccessful }

        logger.i(
            TAG,
            "Assert exercise size is not changed. Exercise list after submit: ${submitExerciseState[1].workoutDetails.exercises.size}."
        )
        assertTrue { submitExerciseState[1].workoutDetails.exercises.size == savedWorkoutDetails.exercises.size }

        val submittedExercise = submitExerciseState[1].workoutDetails.exercises.first {
            it.exerciseId == selectedExercise.exerciseId
        }

        logger.i(
            TAG,
            "Assert exercise sets are chosen from newly submitted entry: ${submittedExercise.sets}."
        )
        assertTrue { selectedExercise.sets == submittedExercise.sets }

        logger.i(
            TAG,
            "Assert previous exercise sets are deleted from DB."
        )
        val initialSelectedExercise = workoutDetails.exercises.first {
            it.exerciseId == selectedExercise.exerciseId
        }
        val exception = try {
            initialSelectedExercise.sets.forEach { set ->
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

        /**
         * Test 2 - Generate exercise that is not already in the workoutDetails.exercise list with some sets
         */

        val excludedIds = workoutDetails.exercises.map { it.exerciseId }

        val exercise =
            MockupDataGeneratorV2.generateExercise(
                excludedIds = excludedIds,
                workoutId = workoutDetails.workoutId
            )
        logger.i(TAG, "Generated exercise: $exercise.")

        //Insert in DB
        val submitExerciseState2 = workoutUseCases.submitExerciseUseCase(
            workoutId = savedWorkoutDetails.workoutId,
            exercise = exercise
        ).toList()

        logger.i(
            TAG,
            "Submit exercise to workout details ${workoutDetails.name} -> isLoading state raised."
        )
        assertTrue { submitExerciseState2[0].isLoading }

        logger.i(
            TAG,
            "Submit exercise to workout details ${workoutDetails.name} -> isSuccessful state raised."
        )
        assertTrue { submitExerciseState2[1].isSuccessful }

        logger.i(
            TAG,
            "Assert exercise sets data is not lost."
        )
        assertTrue { submitExerciseState2[1].workoutDetails.exercises.map { it.sets }.isNotEmpty() }

        val workoutDetailsAfterSubmit = submitExerciseState2[1].workoutDetails
        logger.i(
            TAG,
            "Workout details after new exercise was added: $workoutDetailsAfterSubmit\n. Exercises list: ${workoutDetailsAfterSubmit.exercises}"
        )

        logger.i(TAG, "Assert the exercise is added in workout details")
        assertTrue { workoutDetailsAfterSubmit.exercises.size == workoutDetails.exercises.size + 1 }

        //Check for totalExercises increment
        val getWorkoutState = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
            .toList()

        logger.i(TAG, "Get workout -> isLoading state raised.")
        assertTrue { getWorkoutState[0].isLoading }

        logger.i(TAG, "Get workout -> isSuccessful state raised.")
        assertTrue { getWorkoutState[1].isSuccessful }

        val fetchedWorkout = getWorkoutState[1].workout
        logger.i(TAG, "Fetched workout: $fetchedWorkout")
        logger.i(TAG, "Assert totalExercises in workout has incremented")
        assertTrue { fetchedWorkout.totalExercises == workoutDetailsAfterSubmit.exercises.size }


        /**
         * Test 3 - Generate exercise that is already in the workoutDetails.exercise list with some sets
         */

        //Generate exercise that is in the DB with different ExerciseSets and submit
        val selectedExercise2 = savedWorkoutDetails.exercises
            .filter { it.exerciseId != selectedExercise.exerciseId }
            .random()
        val updatedSelectedExercise2 = selectedExercise2.copy(
            sets = MockupDataGeneratorV2.generateExerciseSetsList(
                n = 2,
                workoutId = workoutDetails.workoutId,
                exerciseId = selectedExercise2.exerciseId
            )
        )

        logger.i(
            TAG,
            "Selected exercise that is already in workout to be submitted 2: $updatedSelectedExercise2"
        )

        val submitExerciseState3 = workoutUseCases.submitExerciseUseCase(
            workoutId = savedWorkoutDetails.workoutId,
            exercise = updatedSelectedExercise2
        ).toList()

        logger.i(
            TAG,
            "Submit exercise that is already in workout to workout details ${workoutDetails.name} 2 -> isLoading state raised."
        )
        assertTrue { submitExerciseState3[0].isLoading }

        logger.i(
            TAG,
            "Submit exercise that is already in workout to workout details ${workoutDetails.name} 2 -> isSuccessful state raised."
        )
        assertTrue { submitExerciseState3[1].isSuccessful }

        logger.i(
            TAG,
            "Assert exercise size is not changed. Exercise list after submit: ${submitExerciseState3[1].workoutDetails.exercises.size}."
        )
        assertTrue { submitExerciseState3[1].workoutDetails.exercises.size == workoutDetailsAfterSubmit.exercises.size }

        val submittedExercise2 = submitExerciseState3[1].workoutDetails.exercises.first {
            it.exerciseId == updatedSelectedExercise2.exerciseId
        }

        logger.i(
            TAG,
            "Assert exercise sets are chosen from newly submitted entry 2: ${submittedExercise2.sets}."
        )
        assertTrue { updatedSelectedExercise2.sets == submittedExercise2.sets }

        logger.i(
            TAG,
            "Assert previous exercise sets are deleted from DB 2."
        )
        val exception2 = try {
            updatedSelectedExercise2.sets.forEach { set ->
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

    //TODO: [Test] update invalid workout configuration
    //TODO: [Test] delete invalid workout configuration
    //TODO: [Test] get invalid workout configuration

    /**
     * Test 1 - create workout configuration
     * Test 2 - update existing workout configuration
     * Test 3 - delete workout configuration
     */
    @RepeatedTest(50)
    @DisplayName("Create, update and delete workout configuration using CreateWorkoutConfigurationUseCase, UpdateWorkoutConfigurationUseCase and DeleteWorkoutConfigurationUseCase tests")
    fun `create, update and delete workout configuration using CreateWorkoutConfigurationUseCase, UpdateWorkoutConfigurationUseCase and DeleteWorkoutConfigurationUseCase tests`() = runTest {

        //Generate workout details and workout
        val workoutDetails =
            MockupDataGeneratorV2.generateWorkoutDetails(enableSetIdGeneration = true)
        logger.i(TAG, "Mocked workout details: $workoutDetails")

        //Insert workout to generate WorkoutDetails in DB
        val createCustomWorkoutDetailsState =
            workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

        val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
        logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

        //Fetch workout
        val getWorkoutDetailsState =
            workoutUseCases.getWorkoutDetailsUseCase(savedWorkoutDetails.workoutId).toList()

        val fetchedWorkoutDetails = getWorkoutDetailsState[1].workoutDetails
        logger.i(TAG, "Fetched workout details: $fetchedWorkoutDetails")

        //Generating custom workout configuration so this check is not needed
//        logger.i(
//            TAG,
//            "Assert workout configuration is default. Workout configuration: ${fetchedWorkoutDetails.configuration}"
//        )
//        assertTrue { fetchedWorkoutDetails.configuration == WorkoutConfigurationDto() }

        val updatedWorkoutConfiguration = WorkoutConfigurationDto(
            workoutId = workoutDetails.workoutId,
            cooldownTime = ExerciseTime(0, 0, 30)
        )

        //Update workout configuration
        val updateWorkoutConfigurationState =
            workoutUseCases.updateWorkoutConfigurationUseCase(updatedWorkoutConfiguration).toList()

        logger.i(TAG, "Update workout configuration -> isLoading state raised.")
        assertTrue { updateWorkoutConfigurationState[0].isLoading }

        logger.i(TAG, "Update workout configuration -> isSuccessful state raised.")
        assertTrue { updateWorkoutConfigurationState[1].isSuccessful }

        //Fetch workout configuration
        val getWorkoutConfigurationState =
            workoutUseCases.getWorkoutConfigurationUseCase(workoutId = workoutDetails.workoutId)
                .toList()

        logger.i(TAG, "Get workout configuration -> isLoading state raised.")
        assertTrue { getWorkoutConfigurationState[0].isLoading }

        logger.i(TAG, "Get workout configuration -> isSuccessful state raised.")
        assertTrue { getWorkoutConfigurationState[1].isSuccessful }

        logger.i(
            TAG,
            "Assert workout configuration has updated. Workout configuration from DB: ${getWorkoutConfigurationState[1].workoutConfiguration}"
        )
        assertTrue { getWorkoutConfigurationState[1].workoutConfiguration == updatedWorkoutConfiguration }

        //Fetch workout
        val getWorkoutDetailsState2 =
            workoutUseCases.getWorkoutDetailsUseCase(savedWorkoutDetails.workoutId).toList()

        val fetchedWorkoutDetails2 = getWorkoutDetailsState2[1].workoutDetails
        logger.i(TAG, "Fetched workout details 2: $fetchedWorkoutDetails2")

        logger.i(
            TAG,
            "Assert workout configuration has changed. Workout configuration: ${fetchedWorkoutDetails2.configuration}"
        )
        assertTrue { fetchedWorkoutDetails2.configuration == getWorkoutConfigurationState[1].workoutConfiguration }

        //Delete workout configuration
        val deleteWorkoutConfigurationState =
            workoutUseCases.deleteWorkoutConfigurationUseCase(savedWorkoutDetails.workoutId)
                .toList()

        logger.i(TAG, "Delete workout configuration -> isLoading state raised.")
        assertTrue { deleteWorkoutConfigurationState[0].isLoading }

        logger.i(TAG, "Delete workout configuration -> isSuccessful state raised.")
        assertTrue { deleteWorkoutConfigurationState[1].isSuccessful }

        //Fetch workout
        val getWorkoutDetailsState3 =
            workoutUseCases.getWorkoutDetailsUseCase(savedWorkoutDetails.workoutId).toList()

        val fetchedWorkoutDetails3 = getWorkoutDetailsState3[1].workoutDetails
        logger.i(TAG, "Fetched workout details 3: $fetchedWorkoutDetails3")

        logger.i(
            TAG,
            "Assert workout configuration is default after delete. Workout configuration: ${fetchedWorkoutDetails3.configuration}"
        )
        assertTrue { fetchedWorkoutDetails3.configuration == WorkoutConfigurationDto().copy(workoutId = fetchedWorkoutDetails3.workoutId) }
    }

    @ParameterizedTest(name = "OnSearchWorkouts for search text {0}")
    @MethodSource("provideSearchTexts")
    @DisplayName("Search workout using OnSearchWorkoutUseCase test")
    fun `search workout using OnSearchWorkoutUseCase test`(searchText: String) = runTest {
        logger.i(TAG, "Search text for this test: {$searchText}.")

        //Generate workout list
        val workoutList = MockupDataGeneratorV2.generateWorkoutList(5)

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

    /**
     * Test 1 - Insert multiple exercises in workout details with no exercises
     * Test 2 - Insert multiple exercises in workout details with exercises (with and without duplicate ones)
     * Test 3 - Delete multiple exercises from workout details with exercises
     * */
    @RepeatedTest(50)
    @DisplayName("Add multiple exercises using AddMultipleExercisesUseCase test and delete multiple exercises using DeleteMultipleExercisesUseCase test")
    fun `add multiple exercises using AddMultipleExercisesUseCase test and delete multiple exercises using DeleteMultipleExercisesUseCase test`() =
        runTest {

            /**
             * Test 1 - Insert multiple exercises in workout details with no exercises
             */

            //Generate workout details
            val workoutDetails =
                MockupDataGeneratorV2.generateWorkoutDetails(generateExercises = false)
            logger.i(
                TAG,
                "Generated workout: $workoutDetails.\nExercises: ${workoutDetails.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetails.exercises.size}")

            //Save workout details to DB
            val createCustomWorkoutDetailsState =
                workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

            val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

            val n = 4
            val initialExerciseList =
                MockupDataGeneratorV2.generateExerciseList(
                    n = n,
                    muscleGroup = workoutDetails.muscleGroup,
                    isDistinct = true,
                    workoutId = workoutDetails.workoutId
                )
            logger.i(TAG, "Generated $n exercises: $initialExerciseList.")

            //Insert in DB
            val addMultipleExercisesState = workoutUseCases.addMultipleExercisesUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exerciseList = initialExerciseList
            ).toList()

            logger.i(
                TAG,
                "Add multiple exercises to workout details ${workoutDetails.name} -> isLoading state raised."
            )
            assertTrue { addMultipleExercisesState[0].isLoading }

            logger.i(
                TAG,
                "Add multiple exercises to workout details ${workoutDetails.name} -> isSuccessful state raised."
            )
            assertTrue { addMultipleExercisesState[1].isSuccessful }

            logger.i(
                TAG,
                "Assert exercise sets data is not lost."
            )
            assertTrue {
                addMultipleExercisesState[1].workoutDetails.exercises.map { it.sets }.isNotEmpty()
            }

            val workoutDetailsAfterAdd = addMultipleExercisesState[1].workoutDetails
            logger.i(
                TAG,
                "Workout details after new exercises were added: $workoutDetailsAfterAdd\n. Exercises list: ${workoutDetailsAfterAdd.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetailsAfterAdd.exercises.size}")

            logger.i(TAG, "Assert n exercises are added in workout details")
            assertTrue { workoutDetailsAfterAdd.exercises.size == workoutDetails.exercises.size + n }

            //Check for totalExercises increment
            val getWorkoutState = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
                .toList()

            val fetchedWorkout = getWorkoutState[1].workout
            logger.i(TAG, "Fetched workout: $fetchedWorkout")
            logger.i(TAG, "Assert totalExercises in workout has increased")
            assertTrue { fetchedWorkout.totalExercises == workoutDetailsAfterAdd.exercises.size }

            /**
             * Test 2 - Insert multiple exercises in workout details with exercises (with and without duplicate ones)
             */

            val n2 = 3

            //Generate exercises that are not already in the workoutDetails.exercise list
            val duplicateExercises = initialExerciseList.subList(0, 2)
            val duplicateExerciseIds = duplicateExercises.map { it.exerciseId }
            val excludedIds = initialExerciseList
                .map { it.exerciseId }
                .toMutableList()

            //Have some duplicate exercises
            excludedIds.removeAll(duplicateExerciseIds)

            //2 duplicate exercises and 1 new
            val exerciseList2 =
                MockupDataGeneratorV2.generateExerciseList(
                    n = n2,
                    muscleGroup = workoutDetails.muscleGroup,
                    preSelectedExerciseIds = duplicateExerciseIds,
                    excludedIds = excludedIds,
                    isDistinct = true,
                    workoutId = workoutDetails.workoutId
                )
            logger.i(TAG, "Generated $n2 exercises: $exerciseList2.")

            //Insert in DB
            val addMultipleExercisesState2 = workoutUseCases.addMultipleExercisesUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exerciseList = exerciseList2
            ).toList()

            logger.i(
                TAG,
                "Add multiple exercises to workout details ${workoutDetails.name} 2 -> isLoading state raised."
            )
            assertTrue { addMultipleExercisesState2[0].isLoading }

            logger.i(
                TAG,
                "Add multiple exercises to workout details ${workoutDetails.name} 2 -> isSuccessful state raised."
            )
            assertTrue { addMultipleExercisesState2[1].isSuccessful }

            logger.i(
                TAG,
                "Assert exercise sets data is not lost."
            )
            assertTrue {
                addMultipleExercisesState2[1].workoutDetails.exercises.map { it.sets }.isNotEmpty()
            }

            val workoutDetailsAfterAdd2 =
                addMultipleExercisesState2[1].workoutDetails //2 duplicate exercises in workout details
            logger.i(
                TAG,
                "Workout details after new exercises were added: $workoutDetailsAfterAdd2\n." +
                        " Exercises list: ${workoutDetailsAfterAdd2.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetailsAfterAdd2.exercises.size}")

            logger.i(TAG, "Assert n2 exercises are added in workout details")
            assertTrue { workoutDetailsAfterAdd2.exercises.size == workoutDetailsAfterAdd.exercises.size + n2 }

            //Check for totalExercises increment
            val getWorkoutState2 = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
                .toList()

            val fetchedWorkout2 = getWorkoutState2[1].workout
            logger.i(TAG, "Fetched workout 2: $fetchedWorkout2")
            logger.i(TAG, "Assert totalExercises in workout has increased")
            assertTrue { fetchedWorkout2.totalExercises == workoutDetailsAfterAdd2.exercises.size }

            /**
             * Test 3 - Delete multiple exercises from workout details with exercises
             */

            //Delete from DB
            val deleteMultipleExercisesState = workoutUseCases.deleteMultipleExercisesUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exerciseIds = initialExerciseList.map { it.exerciseId }
            ).toList()

            logger.i(
                TAG,
                "Delete multiple exercises from workout details ${workoutDetails.name} -> isLoading state raised."
            )
            assertTrue { deleteMultipleExercisesState[0].isLoading }

            logger.i(
                TAG,
                "Delete multiple exercises from workout details ${workoutDetails.name} -> isSuccessful state raised."
            )
            assertTrue { deleteMultipleExercisesState[1].isSuccessful }

            val workoutDetailsAfterDelete = deleteMultipleExercisesState[1].workoutDetails
            logger.i(
                TAG,
                "Workout details after deleted exercise: $workoutDetailsAfterDelete.\n" +
                        "Exercises: ${workoutDetailsAfterDelete.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetailsAfterDelete.exercises.size}")

            logger.i(
                TAG,
                "Assert workout details after delete has 1 exercise -> 2 * 2 duplicate exercises + 2 unique deleted."
            )
            assertTrue { workoutDetailsAfterDelete.exercises.size == 1 }

            val getWorkoutState3 = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
                .toList()

            logger.i(TAG, "Get workout after deleted exercise -> isLoading state raised.")
            assertTrue { getWorkoutState3[0].isLoading }

            logger.i(TAG, "Get workout after deleted exercise -> isSuccessful state raised.")
            assertTrue { getWorkoutState3[1].isSuccessful }

            val fetchedWorkout3 = getWorkoutState3[1].workout
            logger.i(TAG, "Fetched workout after deleted exercise: $fetchedWorkout3")

            logger.i(
                TAG,
                "Assert totalExercises in workout have decreased by 6"
            )
            assertTrue { fetchedWorkout3.totalExercises == workoutDetailsAfterAdd2.exercises.size - 6 }

            logger.i(
                TAG,
                "Assert sets from initial generated exercise are deleted and not in DB."
            )

            val exception = try {
                initialExerciseList.forEach { exercise ->
                    exercise.sets.forEach { set ->
                        set.setId ?: throw NoSuchElementException()

                        exerciseSetDao.getSetById(set.setId!!)
                    }
                }
                null
            } catch (exception: NoSuchElementException) {
                exception
            }

            assertThrows(NoSuchElementException::class.java) {
                throw exception ?: return@assertThrows
            }
        }

    @RepeatedTest(50)
    @DisplayName("Delete exercise using DeleteExerciseUseCase in workout with no exercises test")
    fun `delete exercise using DeleteExerciseUseCase in workout with no exercises test`() =
        runTest {

            //Generate workout details
            val workoutDetails =
                MockupDataGeneratorV2.generateWorkoutDetails(generateExercises = false)
            logger.i(
                TAG,
                "Generated workout: $workoutDetails.\nExercises: ${workoutDetails.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetails.exercises.size}")

            //Save workout details to DB
            val createCustomWorkoutDetailsState =
                workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

            val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

            //Delete exercise
            val deleteExerciseState = workoutUseCases.deleteExerciseUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exerciseId = Random.nextInt(0, 10)
            ).toList()

            logger.i(
                TAG,
                "Delete invalid exercise from workout details ${workoutDetails.name} -> isLoading state raised."
            )
            assertTrue { deleteExerciseState[0].isLoading }

            logger.i(
                TAG,
                "Delete invalid exercise from workout details ${workoutDetails.name} -> isError state raised."
            )
            assertTrue { deleteExerciseState[1].isError }

            logger.i(TAG, "Assert error is KareError.EXERCISE_NOT_FOUND.")
            assertTrue { deleteExerciseState[1].error == KareError.EXERCISE_NOT_FOUND }
        }

    @RepeatedTest(50)
    @DisplayName("Delete multiple exercises using DeleteMultipleExercisesUseCase in workout with no exercises test")
    fun `delete multiple exercises using DeleteMultipleExercisesUseCase in workout with no exercises test`() =
        runTest {

            //Generate workout details
            val workoutDetails =
                MockupDataGeneratorV2.generateWorkoutDetails(generateExercises = false)
            logger.i(
                TAG,
                "Generated workout: $workoutDetails.\nExercises: ${workoutDetails.exercises}"
            )
            logger.i(TAG, "Total exercises: ${workoutDetails.exercises.size}")

            //Save workout details to DB
            val createCustomWorkoutDetailsState =
                workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

            val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
            logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

            val exerciseIds =
                listOf(
                    Random.nextInt(0, 10),
                    Random.nextInt(0, 10),
                    Random.nextInt(0, 10)
                )

            //Delete multiple exercises
            val deleteMultipleExercisesState = workoutUseCases.deleteMultipleExercisesUseCase(
                workoutId = savedWorkoutDetails.workoutId,
                exerciseIds = exerciseIds
            ).toList()

            logger.i(
                TAG,
                "Delete multiple invalid exercises from workout details ${workoutDetails.name} -> isLoading state raised."
            )
            assertTrue { deleteMultipleExercisesState[0].isLoading }

            logger.i(
                TAG,
                "Delete multiple invalid exercises from workout details ${workoutDetails.name} -> isError state raised."
            )
            assertTrue { deleteMultipleExercisesState[1].isError }

            logger.i(TAG, "Assert error is KareError.EXERCISE_NOT_FOUND.")
            assertTrue { deleteMultipleExercisesState[1].error == KareError.EXERCISE_NOT_FOUND }
        }

    /**
     * Test 1 - Generate multiple exercises that are already in the workoutDetails.exercise list with no sets
     * Test 2 - Generate multiple exercises that are not already in the workoutDetails.exercise list with some sets
     * Test 3 - Generate multiple exercises that are already in the workoutDetails.exercise list with some sets
     */
    @RepeatedTest(50)
    @DisplayName("Submit multiple exercises using SubmitMultipleExercisesUseCase test")
    fun `submit multiple exercises using SubmitMultipleExercisesUseCase test`() = runTest {

        /**
         * Test 1 - Generate multiple exercises that are already in the workoutDetails.exercise list with no sets
         */

        //Generate workout details
        val workoutDetails = MockupDataGeneratorV2.generateWorkoutDetails()
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

        //Generate exercises that are in the DB with different ExerciseSets and submit
        val n = 2
        val selectedExerciseList = savedWorkoutDetails.exercises
            .take(n)
            .map { exercise -> exercise.copy(sets = emptyList()) }
        logger.i(
            TAG,
            "Selected exercises that are already in workout to be submitted: $selectedExerciseList"
        )

        val submitMultipleExercisesState = workoutUseCases.submitMultipleExercisesUseCase(
            workoutId = savedWorkoutDetails.workoutId,
            exerciseList = selectedExerciseList
        ).toList()

        logger.i(
            TAG,
            "Submit multiple exercises that are already in workout to workout details ${workoutDetails.name} -> isLoading state raised."
        )
        assertTrue { submitMultipleExercisesState[0].isLoading }

        logger.i(
            TAG,
            "Submit multiple exercises that are already in workout to workout details ${workoutDetails.name} -> isSuccessful state raised."
        )
        assertTrue { submitMultipleExercisesState[1].isSuccessful }

        logger.i(
            TAG,
            "Assert exercise size has not changed. Exercise list after submit: " +
                    "${submitMultipleExercisesState[1].workoutDetails.exercises.size}."
        )
        assertTrue { submitMultipleExercisesState[1].workoutDetails.exercises.size == savedWorkoutDetails.exercises.size }

        val selectedExerciseIds = selectedExerciseList.map { it.exerciseId }
        val submittedExercises = submitMultipleExercisesState[1].workoutDetails.exercises
            .filter { exercise -> selectedExerciseIds.contains(exercise.exerciseId) }

        logger.i(
            TAG,
            "Assert exercise sets are chosen from newly submitted entry: ${submittedExercises}."
        )
        submittedExercises.forEach { submittedExercise ->
            val initialSelectedExercise = selectedExerciseList.first { selectedExercise ->
                selectedExercise.exerciseId == submittedExercise.exerciseId
            }
            assertTrue { initialSelectedExercise.sets == submittedExercise.sets }
        }

        logger.i(
            TAG,
            "Assert previous exercise sets are deleted from DB."
        )
        val initialSelectedExercises = workoutDetails.exercises.filter {
            selectedExerciseIds.contains(it.exerciseId)
        }

        val exception = try {
            initialSelectedExercises.forEach { initialSelectedExercise ->
                initialSelectedExercise.sets.forEach { set ->
                    set.setId ?: throw NoSuchElementException()

                    exerciseSetDao.getSetById(set.setId!!)
                }
            }

            null
        } catch (exception: NoSuchElementException) {
            exception
        }

        assertThrows(NoSuchElementException::class.java) {
            throw exception ?: return@assertThrows
        }

        /**
         * Test 2 - Generate multiple exercises that are not already in the workoutDetails.exercise list with some sets
         */

        val excludedIds = workoutDetails.exercises.map { it.exerciseId }
        val n2 = 2
        val exerciseList =
            MockupDataGeneratorV2.generateExerciseList(
                n = n2,
                muscleGroup = workoutDetails.muscleGroup,
                excludedIds = excludedIds,
                isDistinct = true,
                workoutId = workoutDetails.workoutId
            )
        logger.i(TAG, "Generated $n2 exercises: $exerciseList.")

        //Insert in DB
        val submitMultipleExercisesState2 = workoutUseCases.submitMultipleExercisesUseCase(
            workoutId = savedWorkoutDetails.workoutId,
            exerciseList = exerciseList
        ).toList()

        logger.i(
            TAG,
            "Submit multiple new exercises to workout details ${workoutDetails.name} -> isLoading state raised."
        )
        assertTrue { submitMultipleExercisesState2[0].isLoading }

        logger.i(
            TAG,
            "Submit multiple new exercises to workout details ${workoutDetails.name} -> isSuccessful state raised."
        )
        assertTrue { submitMultipleExercisesState2[1].isSuccessful }

        logger.i(
            TAG,
            "Assert exercise sets data is not lost."
        )
        assertTrue {
            submitMultipleExercisesState2[1].workoutDetails.exercises.map { it.sets }.isNotEmpty()
        }

        val workoutDetailsAfterSubmit2 = submitMultipleExercisesState2[1].workoutDetails
        logger.i(
            TAG,
            "Workout details after new exercises were added: $workoutDetailsAfterSubmit2\n." +
                    " Exercises list: ${workoutDetailsAfterSubmit2.exercises}"
        )

        logger.i(TAG, "Assert the new exercises are added in workout details")
        assertTrue { workoutDetailsAfterSubmit2.exercises.size == workoutDetails.exercises.size + n2 }

        //Check for totalExercises increment
        val getWorkoutState = workoutUseCases.getWorkoutUseCase(workoutDetails.workoutId)
            .toList()

        val fetchedWorkout = getWorkoutState[1].workout
        logger.i(TAG, "Fetched workout: $fetchedWorkout")
        logger.i(TAG, "Assert totalExercises in workout has increased by $n2")
        assertTrue { fetchedWorkout.totalExercises == workoutDetails.exercises.size + n2 }

        /**
         * Test 3 - Generate multiple exercises that are already in the workoutDetails.exercise list with some sets
         */

        //Generate exercises that are not already in the workoutDetails.exercise list
        val n3 = 3

        val duplicateExercises = workoutDetails.exercises.subList(0, 2)
        val duplicateExerciseIds = duplicateExercises.map { it.exerciseId }
        var excludedIds2 = excludedIds.toMutableList()
        excludedIds2.addAll(
            exerciseList.map { it.exerciseId }
        )
        excludedIds2 = excludedIds2.distinct().toMutableList()

        //Have some duplicate exercises
        excludedIds2.removeAll(duplicateExerciseIds)

        //2 duplicate exercises and 1 new
        val exerciseList2 =
            MockupDataGeneratorV2.generateExerciseList(
                n = n3,
                muscleGroup = workoutDetails.muscleGroup,
                preSelectedExerciseIds = duplicateExerciseIds,
                excludedIds = excludedIds2,
                isDistinct = true,
                workoutId = workoutDetails.workoutId
            )
        logger.i(TAG, "Generated $n3 exercises: $exerciseList2.")

        val submitMultipleExercisesState3 = workoutUseCases.submitMultipleExercisesUseCase(
            workoutId = savedWorkoutDetails.workoutId,
            exerciseList = exerciseList2
        ).toList()

        logger.i(
            TAG,
            "Submit multiple exercise that are already in workout to workout details ${workoutDetails.name} 2 -> isLoading state raised."
        )
        assertTrue { submitMultipleExercisesState3[0].isLoading }

        logger.i(
            TAG,
            "Submit multiple exercise that are already in workout to workout details ${workoutDetails.name} 2 -> isSuccessful state raised."
        )
        assertTrue { submitMultipleExercisesState3[1].isSuccessful }

        logger.i(
            TAG,
            "Assert exercise size has changed by $n3. Exercise list after submit:" +
                    " ${submitMultipleExercisesState3[1].workoutDetails.exercises.size}."
        )
        assertTrue { submitMultipleExercisesState3[1].workoutDetails.exercises.size == savedWorkoutDetails.exercises.size + n3 }

        val exerciseIds2 = exerciseList2.map { it.exerciseId }
        val submittedExercises2 = submitMultipleExercisesState3[1].workoutDetails.exercises
            .filter { exercise -> exerciseIds2.contains(exercise.exerciseId) }

        logger.i(
            TAG,
            "Assert exercise sets are chosen from newly submitted entry: ${submittedExercises2}."
        )
        submittedExercises2.forEach { submittedExercise ->
            val initialSelectedExercise = exerciseList2.first { selectedExercise ->
                selectedExercise.exerciseId == submittedExercise.exerciseId
            }
            assertTrue { initialSelectedExercise.sets == submittedExercise.sets }
        }

        logger.i(
            TAG,
            "Assert previous exercise sets are deleted from DB."
        )
        val initialSelectedExercises2 = workoutDetails.exercises.filter {
            exerciseIds2.contains(it.exerciseId)
        }

        val exception2 = try {
            initialSelectedExercises2.forEach { initialSelectedExercise ->
                initialSelectedExercise.sets.forEach { set ->
                    set.setId ?: throw NoSuchElementException()

                    exerciseSetDao.getSetById(set.setId!!)
                }
            }

            null
        } catch (exception: NoSuchElementException) {
            exception
        }

        assertThrows(NoSuchElementException::class.java) {
            throw exception ?: return@assertThrows
        }
    }

    /**
     * Test 1 -> duplicate exercise with no sets
     * Test 2 -> duplicate exercise with sets
     * Test 3 -> no duplicate exercises
     */
    @RepeatedTest(50)
    @DisplayName("Find duplicate exercises using FindDuplicateExercisesUseCase test")
    fun `find duplicate exercises using FindDuplicateExercisesUseCase test`() = runTest {

        /**
         * Test 1 -> duplicate exercise with no sets
         */

        //Generate workout details
        val workoutDetails = MockupDataGeneratorV2.generateWorkoutDetails()
        logger.i(
            TAG,
            "Generated workout: $workoutDetails.\nExercises: ${workoutDetails.exercises}"
        )
        logger.i(TAG, "Total exercises: ${workoutDetails.exercises.size}")

        //Save workout details to DB
        val createCustomWorkoutDetailsState =
            workoutUseCases.createCustomWorkoutDetailsUseCase(workoutDetails).toList()

//        logger.i(TAG, "Create custom workout details -> isLoading state raised.")
//        assertTrue { createCustomWorkoutDetailsState[0].isLoading }

        logger.i(TAG, "Create custom workout details -> isSuccessful state raised.")
        assertTrue { createCustomWorkoutDetailsState[1].isSuccessful }

        val savedWorkoutDetails = createCustomWorkoutDetailsState[1].workoutDetails
        logger.i(TAG, "Saved workout details: $savedWorkoutDetails")

        //Generate exercises that are in the DB with different no ExerciseSets
        val n = 2
        val selectedExerciseList = savedWorkoutDetails.exercises
            .take(n)
            .map { exercise -> exercise.copy(sets = emptyList()) }
        logger.i(
            TAG,
            "Selected exercises that are already in workout: $selectedExerciseList"
        )

        val findDuplicateExercisesState = workoutUseCases.findDuplicateExercisesUseCase(
            workoutId = workoutDetails.workoutId,
            exerciseList = selectedExerciseList
        ).toList()

//        logger.i(TAG, "Find duplicate exercises in workout -> isLoading state raised")
//        assertTrue { findDuplicateExercisesState[0].isLoading }

        logger.i(TAG, "Find duplicate exercises in workout -> isSuccessful state raised")
        assertTrue { findDuplicateExercisesState[0].isSuccessful }

        logger.i(TAG, "Assert duplicate exercises are found")
        assertTrue { findDuplicateExercisesState[0].containsDuplicates }

        /**
         * Test 2 -> duplicate exercise with sets
         */

        //Generate exercises that are in the DB with different no ExerciseSets
        val n2 = 2
        val selectedExerciseList2 = savedWorkoutDetails.exercises.take(n2)
        logger.i(
            TAG,
            "Selected exercises that are already in workout 2: $selectedExerciseList2"
        )

        val findDuplicateExercisesState2 = workoutUseCases.findDuplicateExercisesUseCase(
            workoutId = workoutDetails.workoutId,
            exerciseList = selectedExerciseList2
        ).toList()

//        logger.i(TAG, "Find duplicate exercises in workout 2 -> isLoading state raised")
//        assertTrue { findDuplicateExercisesState2[0].isLoading }

        logger.i(TAG, "Find duplicate exercises in workout 2 -> isSuccessful state raised")
        assertTrue { findDuplicateExercisesState2[0].isSuccessful }

        logger.i(TAG, "Assert duplicate exercises are found")
        assertTrue { findDuplicateExercisesState2[0].containsDuplicates }

        /**
         * Test 3 -> no duplicate exercises
         */

        //Generate exercises that are are not in DB
        val selectedExerciseList3 =
            MockupDataGeneratorV2.generateExerciseList(
                n = 2,
                muscleGroup = workoutDetails.muscleGroup,
                excludedIds = workoutDetails.exercises.map { it.exerciseId },
                workoutId = workoutDetails.workoutId
            )
        logger.i(
            TAG,
            "Selected exercises that are not in workout 3: $selectedExerciseList3"
        )

        val findDuplicateExercisesState3 = workoutUseCases.findDuplicateExercisesUseCase(
            workoutId = workoutDetails.workoutId,
            exerciseList = selectedExerciseList3
        ).toList()

//        logger.i(TAG, "Find duplicate exercises in workout 3 -> isLoading state raised")
//        assertTrue { findDuplicateExercisesState3[0].isLoading }

        logger.i(TAG, "Find duplicate exercises in workout 3 -> isSuccessful state raised")
        assertTrue { findDuplicateExercisesState3[0].isSuccessful }

        logger.i(TAG, "Assert duplicate exercises are not found")
        assertTrue { !findDuplicateExercisesState3[0].containsDuplicates }
    }

    /**
     * Test 1 - favorited workout to favorite again
     * Test 2 - not favorited workout to favorite
     * Test 3 - fetch all favorite workouts
     */
    @RepeatedTest(50)
    @DisplayName("favorite workout using FavoriteWorkoutUseCase test and get favorite workout using GetFavoriteWorkoutsUseCase test")
    fun `favorite workout using FavoriteWorkoutUseCase test and get favorite workout using GetFavoriteWorkoutsUseCase test`() =
        runTest {

            /**
             * Test 1 - favorited workout to favorite again
             */

            //Generate workout
            val workout = MockupDataGeneratorV2.generateWorkout(isFavorite = true, workoutId = 1)
            logger.i(
                TAG,
                "Generated workout: $workout."
            )

            //Save workout details to DB
            val createCustomWorkoutState =
                workoutUseCases.createCustomWorkoutUseCase(workout).toList()

            logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState[1].isSuccessful }

            val savedWorkout = createCustomWorkoutState[1].workout
            logger.i(TAG, "Saved workout: $savedWorkout")

            val favoriteWorkoutState =
                workoutUseCases.favoriteWorkoutUseCase(savedWorkout.workoutId).toList()

            logger.i(TAG, "Favorite workout -> isLoading state raised.")
            assertTrue { favoriteWorkoutState[0].isLoading }

            logger.i(TAG, "Favorite workout -> isSuccessful state raised.")
            assertTrue { favoriteWorkoutState[1].isSuccessful }

            val getWorkoutState =
                workoutUseCases.getWorkoutUseCase(savedWorkout.workoutId).toList()

            logger.i(TAG, "Get favorited workout -> isLoading state raised.")
            assertTrue { getWorkoutState[0].isLoading }

            logger.i(TAG, "Get favorited workout -> isSuccessful state raised.")
            assertTrue { getWorkoutState[1].isSuccessful }

            logger.i(TAG, "Assert workout is still favorite in DB.")
            assertTrue { getWorkoutState[1].workout.isFavorite  }

            /**
             * Test 2 - not favorited workout to favorite
             */

            //Generate workout
            val workout2 = MockupDataGeneratorV2.generateWorkout(isFavorite = false, workoutId = 2)
            logger.i(
                TAG,
                "Generated workout 2: $workout2."
            )

            //Save workout details to DB
            val createCustomWorkoutState2 =
                workoutUseCases.createCustomWorkoutUseCase(workout2).toList()

            logger.i(TAG, "Create custom workout 2 -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState2[1].isSuccessful }

            val savedWorkout2 = createCustomWorkoutState2[1].workout
            logger.i(TAG, "Saved workout 2: $savedWorkout2")

            val favoriteWorkoutState2 =
                workoutUseCases.favoriteWorkoutUseCase(savedWorkout2.workoutId).toList()

            logger.i(TAG, "Favorite workout 2 -> isLoading state raised.")
            assertTrue { favoriteWorkoutState2[0].isLoading }

            logger.i(TAG, "Favorite workout 2 -> isSuccessful state raised.")
            assertTrue { favoriteWorkoutState2[1].isSuccessful }

            val getWorkoutState2 =
                workoutUseCases.getWorkoutUseCase(savedWorkout2.workoutId).toList()

            logger.i(TAG, "Get favorited workout 2 -> isLoading state raised.")
            assertTrue { getWorkoutState2[0].isLoading }

            logger.i(TAG, "Get favorited workout 2 -> isSuccessful state raised.")
            assertTrue { getWorkoutState2[1].isSuccessful }

            logger.i(TAG, "Assert workout 2 is favorite in DB.")
            assertTrue { getWorkoutState2[1].workout.isFavorite  }

            /**
             * Test 3 - fetch all favorite workouts
             */

            val getFavoriteWorkoutsState =
                workoutUseCases.getFavoriteWorkoutsUseCase().toList()

            logger.i(TAG, "Get favorite workouts -> isLoading state raised.")
            assertTrue { getFavoriteWorkoutsState[0].isLoading }

            logger.i(TAG, "Get favorite workouts -> isSuccessful state raised.")
            assertTrue { getFavoriteWorkoutsState[1].isSuccessful }

            logger.i(TAG, "Favorite workouts: ${getFavoriteWorkoutsState[1].workoutList}")
            logger.i(TAG, "Assert favorite workouts are 2")
            assertTrue { getFavoriteWorkoutsState[1].workoutList.size == 2 }
        }

    /**
     * Test 1 - favorited workout to unfavorite
     * Test 2 - not favorited workout to unfavorite again
     * Test 3 - fetch all favorite workouts
     */
    @RepeatedTest(50) //TODO: fix...
    @DisplayName("unfavorite workout using UnfavoriteWorkoutUseCase test and get favorite workout using GetFavoriteWorkoutsUseCase test")
    fun `unfavorite workout using UnfavoriteWorkoutUseCase test and get favorite workout using GetFavoriteWorkoutsUseCase test`() =
        runTest {

            /**
             * Test 1 - favorited workout to unfavorite
             */

            //Generate workout
            val workout = MockupDataGeneratorV2.generateWorkout(isFavorite = true)
            logger.i(
                TAG,
                "Generated workout: $workout."
            )

            //Save workout details to DB
            val createCustomWorkoutState =
                workoutUseCases.createCustomWorkoutUseCase(workout).toList()

            logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState[1].isSuccessful }

            val savedWorkout = createCustomWorkoutState[1].workout
            logger.i(TAG, "Saved workout: $savedWorkout")

            val unfavoriteWorkoutState =
                workoutUseCases.unfavoriteWorkoutUseCase(savedWorkout.workoutId).toList()

            logger.i(TAG, "Unfavorite workout -> isLoading state raised.")
            assertTrue { unfavoriteWorkoutState[0].isLoading }

            logger.i(TAG, "Unfavorite workout -> isSuccessful state raised.")
            assertTrue { unfavoriteWorkoutState[1].isSuccessful }

            val getWorkoutState =
                workoutUseCases.getWorkoutUseCase(savedWorkout.workoutId).toList()

            logger.i(TAG, "Get favorited workout -> isLoading state raised.")
            assertTrue { getWorkoutState[0].isLoading }

            logger.i(TAG, "Get favorited workout -> isSuccessful state raised.")
            assertTrue { getWorkoutState[1].isSuccessful }

            logger.i(TAG, "Assert workout is not favorite in DB.")
            assertTrue { !getWorkoutState[1].workout.isFavorite  }

            /**
             * Test 2 - not favorited workout to unfavorite again
             */

            //Generate workout
            val workout2 = MockupDataGeneratorV2.generateWorkout(isFavorite = false)
            logger.i(
                TAG,
                "Generated workout 2: $workout2."
            )

            //Save workout details to DB
            val createCustomWorkoutState2 =
                workoutUseCases.createCustomWorkoutUseCase(workout2).toList()

            logger.i(TAG, "Create custom workout 2 -> isSuccessful state raised.")
            assertTrue { createCustomWorkoutState2[1].isSuccessful }

            val savedWorkout2 = createCustomWorkoutState2[1].workout
            logger.i(TAG, "Saved workout 2: $savedWorkout2")

            val unfavoriteWorkoutState2 =
                workoutUseCases.unfavoriteWorkoutUseCase(savedWorkout2.workoutId).toList()

            logger.i(TAG, "Unfavorite workout 2 -> isLoading state raised.")
            assertTrue { unfavoriteWorkoutState2[0].isLoading }

            logger.i(TAG, "Unfavorite workout 2 -> isSuccessful state raised.")
            assertTrue { unfavoriteWorkoutState2[1].isSuccessful }

            val getWorkoutState2 =
                workoutUseCases.getWorkoutUseCase(savedWorkout2.workoutId).toList()

            logger.i(TAG, "Get favorited workout 2 -> isLoading state raised.")
            assertTrue { getWorkoutState2[0].isLoading }

            logger.i(TAG, "Get favorited workout 2 -> isSuccessful state raised.")
            assertTrue { getWorkoutState2[1].isSuccessful }

            logger.i(TAG, "Assert workout 2 is not favorite in DB.")
            assertTrue { !getWorkoutState2[1].workout.isFavorite  }

            /**
             * Test 3 - fetch all favorite workouts
             */

            val getFavoriteWorkoutsState =
                workoutUseCases.getFavoriteWorkoutsUseCase().toList()

            logger.i(TAG, "Get favorite workouts -> isLoading state raised.")
            assertTrue { getFavoriteWorkoutsState[0].isLoading }

            logger.i(TAG, "Get favorite workouts -> isSuccessful state raised.")
            assertTrue { getFavoriteWorkoutsState[1].isSuccessful }

            logger.i(TAG, "Favorite workouts: ${getFavoriteWorkoutsState[1].workoutList}")
            logger.i(TAG, "Favorite workouts size: ${getFavoriteWorkoutsState[1].workoutList.size}")
            logger.i(TAG, "Assert favorite workouts are 0")
            assertTrue { getFavoriteWorkoutsState[1].workoutList.isEmpty() }
        }
}


/**
 * Selected workout tests for single view screen (before refactoring to list view and migrating from select to favorite)
 */
//   /**
//     * Tested functions inside:
//     *
//     * GetFavoriteWorkoutsUseCase()
//     * WorkoutDao.getWorkoutByIsFavorite()
//     * ----------------------------
//     * CreateCustomWorkoutUseCase()
//     * WorkoutDao.insertWorkout()
//     * WorkoutDetailsDao.insertWorkoutDetails()
//     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
//     * ----------------------------
//     * UnfavoriteWorkoutUseCase()
//     * WorkoutDao.getWorkoutById()
//     *
//     * updateWorkoutUseCase()
//     * WorkoutDao.updateWorkout()
//     * WorkoutDetailsDao.getWorkoutDetailsById()
//     * WorkoutDetailsDao.updateWorkoutDetails()
//     * ----------------------------
//     * FavoriteWorkoutUseCase()
//     * WorkoutDao.getWorkoutByIsFavorite()
//     * WorkoutDao.selectWorkoutById()
//     */
//    @RepeatedTest(50)
//    @DisplayName("Select workout using FavoriteWorkoutUseCase test and get selected workout using GetFavoriteWorkoutsUseCase test")
//    fun `select workout using FavoriteWorkoutUseCase test and get selected workout using GetFavoriteWorkoutsUseCase test`() =
//        runTest {
//
//            logger.i(TAG, "Test 1 -> get selected workout on empty DB.")
//
//            //Fetch with empty DB -> no selected workout
//            val getSelectedWorkoutState = workoutUseCases.getFavoriteWorkoutsUseCase().toList()
//
//            logger.i(TAG, "Get selected workout -> isLoading state raised.")
//            assertTrue { getSelectedWorkoutState[0].isLoading }
//
//            logger.i(TAG, "Get selected workout -> isSuccessful state raised.")
//            assertTrue { getSelectedWorkoutState[1].isSuccessful }
//
//            val selectedWorkout = getSelectedWorkoutState[1].selectedWorkout
//            logger.i(TAG, "Selected workout 1: $selectedWorkout")
//
//            logger.i(TAG, "Assert no workout is currently selected. Workout DB is empty.")
//            assertNull(selectedWorkout)
//
//            //Generate workout
//            val workout = MockupDataGeneratorV2.generateWorkout(isFavorite = true)
//            logger.i(TAG, "Mocked workout: $workout")
//
//            //Insert Workout in DB
//            val createCustomWorkoutState =
//                workoutUseCases.createCustomWorkoutUseCase(workout).toList()
//
//            logger.i(TAG, "Create custom workout -> isLoading state raised.")
//            assertTrue { createCustomWorkoutState[0].isLoading }
//
//            logger.i(TAG, "Create custom workout -> isSuccessful state raised.")
//            assertTrue { createCustomWorkoutState[1].isSuccessful }
//
//            val savedWorkout = createCustomWorkoutState[1].workout
//            logger.i(TAG, "Saved workout: $savedWorkout")
//
//            logger.i(TAG, "Test 2 -> get selected workout after created a new one.")
//
//            //Check which workout is currently selected
//            val getSelectedWorkoutState2 = workoutUseCases.getFavoriteWorkoutsUseCase().toList()
//
//            logger.i(TAG, "Get selected workout 2 -> isLoading state raised.")
//            assertTrue { getSelectedWorkoutState2[0].isLoading }
//
//            logger.i(TAG, "Get selected workout 2 -> isSuccessful state raised.")
//            assertTrue { getSelectedWorkoutState2[1].isSuccessful }
//
//            val selectedWorkout2 = getSelectedWorkoutState2[1].selectedWorkout
//            logger.i(TAG, "Selected workout 2: $selectedWorkout2")
//
//            logger.i(TAG, "Assert workout 1 is currently selected.")
//            assertTrue { selectedWorkout2 == workout }
//
//            logger.i(
//                TAG,
//                "Test 3 -> get selected workout after created a new one and DB contains selected workout."
//            )
//
//            val workout2 = MockupDataGeneratorV2.generateWorkout(
//                isFavorite = true,
//                excludedIds = listOf(workout.workoutId)
//            )
//            logger.i(TAG, "Mocked workout 2: $workout2")
//
//            //Insert another selected workout in DB
//            val createCustomWorkoutState2 =
//                workoutUseCases.createCustomWorkoutUseCase(workout2).toList()
//
//            logger.i(TAG, "Create custom workout 2 -> isLoading state raised.")
//            assertTrue { createCustomWorkoutState2[0].isLoading }
//
//            logger.i(TAG, "Create custom workout 2 -> isSuccessful state raised.")
//            assertTrue { createCustomWorkoutState2[1].isSuccessful }
//
//            val savedWorkout2 = createCustomWorkoutState2[1].workout
//            logger.i(TAG, "Saved workout 2: $savedWorkout2")
//
//            //Check which workout is currently selected -> deselect workout and select workout 2
//            val getSelectedWorkoutState3 = workoutUseCases.getFavoriteWorkoutsUseCase().toList()
//
//            logger.i(TAG, "Get selected workout 3 -> isLoading state raised.")
//            assertTrue { getSelectedWorkoutState3[0].isLoading }
//
//            logger.i(TAG, "Get selected workout 3 -> isSuccessful state raised.")
//            assertTrue { getSelectedWorkoutState3[1].isSuccessful }
//
//            val selectedWorkout3 = getSelectedWorkoutState3[1].selectedWorkout
//            logger.i(TAG, "Selected workout 3: $selectedWorkout3")
//
//            logger.i(TAG, "Assert workout 2 is currently selected.")
//            assertTrue { selectedWorkout3 == workout2 }
//
//            logger.i(TAG, "Test 4 -> deselect workout 2.")
//
//            //Deselect workout
//            val deselectWorkoutState =
//                workoutUseCases.unfavoriteWorkoutUseCase(workout2.workoutId).toList()
//            logger.i(TAG, "Deselected workout 2 -> isLoading state raised.")
//            assertTrue { deselectWorkoutState[0].isLoading }
//
//            logger.i(TAG, "Deselected workout 2 -> isSuccessful state raised.")
//            assertTrue { deselectWorkoutState[1].isSuccessful }
//
//            //Check if DB has no selected workout
//            val getSelectedWorkoutState4 = workoutUseCases.getFavoriteWorkoutsUseCase().toList()
//
//            logger.i(TAG, "Get selected workout 4 -> isLoading state raised.")
//            assertTrue { getSelectedWorkoutState4[0].isLoading }
//
//            logger.i(TAG, "Get selected workout 4 -> isSuccessful state raised.")
//            assertTrue { getSelectedWorkoutState4[1].isSuccessful }
//
//            val selectedWorkout4 = getSelectedWorkoutState4[1].selectedWorkout
//            logger.i(TAG, "Selected workout 4: $selectedWorkout4")
//
//            logger.i(TAG, "Assert no workout is currently selected.")
//            assertNull(selectedWorkout4)
//
//            logger.i(TAG, "Test 5 -> select workout 1.")
//
//            //Fetch workout 1 from DB
//            val getWorkoutState = workoutUseCases.getWorkoutUseCase(workout.workoutId)
//                .toList()
//
//            logger.i(TAG, "Get workout 1 -> isLoading state raised.")
//            assertTrue { getWorkoutState[0].isLoading }
//
//            logger.i(TAG, "Get workout 1 -> isSuccessful state raised.")
//            assertTrue { getWorkoutState[1].isSuccessful }
//
//            val fetchedWorkout = getWorkoutState[1].workout
//            logger.i(TAG, "Assert workout 1 is not selected in the DB.")
//            assertFalse { fetchedWorkout.isFavorite }
//
//            //Select workout 1
//            logger.i(TAG, "Workout 1 is now selected.")
//            val selectWorkoutState =
//                workoutUseCases.favoriteWorkoutUseCase(workout.workoutId).toList()
//            logger.i(TAG, "Select workout 1 -> isLoading state raised.")
//            assertTrue { selectWorkoutState[0].isLoading }
//
//            logger.i(TAG, "Select workout 1 -> isSuccessful state raised.")
//            assertTrue { selectWorkoutState[1].isSuccessful }
//
//            //Fetch current selected workout
//            val getSelectedWorkoutState5 = workoutUseCases.getFavoriteWorkoutsUseCase().toList()
//
//            logger.i(TAG, "Get selected workout 5 -> isLoading state raised.")
//            assertTrue { getSelectedWorkoutState5[0].isLoading }
//
//            logger.i(TAG, "Get selected workout 5 -> isSuccessful state raised.")
//            assertTrue { getSelectedWorkoutState5[1].isSuccessful }
//
//            val selectedWorkout5 = getSelectedWorkoutState5[1].selectedWorkout
//            logger.i(TAG, "Selected workout 5: $selectedWorkout5")
//
//            logger.i(TAG, "Assert workout 1 is currently selected.")
//            assertTrue { selectedWorkout5 == workout }
//
//            //TODO: [Test] select workout with workoutID not in DB and fetch...
//            //TODO: [Test] select current selected workout and fetch...
//        }