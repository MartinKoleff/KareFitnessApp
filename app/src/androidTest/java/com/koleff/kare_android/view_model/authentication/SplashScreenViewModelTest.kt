package com.koleff.kare_android.view_model.authentication

import app.cash.turbine.test
import com.koleff.kare_android.authentication.data.CredentialsDataStoreFake
import com.koleff.kare_android.authentication.data.UserDaoFake
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.common.auth.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.auth.CredentialsValidator
import com.koleff.kare_android.common.auth.CredentialsValidatorImpl
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.model.dto.Tokens
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.data.repository.UserRepositoryImpl
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.LogoutUseCase
import com.koleff.kare_android.domain.usecases.RegisterUseCase
import com.koleff.kare_android.ui.state.LoginData
import com.koleff.kare_android.ui.state.LoginState
import com.koleff.kare_android.ui.view_model.SplashScreenViewModel
import com.koleff.kare_android.utils.TestLogger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class SplashScreenViewModelTest {

    companion object {
        private lateinit var dispatcher: CoroutineDispatcher

        private lateinit var credentialsAuthenticator: CredentialsAuthenticator
        private lateinit var credentialsValidator: CredentialsValidator
        private lateinit var credentialsDataStoreFake: CredentialsDataStoreFake
        private lateinit var authenticationUseCases: AuthenticationUseCases
        private lateinit var authenticationRepository: AuthenticationRepository
        private lateinit var authenticationDataSource: AuthenticationDataSource
        private lateinit var userRepository: UserRepository
        private lateinit var userDataSource: UserDataSource
        private lateinit var userDao: UserDaoFake

        private lateinit var preferences: Preferences
        private lateinit var splashScreenViewModel: SplashScreenViewModel

        private val isLogging = true
        private lateinit var logger: TestLogger

        private val validCredentials = Credentials(
            username = "username",
            password = "password"
        )
        private val validTokens = Tokens(
            accessToken = "accessToken",
            refreshToken = "refreshToken"
        )

        private const val TAG = "AuthenticationTest"

        @OptIn(ExperimentalCoroutinesApi::class)
        @JvmStatic
        @BeforeClass
        fun setup() {
            dispatcher = StandardTestDispatcher()
            Dispatchers.setMain(dispatcher)

            userDao = UserDaoFake()
            userDataSource = UserLocalDataSource(userDao)
            userRepository = UserRepositoryImpl(userDataSource)

            credentialsValidator = CredentialsValidatorImpl(userRepository)
            credentialsDataStoreFake = CredentialsDataStoreFake() //No caching needed for testing
            credentialsAuthenticator =
                CredentialsAuthenticatorImpl(credentialsValidator, credentialsDataStoreFake)

            authenticationDataSource = AuthenticationLocalDataSource(
                userDao,
                credentialsAuthenticator as CredentialsAuthenticatorImpl
            )
            authenticationRepository = AuthenticationRepositoryImpl(authenticationDataSource)

            authenticationUseCases = AuthenticationUseCases(
                loginUseCase = LoginUseCase(authenticationRepository, credentialsAuthenticator),
                registerUseCase = RegisterUseCase(
                    authenticationRepository,
                    credentialsAuthenticator
                ),
                logoutUseCase = LogoutUseCase(authenticationRepository)
            )

            logger = TestLogger(isLogging)
        }
    }

    @Before
    fun setUp() {
        preferences = mockk()
        authenticationUseCases = mockk()
        authenticationUseCases = mockk()
        dispatcher = mockk()
        splashScreenViewModel = SplashScreenViewModel(
            preferences = preferences,
            authenticationUseCases = authenticationUseCases,
            dispatcher = dispatcher
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginWithValidCachedCredentialsTest() = runTest {
        every { preferences.getCredentials() } returns validCredentials
        every { preferences.getTokens() } returns validTokens
        coEvery { authenticationUseCases.loginUseCase(validCredentials) } returns flow {
            emit(
                LoginState(
                    isSuccessful = true,
                    data = LoginData(
                        accessToken = validTokens.accessToken,
                        refreshToken = validTokens.refreshToken,
                        user = validCredentials
                    )
                )
            )
        }

        splashScreenViewModel.state.test {
            splashScreenViewModel.getCredentials()
            advanceTimeBy(1000)

            val state = awaitItem()
            assertEquals(true, state.isSuccessful)
            assertEquals(true, state.hasSignedIn)
            assertEquals(validCredentials, state.credentials)
            assertEquals(validTokens, state.tokens)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginWithNoCachedCredentialsTest() = runTest {
        every { preferences.getCredentials() } returns null
        every { preferences.getTokens() } returns null

        splashScreenViewModel.state.test {
            splashScreenViewModel.getCredentials()
            advanceTimeBy(1000)

            val state = awaitItem()
            assertEquals(true, state.isSuccessful)
            assertEquals(false, state.hasSignedIn)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun loginWithInvalidCachedCredentialsTest() = runTest {
        every { preferences.getCredentials() } returns validCredentials
        every { preferences.getTokens() } returns validTokens
        coEvery { authenticationUseCases.loginUseCase(validCredentials) } returns flow {
            emit(LoginState(isError = true, error = KareError.INVALID_CREDENTIALS))
        }

        splashScreenViewModel.state.test {
            splashScreenViewModel.getCredentials()
            advanceTimeBy(1000)

            val state = awaitItem()
            assertEquals(false, state.isSuccessful)
            assertEquals(false, state.hasSignedIn)
            assertEquals(true, state.isError)
            assertEquals(KareError.INVALID_CREDENTIALS, state.error)
        }
    }
}