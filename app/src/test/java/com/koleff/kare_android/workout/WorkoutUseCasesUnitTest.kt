package com.koleff.kare_android.workout

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSource
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDetailsDto
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.entity.relations.ExerciseWithSet
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeselectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.data.WorkoutDaoFake
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFake
import com.koleff.kare_android.workout.data.WorkoutMockupDataSource
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

typealias WorkoutFakeDataSource = WorkoutLocalDataSource

//TODO: remove mockup data sources? (how to integrate isError in local datasource?)
//TODO: add inner classes for each use case...
//TODO: add naming to each assertion...
class WorkoutUseCasesUnitTest {
    private lateinit var workoutDao: WorkoutDaoFake
    private lateinit var workoutDetailsDao: WorkoutDetailsDaoFake
    private lateinit var exerciseDao: ExerciseDaoFake
    private lateinit var exerciseSetDao: ExerciseSetDaoFake

    private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
    private lateinit var workoutMockupDataSource: WorkoutMockupDataSource
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var workoutUseCases: WorkoutUseCases

    private val useMockupDataSource = false
    private val isErrorTesting = false

    private val isLogging = true
    private lateinit var logger: TestLogger

    private val invalidWorkout = mockk<WorkoutDto>(relaxed = true)

    companion object {
        private const val TAG = "WorkoutUseCasesUnitTest"
    }

    @BeforeEach
    fun setup() = runBlocking {
        logger = TestLogger(isLogging)

        exerciseSetDao = ExerciseSetDaoFake()
        exerciseDao = ExerciseDaoFake(exerciseSetDao = exerciseSetDao, logger = logger)

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
            deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
            selectWorkoutUseCase = SelectWorkoutUseCase(workoutRepository),
            deselectWorkoutUseCase = DeselectWorkoutUseCase(workoutRepository),
            getSelectedWorkoutUseCase = GetSelectedWorkoutUseCase(workoutRepository),
            createNewWorkoutUseCase = CreateNewWorkoutUseCase(workoutRepository),
            createCustomWorkoutUseCase = CreateCustomWorkoutUseCase(workoutRepository),
            createCustomWorkoutDetailsUseCase = CreateCustomWorkoutDetailsUseCase(workoutRepository)
        )


        //Load ExerciseDao with all exercises
        for (muscleGroup in MuscleGroup.entries) {
            val exercisesList = ExerciseGenerator.loadExercises(muscleGroup)
            exerciseDao.insertAll(exercisesList)
        }
    }

    @AfterEach
    fun tearDown() {
        exerciseDao.clearDB()
        exerciseSetDao.clearDB()
        workoutDetailsDao.clearDB()

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
     * WorkoutLocalDataSource.getAllWorkouts()
     * WorkoutDao.getWorkoutsOrderedById()
     * WorkoutDao.insertWorkout()
     */
    @Test
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
    fun `get workout using GetWorkoutUseCase test`() = runTest {

        //Generate workout
        val workout = MockupDataGenerator.generateWorkout()
        logger.i(TAG, "Mocked workout: $workout")

        //Insert
        val id = workoutDao.insertWorkout(workout.toWorkout())
        logger.i(TAG, "Mocked workout inserted successfully. Workout id: $id")

        //Fetch
        val getWorkoutState = workoutUseCases.getWorkoutUseCase(id.toInt())
            .toList() //Calls getWorkoutById internally...

        logger.i(TAG, "Get workout -> isLoading state raised.")
        assertTrue { getWorkoutState[0].isLoading }

        logger.i(TAG, "Get workout -> isSuccessful state raised.")
        assertTrue { getWorkoutState[1].isSuccessful }

        val fetchedWorkout = getWorkoutState[1].workoutList.first()
        logger.i(TAG, "Fetched workout: $fetchedWorkout")

        logger.i(TAG, "Assert fetched workout is the same as inserted one.")
        assertTrue(fetchedWorkout == workout)
    }

    //TODO: [get workout using GetWorkoutUseCase test] -> Throw error for invalid ID test

    /**
     * Tested functions inside:
     *
     * GetWorkoutDetailsUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById()
     * ExerciseDao.getExerciseById()
     * ---------------------
     * CreateCustomWorkoutDetailsUseCase()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * WorkoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef()
     * ExerciseSetDao.saveSet()
     * ExerciseSetDao.updateSet() -> replaced by saveSet()
     * ExerciseDao.insertAllExerciseSetCrossRef()
     */
    @RepeatedTest(50)
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

            val exercisesWithSets = workoutDetailsDao.getWorkoutExercisesWithSets()
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
     * ExerciseSetDao.updateSet() -> replaced by saveSet()
     * ExerciseDao.insertAllExerciseSetCrossRef()
     * ---------------------
     * UpdateWorkoutDetailsUseCase()
     * WorkoutDetailsDao.getWorkoutDetailsById(workoutId)
     * WorkoutDetailsDao.insertAllWorkoutDetailsExerciseCrossRef(crossRefs)
     * ExerciseSetDao.saveSet(exerciseSet)
     * ExerciseSetDao.updateSet(exerciseSet) -> replaced by saveSet()
     * ExerciseDao.insertAllExerciseSetCrossRef(exerciseSetCrossRefs)
     * WorkoutDetailsDao.updateWorkoutDetails(workoutDetails.toWorkoutDetails())
     * ---------------------
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
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
                MockupDataGenerator.generateExercise(muscleGroup = muscleGroup)
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

            val fetchedWorkout = getWorkoutState[1].workoutList.first()
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
     * ExerciseDao.getExerciseById()
     */
    @RepeatedTest(50)
    fun `update workout using UpdateWorkoutUseCase test and CreateCustomWorkoutUseCase`() =
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

        //TODO: test when 2 entries are added and 1 deleted if 1 stays in DB...
    }

    @RepeatedTest(50)
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
                MockupDataGenerator.generateExercise(excludedIds = excludedIds)
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
                exercise.sets.forEach { set ->
                    set.setId ?: throw NoSuchElementException()

                    exerciseSetDao.getSetById(set.setId!!)
                }
            }
        }


}