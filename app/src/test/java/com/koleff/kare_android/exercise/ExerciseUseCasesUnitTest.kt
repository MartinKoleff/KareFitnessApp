package com.koleff.kare_android.exercise

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.ExerciseGenerator
import com.koleff.kare_android.data.datasource.ExerciseLocalDataSource
import com.koleff.kare_android.data.model.dto.MachineType
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseSetDaoFake
import com.koleff.kare_android.ui.event.OnFilterExercisesEvent
import com.koleff.kare_android.ui.event.OnSearchExerciseEvent
import com.koleff.kare_android.utils.TestLogger
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.stream.Stream

typealias ExerciseFakeDataSource = ExerciseLocalDataSource

class ExerciseUseCasesUnitTest {
    private lateinit var exerciseDBManager: ExerciseDBManager

    private lateinit var exerciseSetDao: ExerciseSetDaoFake
    private lateinit var exerciseDao: ExerciseDaoFake
    private lateinit var exerciseDetailsDao: ExerciseDetailsDaoFake

    private lateinit var exerciseFakeDataSource: ExerciseFakeDataSource
    private lateinit var exerciseRepository: ExerciseRepository

    private lateinit var exerciseUseCases: ExerciseUseCases

    private val useMockupDataSource = false
    private val isErrorTesting = false

    private val isLogging = true
    private lateinit var logger: TestLogger

    companion object {
        private const val TAG = "ExerciseUseCasesUnitTest"

        @JvmStatic
        fun provideMuscleGroupsAndSearchTexts(): Stream<Arguments> {
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
            val muscleGroups = MuscleGroup.entries.toTypedArray()
            return muscleGroups.flatMap { muscleGroup ->
                searchTexts.map { text ->
                    Arguments.of(muscleGroup.name, text) //Passing enum as string
                }
            }.stream()
        }
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
    fun tearDown() {
        exerciseDao.clearDB()
        exerciseDetailsDao.clearDB()
        exerciseSetDao.clearDB()

        logger.i("tearDown", "DB cleared!")
        logger.i("tearDown", "ExerciseDao: ${exerciseDao.getAllExercises()}")
    }

    /**
     * Tested functions inside:
     *
     * GetExercisesUseCase()
     * exerciseDao.getAllExercises()
     * exerciseDao.getExercisesOrderedById()
     */
    @ParameterizedTest(name = "Fetches all exercises for muscle group {0}")
    @CsvSource(
        value = [
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
        ]
    )
    fun `get exercises for each muscle group using GetExercisesUseCase test`(muscleGroupName: String) =
        runTest {
            val muscleGroup = MuscleGroup.valueOf(muscleGroupName)
            val supportedMuscleGroups = MuscleGroup.getSupportedMuscleGroups()

            val getExercisesState =
                exerciseUseCases.getCatalogExercisesUseCase(muscleGroup.muscleGroupId).toList()

            logger.i(TAG, "Get exercises for muscle group $muscleGroup -> isLoading state raised.")
            assertTrue { getExercisesState[0].isLoading }

            logger.i(
                TAG,
                "Get exercises for muscle group $muscleGroup -> isSuccessful state raised."
            )
            assertTrue { getExercisesState[1].isSuccessful }

            val exercises = getExercisesState[1].exerciseList
            logger.i(TAG, "Fetched exercises for muscle group $muscleGroup:\n$exercises")

            //Supported muscle group
            if (supportedMuscleGroups.contains(muscleGroup) || muscleGroup == MuscleGroup.ALL) {

                logger.i(TAG, "Assert exercises were fetched")
                assertTrue(exercises.isNotEmpty())

                //Fetched all exercises
                if (muscleGroup == MuscleGroup.ALL) {

                    logger.i(TAG, "Assert all exercises are fetched -> $muscleGroup")
                    logger.i(TAG, "Total exercises in DB: ${ExerciseGenerator.TOTAL_EXERCISES}")
                    logger.i(TAG, "Fetched exercises: ${exercises.size}")

                    assertTrue(exercises.size == ExerciseGenerator.TOTAL_EXERCISES)
                } else {
                    val exerciseIdRange = ExerciseGenerator.getMuscleGroupRange(muscleGroup)

                    logger.i(TAG, "Assert all exercises are from muscle group: $muscleGroup")
                    assertTrue(exercises.all { it.muscleGroup == muscleGroup })

                    logger.i(TAG, "Assert all exercises are in exercise id range: $exerciseIdRange")
                    assertTrue(exercises.all { it.exerciseId >= exerciseIdRange.first && it.exerciseId <= exerciseIdRange.second })
                }
            } else {
                logger.i(
                    TAG,
                    "Muscle group $muscleGroup currently not supported. No exercises for the muscle group."
                )

                logger.i(TAG, "Assert no exercises were fetched")
                assertTrue(exercises.isEmpty())
            }
        }

    /**
     * Tested functions inside:
     *
     * GetCatalogExerciseUseCase()
     * exerciseDao.getExerciseById()
     */
    @ParameterizedTest(name = "Fetches exercise with id {0}")
    @ValueSource(ints = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60])
    fun `get exercise using GetCatalogExerciseUseCase test`(exerciseId: Int) = runTest {
        val getExerciseState = exerciseUseCases.getCatalogExerciseUseCase(exerciseId).toList()

        logger.i(TAG, "Get exercise for exerciseId $exerciseId -> isLoading state raised.")
        assertTrue { getExerciseState[0].isLoading }

        logger.i(TAG, "Get exercise for exerciseId $exerciseId -> isSuccessful state raised.")
        assertTrue { getExerciseState[1].isSuccessful }

        val fetchedExercise = getExerciseState[1].exercise
        logger.i(TAG, "Fetched exercise: $fetchedExercise")

        val muscleGroupRange = ExerciseGenerator.getMuscleGroupRange(fetchedExercise.muscleGroup)

        logger.i(
            TAG,
            "Assert fetched exercise is from muscle group  ${fetchedExercise.muscleGroup} range: $muscleGroupRange"
        )
        assertTrue(fetchedExercise.exerciseId >= muscleGroupRange.first && fetchedExercise.exerciseId <= muscleGroupRange.second)

        val supportedMuscleGroups = MuscleGroup.getSupportedMuscleGroups()

        logger.i(TAG, "Assert fetched exercise is from supported muscle groups.")
        assertTrue { supportedMuscleGroups.contains(fetchedExercise.muscleGroup) }
    }

