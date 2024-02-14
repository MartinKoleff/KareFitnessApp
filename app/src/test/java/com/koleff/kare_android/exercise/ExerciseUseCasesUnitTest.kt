package com.koleff.kare_android.exercise

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseLocalDataSource
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.exercise.data.ExerciseDaoFake
import com.koleff.kare_android.exercise.data.ExerciseDetailsDaoFake
import com.koleff.kare_android.exercise.data.ExerciseMockupDataSource
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

typealias ExerciseFakeDataSource = ExerciseLocalDataSource

class ExerciseUseCasesUnitTest {

    private lateinit var exerciseDao: ExerciseDao
    private lateinit var exerciseDetailsDao: ExerciseDetailsDao

    private lateinit var exerciseMockupDataSource: ExerciseMockupDataSource
    private lateinit var exerciseFakeDataSource: ExerciseFakeDataSource
    private lateinit var exerciseRepository: ExerciseRepository

    private lateinit var exerciseUseCases: ExerciseUseCases

    private val useMockupDataSource = false
    private val isErrorTesting = false

    @BeforeEach
    fun setup() {
        exerciseDao = ExerciseDaoFake()
        exerciseDetailsDao = ExerciseDetailsDaoFake()

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

    }
}