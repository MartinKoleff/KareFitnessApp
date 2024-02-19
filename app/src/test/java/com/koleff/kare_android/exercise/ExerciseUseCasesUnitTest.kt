package com.koleff.kare_android.exercise

import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseLocalDataSource
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseMockupDataSource
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.utils.TestLogger
import com.koleff.kare_android.workout.WorkoutUseCasesUnitTest
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

typealias ExerciseFakeDataSource = ExerciseLocalDataSource

class ExerciseUseCasesUnitTest {

    private lateinit var exerciseSetDao: ExerciseSetDaoFake
    private lateinit var exerciseDao: ExerciseDaoFake
    private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake

    private lateinit var exerciseMockupDataSource: ExerciseMockupDataSource
    private lateinit var exerciseFakeDataSource: ExerciseFakeDataSource
    private lateinit var exerciseRepository: ExerciseRepository

    private lateinit var exerciseUseCases: ExerciseUseCases

    private val useMockupDataSource = false
    private val isErrorTesting = false

    private val isLogging = true
    private lateinit var logger: TestLogger

    companion object {
        private const val TAG = "ExerciseUseCasesUnitTest"
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

        exerciseMockupDataSource = ExerciseMockupDataSource(isError = isErrorTesting)
        exerciseFakeDataSource = ExerciseFakeDataSource(
            exerciseDao = exerciseDao,
            exerciseDetailsDao = exerciseDetailsDao
        )

        exerciseRepository = ExerciseRepositoryImpl(
            if (useMockupDataSource) exerciseMockupDataSource else exerciseFakeDataSource
        )

        exerciseUseCases = ExerciseUseCases(
            onSearchExerciseUseCase = OnSearchExerciseUseCase(),
            onFilterExercisesUseCase = OnFilterExercisesUseCase(),
            getExerciseDetailsUseCase = GetExerciseDetailsUseCase(exerciseRepository),
            getExercisesUseCase = GetExercisesUseCase(exerciseRepository),
            getExerciseUseCase = GetExerciseUseCase(exerciseRepository)
        )

        //Load ExerciseDao with all exercises
        for (muscleGroup in MuscleGroup.entries) {
            val exercisesList = ExerciseGenerator.loadExercises(muscleGroup)
            exerciseDao.insertAll(exercisesList)
        }
    }

    @ParameterizedTest(name = "Fetches all exercises for muscle group {0}")
    @CsvSource(value = [
        "CHEST",
        "BACK",
        "TRICEPS",
        "BICEPS",
        "SHOULDERS",
        "LEGS",
        "ABS",
        "CARDIO",
        "FULL_BODY",
        "PUSH_PULL_LEGS",
        "UPPER_LOWER_BODY",
        "ARMS",
        "OTHER",
        "ALL"
    ])
    fun `get exercises for each muscle group using GetExercisesUseCase test`(muscleGroupName: String) = runTest {
        val muscleGroup = MuscleGroup.valueOf(muscleGroupName)
        val supportedMuscleGroups = MuscleGroup.getSupportedMuscleGroups()


        val getExercisesState = exerciseUseCases.getExercisesUseCase(muscleGroup.muscleGroupId).toList()

        logger.i(TAG, "Get exercises for muscle group $muscleGroup -> isLoading state raised.")
        assertTrue { getExercisesState[0].isLoading }

        logger.i(TAG, "Get exercises for muscle group $muscleGroup -> isSuccessful state raised.")
        assertTrue { getExercisesState[1].isSuccessful }

        val exercises = getExercisesState[1].exerciseList
        logger.i(TAG, "Fetched exercises for muscle group $muscleGroup:\n$exercises")

        //Supported muscle group
        if(supportedMuscleGroups.contains(muscleGroup) || muscleGroup == MuscleGroup.ALL) {

            logger.i(TAG, "Assert exercises were fetched")
            assertTrue(exercises.isNotEmpty())

            //Fetched all exercises
            if(muscleGroup == MuscleGroup.ALL) {

                logger.i(TAG, "Assert all exercises are fetched -> $muscleGroup")
                logger.i(TAG, "Total exercises in DB: ${ExerciseGenerator.TOTAL_EXERCISES}")
                logger.i(TAG, "Fetched exercises: ${exercises.size}")

                assertTrue(exercises.size == ExerciseGenerator.TOTAL_EXERCISES)
            }else{
                val exerciseIdRange = ExerciseGenerator.getMuscleGroupRange(muscleGroup)

                logger.i(TAG, "Assert all exercises are from muscle group: $muscleGroup")
                assertTrue(exercises.all { it.muscleGroup == muscleGroup })

                logger.i(TAG, "Assert all exercises are in exercise id range: $exerciseIdRange")
                assertTrue(exercises.all { it.exerciseId >= exerciseIdRange.first && it.exerciseId <= exerciseIdRange.second })
            }
        }else{

            logger.i(TAG, "Assert no exercises were fetched")
            assertTrue(exercises.isEmpty())
        }
    }

    
}