    /**
     * Tested functions inside:
     *
     * GetExerciseDetailsUseCase()
     * exerciseDetailsDao.getExerciseDetailsById()
     */
    @ParameterizedTest(name = "Fetches exercise details for catalog exercises with id {0}")
    @ValueSource(ints = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60])
    fun `get exercise details using GetExerciseDetailsUseCase test`(exerciseId: Int) = runTest {
        val getExerciseDetailsState =
            exerciseUseCases.getExerciseDetailsUseCase(exerciseId, Constants.CATALOG_EXERCISE_ID).toList()

        logger.i(TAG, "Get exercise details for exerciseId $exerciseId -> isLoading state raised.")
        assertTrue { getExerciseDetailsState[0].isLoading }

        logger.i(
            TAG,
            "Get exercise details for exerciseId $exerciseId -> isSuccessful state raised."
        )
        assertTrue { getExerciseDetailsState[1].isSuccessful }

        val fetchedExerciseDetails = getExerciseDetailsState[1].exercise
        logger.i(TAG, "Fetched exercise details: $fetchedExerciseDetails")

        val muscleGroupRange =
            ExerciseGenerator.getMuscleGroupRange(fetchedExerciseDetails.muscleGroup)

        logger.i(
            TAG,
            "Assert fetched exercise details is from muscle group  ${fetchedExerciseDetails.muscleGroup} range: $muscleGroupRange"
        )
        assertTrue(fetchedExerciseDetails.id >= muscleGroupRange.first && fetchedExerciseDetails.id <= muscleGroupRange.second)

        val supportedMuscleGroups = MuscleGroup.getSupportedMuscleGroups()

        logger.i(TAG, "Assert fetched exercise details is from supported muscle groups.")
        assertTrue { supportedMuscleGroups.contains(fetchedExerciseDetails.muscleGroup) }
    }

    /**
     * Tested functions inside:
     *
     * GetExercisesUseCase()
     * exerciseDao.getAllExercises()
     * exerciseDao.getExercisesOrderedById()
     * ------------------------
     * OnFilterExercisesUseCase()
     */
    @ParameterizedTest(name = "OnFilterExercises for muscle group {0}")
    @CsvSource(
        value = [
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
        ]
    )
    fun `OnFilterExercisesUseCase test`(muscleGroupName: String) = runTest {
        val muscleGroup = MuscleGroup.valueOf(muscleGroupName)
        val supportedMuscleGroups = MuscleGroup.getSupportedMuscleGroups()

        val getExercisesState =
            exerciseUseCases.getCatalogExercisesUseCase(muscleGroup.muscleGroupId).toList()

        logger.i(TAG, "Get exercises for muscle group $muscleGroup -> isLoading state raised.")
        assertTrue { getExercisesState[0].isLoading }

        logger.i(
            TAG,
            "Get exercises for muscle group $muscleGroup -> isSuccessful state raised."
        )
        assertTrue { getExercisesState[1].isSuccessful }

        val exercises = getExercisesState[1].exerciseList
        logger.i(TAG, "Fetched exercises for muscle group $muscleGroup:\n$exercises")

        //Supported muscle group
        if (supportedMuscleGroups.contains(muscleGroup) || muscleGroup == MuscleGroup.ALL) {

            //Barbell
            val event = OnFilterExercisesEvent.BarbellFilter(exercises)
            val onFilterEventState = exerciseUseCases.onFilterExercisesUseCase(event).toList()

            logger.i(TAG, "On barbell filter exercise -> isLoading state raised.")
            assertTrue { onFilterEventState[0].isLoading }

            logger.i(TAG, "On barbell filter exercise -> isSuccessful state raised.")
            assertTrue { onFilterEventState[1].isSuccessful }

            val filteredExerciseList = onFilterEventState[1].exerciseList
            logger.i(TAG, "Filtered exercise list by barbell: $filteredExerciseList")

            logger.i(TAG, "Assert only barbell exercises are filtered.")
            assertTrue {
                filteredExerciseList.map { it.machineType }.all { it == MachineType.BARBELL }
            }

            //Calisthenics
            val event2 = OnFilterExercisesEvent.CalisthenicsFilter(exercises)
            val onFilterEventState2 = exerciseUseCases.onFilterExercisesUseCase(event2).toList()

            logger.i(TAG, "On calisthenics filter exercise -> isLoading state raised.")
            assertTrue { onFilterEventState2[0].isLoading }

            logger.i(TAG, "On calisthenics filter exercise -> isSuccessful state raised.")
            assertTrue { onFilterEventState2[1].isSuccessful }

            val filteredExerciseList2 = onFilterEventState2[1].exerciseList
            logger.i(TAG, "Filtered exercise list by calisthenics: $filteredExerciseList2")

            logger.i(TAG, "Assert only calisthenics exercises are filtered.")
            assertTrue {
                filteredExerciseList2.map { it.machineType }.all { it == MachineType.CALISTHENICS }
            }

            //Dumbbell
            val event3 = OnFilterExercisesEvent.DumbbellFilter(exercises)
            val onFilterEventState3 = exerciseUseCases.onFilterExercisesUseCase(event3).toList()

            logger.i(TAG, "On dumbbell filter exercise -> isLoading state raised.")
            assertTrue { onFilterEventState3[0].isLoading }

            logger.i(TAG, "On dumbbell filter exercise -> isSuccessful state raised.")
            assertTrue { onFilterEventState3[1].isSuccessful }

            val filteredExerciseList3 = onFilterEventState3[1].exerciseList
            logger.i(TAG, "Filtered exercise list by dumbbell: $filteredExerciseList3")

            logger.i(TAG, "Assert only dumbbell exercises are filtered.")
            assertTrue {
                filteredExerciseList3.map { it.machineType }.all { it == MachineType.DUMBBELL }
            }

            //Machine
            val event4 = OnFilterExercisesEvent.MachineFilter(exercises)
            val onFilterEventState4 = exerciseUseCases.onFilterExercisesUseCase(event4).toList()

            logger.i(TAG, "On machine filter exercise -> isLoading state raised.")
            assertTrue { onFilterEventState4[0].isLoading }

            logger.i(TAG, "On machine filter exercise -> isSuccessful state raised.")
            assertTrue { onFilterEventState4[1].isSuccessful }

            val filteredExerciseList4 = onFilterEventState4[1].exerciseList
            logger.i(TAG, "Filtered exercise list by machine: $filteredExerciseList4")

            logger.i(TAG, "Assert only machine exercises are filtered.")
            assertTrue {
                filteredExerciseList4.map { it.machineType }.all { it == MachineType.MACHINE }
            }

            //No filter -> all exercises
            val event5 = OnFilterExercisesEvent.NoFilter(exercises)
            val onFilterEventState5 = exerciseUseCases.onFilterExercisesUseCase(event5).toList()

            logger.i(TAG, "On no filter exercise -> isLoading state raised.")
            assertTrue { onFilterEventState5[0].isLoading }

            logger.i(TAG, "On no filter exercise -> isSuccessful state raised.")
            assertTrue { onFilterEventState5[1].isSuccessful }

            val filteredExerciseList5 = onFilterEventState5[1].exerciseList
            logger.i(TAG, "Filtered exercise list by no filter: $filteredExerciseList5")

            logger.i(TAG, "Assert all exercises are fetched.")
            val totalExercisesForMuscleGroup =
                if (muscleGroup == MuscleGroup.ALL) ExerciseGenerator.TOTAL_EXERCISES else MuscleGroup.getTotalExercises(
                    muscleGroup
                )

            logger.i(
                TAG,
                "Total exercises for muscle group $muscleGroup: $totalExercisesForMuscleGroup "
            )
            logger.i(TAG, "Filtered exercises: ${filteredExerciseList5.size}")
            assertTrue { filteredExerciseList5.size == totalExercisesForMuscleGroup }

            //Valio beshe tuk
        } else {
            logger.i(
                TAG,
                "Muscle group $muscleGroup currently not supported. No exercises for the muscle group."
            )
        }
    }

    /**
     * Tested functions inside:
     *
     * GetExercisesUseCase()
     * exerciseDao.getAllExercises()
     * exerciseDao.getExercisesOrderedById()
     * ------------------------
     * OnSearchExerciseUseCase()
     */

    @ParameterizedTest(name = "OnSearchExercises for muscle group {0} and search text {1}")
    @MethodSource("provideMuscleGroupsAndSearchTexts")
    fun `OnSearchExercisesUseCase test`(muscleGroupName: String, searchText: String) = runTest {
        val muscleGroup = MuscleGroup.valueOf(muscleGroupName)
        val supportedMuscleGroups = MuscleGroup.getSupportedMuscleGroups()

        logger.i(TAG, "Muscle group for this test: {$muscleGroup}.")
        logger.i(TAG, "Search text for this test: {$searchText}.")

        val getExercisesState =
            exerciseUseCases.getCatalogExercisesUseCase(muscleGroup.muscleGroupId).toList()

        logger.i(TAG, "Get exercises for muscle group $muscleGroup -> isLoading state raised.")
        assertTrue { getExercisesState[0].isLoading }

        logger.i(
            TAG,
            "Get exercises for muscle group $muscleGroup -> isSuccessful state raised."
        )
        assertTrue { getExercisesState[1].isSuccessful }

        val exercises = getExercisesState[1].exerciseList
        logger.i(TAG, "Fetched exercises for muscle group $muscleGroup:\n$exercises")

        //Supported muscle group
        if (supportedMuscleGroups.contains(muscleGroup) || muscleGroup == MuscleGroup.ALL) {
            val event = OnSearchExerciseEvent.OnSearchTextChange(searchText, exercises)

            val onSearchState = exerciseUseCases.onSearchExerciseUseCase(event).last()

//            logger.i(TAG, "On search filter exercise -> isLoading state raised.")
//            assertTrue { onSearchState[0].isLoading }
//
//            logger.i(TAG, "On search filter exercise -> isSuccessful state raised.")
//            assertTrue { onSearchState[1].isSuccessful }

            val filteredExerciseList = onSearchState.exerciseList
            logger.i(
                TAG,
                "Filtered exercise list by search text {$searchText}: $filteredExerciseList"
            )

            logger.i(
                TAG,
                "Assert all exercises contain the search text {$searchText} in their names or the exercise list is empty: ${filteredExerciseList.isEmpty()}."
            )
            assertTrue(filteredExerciseList.any {
                it.name.contains(
                    searchText,
                    ignoreCase = true
                )
            } || filteredExerciseList.isEmpty())
        } else {
            logger.i(
                TAG,
                "Muscle group $muscleGroup currently not supported. No exercises for the muscle group."
            )
        }

        //TODO: [TEST] OnToggleSearch...
    }
}
//TODO: [TEST] catalog vs normal exercise from workout tests...