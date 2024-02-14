package com.koleff.kare_android.workout

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
import com.koleff.kare_android.workout.data.WorkoutMockupDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlinx.coroutines.test.runTest

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
    }
}