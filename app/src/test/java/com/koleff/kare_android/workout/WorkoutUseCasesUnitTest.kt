package com.koleff.kare_android.workout

import android.util.Log
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSource
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.workout.data.WorkoutDaoFake
import com.koleff.kare_android.workout.data.WorkoutDetailsDaoFake
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
import com.koleff.kare_android.workout.data.WorkoutMockupDataSource
import io.mockk.every
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceUntilIdle
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue

typealias WorkoutFakeDataSource = WorkoutLocalDataSource

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

    companion object{
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

        //Log
//        mockkStatic(Log::class)
//        every { Log.v(any(), any()) } returns 0
//        every { Log.d(any(), any()) } returns 0
//        every { Log.i(any(), any()) } returns 0
//        every { Log.e(any(), any()) } returns 0

        logger = TestLogger()
    }

    @Test
    fun `create workout test`() = runTest {
        val workouts = workoutDao.getWorkoutsOrderedById()
        val workoutsInDB = workouts.size

        val createWorkoutState = workoutUseCases.createWorkoutUseCase().toList() //invoke() doesn't work

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

        logger.i(TAG, "Assert name is Workout $workoutsInDBAfterCreate -> all workouts in DB index")
        assert(createdWorkout.name == "Workout $workoutsInDBAfterCreate")

        logger.i(TAG, "Assert no exercises are in Workout")
        assert(createdWorkout.totalExercises == 0)

        val createdWorkoutDetails =
            workoutDetailsDao.getWorkoutDetailsById(createdWorkout.workoutId)

        logger.i(TAG, "Assert no exercises are in WorkoutDetails")
        assert(createdWorkoutDetails?.exercises?.isEmpty() == true)

        logger.i(TAG, "Assert name is Workout $workoutsInDBAfterCreate -> all workouts in DB index")
        assert(createdWorkoutDetails?.workoutDetails?.name == "Workout $workoutsInDBAfterCreate")
    }

    @Test
    fun `create workout emits success state`() = runTest {
        val createWorkoutState = workoutUseCases.createWorkoutUseCase().toList()

        logger.i(TAG, "isLoading state raised.")
        assertTrue { createWorkoutState.any { it.isLoading } }

        logger.i(TAG, "isSuccessful state raised.")
        assertTrue { createWorkoutState.any { it.isSuccessful } }

        val successState = createWorkoutState.find { it.isSuccessful }

        logger.i(TAG, "Workout created: ${successState?.workout}")
        assertTrue { successState?.workout != null }
    }
}