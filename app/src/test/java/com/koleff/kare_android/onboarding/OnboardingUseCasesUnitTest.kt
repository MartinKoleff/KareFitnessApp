package com.koleff.kare_android.onboarding

import com.koleff.kare_android.common.manager.data.MockupDataGeneratorV2
import com.koleff.kare_android.data.datasource.OnboardingLocalDataSource
import com.koleff.kare_android.data.repository.OnboardingRepositoryImpl
import com.koleff.kare_android.domain.repository.OnboardingRepository
import com.koleff.kare_android.domain.usecases.GetOnboardingUseCase
import com.koleff.kare_android.domain.usecases.OnboardingUseCases
import com.koleff.kare_android.domain.usecases.SaveOnboardingUseCase
import com.koleff.kare_android.onboarding.data.OnboardingDaoFake
import com.koleff.kare_android.utils.TestLogger
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.RepeatedTest

typealias OnboardingFakeDataSource = OnboardingLocalDataSource

class OnboardingUseCasesUnitTest {

    companion object {
        private lateinit var onboardingUseCases: OnboardingUseCases
        private lateinit var onboardingRepository: OnboardingRepository
        private lateinit var onboardingFakeDataSource: OnboardingFakeDataSource
        private lateinit var onboardingDao: OnboardingDaoFake

        private val useMockupDataSource = false
        private val isErrorTesting = false

        private val isLogging = true
        private lateinit var logger: TestLogger

        private const val TAG = "OnboardingUseCasesUnitTest"

        @JvmStatic
        @BeforeAll
        fun setup() = runTest {
            logger = TestLogger(isLogging)
            onboardingDao = OnboardingDaoFake()
            onboardingFakeDataSource = OnboardingFakeDataSource(onboardingDao)
            onboardingRepository = OnboardingRepositoryImpl(onboardingFakeDataSource)
            onboardingUseCases = OnboardingUseCases(
                getOnboardingUseCase = GetOnboardingUseCase(onboardingRepository),
                saveOnboardingUseCase = SaveOnboardingUseCase(onboardingRepository)
            )
        }

        @AfterEach
        fun tearDown() = runTest {
            onboardingDao.clearDB()

            logger.i("tearDown", "DB cleared!")
        }
    }

    @RepeatedTest(50)
    @DisplayName("Get onboarding data using GetOnboardingDataUseCase and Save onboarding data using SaveOnboardingData")
    fun testGetAndSaveOnboardingData() = runTest {
        val onboardingData = MockupDataGeneratorV2.generateOnboardingData().copy(id = -1)
        logger.i(TAG, "Generated onboarding data: $onboardingData")

        val saveOnboardingDataState = onboardingUseCases.saveOnboardingUseCase(onboardingData).toList()

        logger.i(TAG, "Save onboarding data -> isLoading state raised.")
        assertTrue { saveOnboardingDataState[0].isLoading }

        logger.i(TAG, "Save onboarding data -> isSuccessful state raised.")
        assertTrue { saveOnboardingDataState[1].isSuccessful }
        val savedOnboardingData = saveOnboardingDataState[1].onboardingData
        logger.i(TAG, "Save onboarding data -> $savedOnboardingData")

        val getOnboardingDataState = onboardingUseCases.getOnboardingUseCase(savedOnboardingData.id).toList()

        logger.i(TAG, "Get onboarding data -> isLoading state raised.")
        assertTrue { getOnboardingDataState[0].isLoading }

        logger.i(TAG, "Get onboarding data -> isSuccessful state raised.")
        assertTrue { getOnboardingDataState[1].isSuccessful }

        logger.i(TAG, "Assert fetched onboarding data from DB is the same as initial one")
        logger.i(TAG, "Onboarding data from DB: ${getOnboardingDataState[1].onboardingData}")
        assertTrue { getOnboardingDataState[1].onboardingData == onboardingData.copy(id = savedOnboardingData.id) }
    }
}


