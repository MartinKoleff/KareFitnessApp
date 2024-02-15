package com.koleff.kare_android.workout

import com.koleff.kare_android.common.MockupDataGenerator
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSource
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.CreateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
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
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test

typealias WorkoutFakeDataSource = WorkoutLocalDataSource

//TODO: remove mockup data sources? (how to integrate isError in local datasource?)
//TODO: add inner classes for each use case...
//TODO: add naming to each assertion...
class WorkoutUseCasesUnitTest {
    private lateinit var workoutDao: WorkoutDao
    private lateinit var workoutDetailsDao: WorkoutDetailsDao
    private lateinit var exerciseDao: ExerciseDao
    private lateinit var exerciseSetDao: ExerciseSetDao

    private lateinit var workoutFakeDataSource: WorkoutFakeDataSource
    private lateinit var workoutMockupDataSource: WorkoutMockupDataSource
    private lateinit var workoutRepository: WorkoutRepository

    private lateinit var workoutUseCases: WorkoutUseCases

    private val useMockupDataSource = false
    private val isErrorTesting = false

    private lateinit var logger: TestLogger

    private val invalidWorkout = mockk<WorkoutDto>(relaxed = true)

    companion object {
        private const val TAG = "WorkoutUseCasesUnitTest"
    }

    @BeforeEach
    fun setup() {
        workoutDao = WorkoutDaoFake()
        workoutDetailsDao = WorkoutDetailsDaoFake()
        exerciseDao = ExerciseDaoFake()
        exerciseSetDao = ExerciseSetDaoFake()

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
            getWorkoutsUseCase = GetWorkoutsUseCase(workoutRepository),
            getWorkoutUseCase = GetWorkoutUseCase(workoutRepository),
            updateWorkoutUseCase = UpdateWorkoutUseCase(workoutRepository),
            onSearchWorkoutUseCase = OnSearchWorkoutUseCase(),
            deleteExerciseUseCase = DeleteExerciseUseCase(workoutRepository),
            deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
            selectWorkoutUseCase = SelectWorkoutUseCase(workoutRepository),
            getSelectedWorkoutUseCase = GetSelectedWorkoutUseCase(workoutRepository),
            createWorkoutUseCase = CreateWorkoutUseCase(workoutRepository)
        )

        logger = TestLogger()
    }

    /**Tested functions inside:
     * CreateWorkoutUseCase()
     * WorkoutLocalDataSource.createNewWorkout()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.updateWorkout()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * WorkoutDao.insertWorkoutDetailsWorkoutCrossRef()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutsOrderedById()
     */
    @Test
    fun `create workout from CreateWorkoutUseCase test`() =
        runTest { //TODO: create workout when there is already workout in DB...
            val workouts = workoutDao.getWorkoutsOrderedById()
            val workoutsInDB = workouts.size

            val createWorkoutState =
                workoutUseCases.createWorkoutUseCase().toList()

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
        }

    /**Tested functions inside:
     * GetWorkoutsUseCase()
     * WorkoutLocalDataSource.getAllWorkouts()
     * WorkoutDao.getWorkoutsOrderedById()
     * WorkoutDao.insertWorkout()
     */
    @Test
    fun `get workouts from GetWorkoutsUseCase test`() = runTest {
        //Fetch
        val getWorkoutsState = workoutUseCases.getWorkoutsUseCase().toList()

        logger.i(TAG, "Get workouts -> isLoading state raised.")
        assertTrue { getWorkoutsState[0].isLoading }

        logger.i(TAG, "Get workouts -> isSuccessful state raised.")
        assertTrue { getWorkoutsState[1].isSuccessful }

        logger.i(TAG, "Assert workout DB is initially empty.")
        assertTrue(getWorkoutsState[1].workoutList.isEmpty())

        //TODO: insert 2 workouts and fetch again...
    }

    /**Tested functions inside:
     * GetWorkoutUseCase()
     * WorkoutDao.insertWorkout()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
    fun `get workout from GetWorkoutUseCase test`() = runTest { //TODO: Throw error for invalid ID

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

    /**Tested functions inside:
     * GetWorkoutDetailsUseCase()
     * WorkoutDetailsDao.insertWorkoutDetails()
     * ExerciseDao.getExerciseById()
     * WorkoutDao.getWorkoutById()
     */
    @RepeatedTest(50)
    fun `get workout details from GetWorkoutsDetailsUseCase test`() = runTest {

        //Generate workout details
        val workoutDetails = MockupDataGenerator.generateWorkoutDetails()
        logger.i(TAG, "Mocked workout details: $workoutDetails")

        //Insert //TODO: fix save...
        val id = workoutDetailsDao.insertWorkoutDetails(workoutDetails.toWorkoutDetails())
        logger.i(TAG, "Mocked workout details inserted successfully. Workout details id: $id")

        //Fetch
        val getWorkoutDetailsState = workoutUseCases.getWorkoutDetailsUseCase(id.toInt()).toList()

        logger.i(TAG, "Get workout details -> isLoading state raised.")
        assertTrue { getWorkoutDetailsState[0].isLoading }

        logger.i(TAG, "Get workout details -> isSuccessful state raised.")
        assertTrue { getWorkoutDetailsState[1].isSuccessful }

        val fetchedWorkoutDetails = getWorkoutDetailsState[1].workout
        logger.i(TAG, "Fetched workout details: $fetchedWorkoutDetails")

        logger.i(TAG, "Assert fetched workout details is the same as inserted one.")
        assertTrue(fetchedWorkoutDetails == workoutDetails)
    }


}