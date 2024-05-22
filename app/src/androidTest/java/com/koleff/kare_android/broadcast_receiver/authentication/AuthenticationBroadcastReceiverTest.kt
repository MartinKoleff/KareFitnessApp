package com.koleff.kare_android.broadcast_receiver.authentication

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import com.koleff.kare_android.authentication.data.CredentialsDataStoreFake
import com.koleff.kare_android.authentication.data.NavigationControllerFake
import com.koleff.kare_android.authentication.data.TokenDataStoreFake
import com.koleff.kare_android.authentication.data.UserDaoFake
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.common.auth.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.auth.CredentialsValidator
import com.koleff.kare_android.common.auth.CredentialsValidatorImpl
import com.koleff.kare_android.common.broadcast.LogoutBroadcastReceiver
import com.koleff.kare_android.common.broadcast.LogoutHandler
import com.koleff.kare_android.common.broadcast.RegenerateTokenBroadcastReceiver
import com.koleff.kare_android.common.broadcast.RegenerateTokenHandler
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.data.repository.UserRepositoryImpl
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.LogoutUseCase
import com.koleff.kare_android.domain.usecases.RegenerateTokenUseCase
import com.koleff.kare_android.domain.usecases.RegisterUseCase
import com.koleff.kare_android.utils.TestLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.BeforeClass
import org.junit.Test

class AuthenticationBroadcastReceiverTest {

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

        private val isLogging = true
        private lateinit var logger: TestLogger

        private lateinit var navigationControllerFake: NavigationControllerFake
        private lateinit var logoutHandler: LogoutHandler
        private lateinit var logoutBroadcastReceiver: LogoutBroadcastReceiver

        private lateinit var tokenDataStoreFake: TokenDataStoreFake
        private lateinit var regenerateTokenHandler: RegenerateTokenHandler
        private lateinit var regenerateTokenBroadcastReceiver: RegenerateTokenBroadcastReceiver

        private const val TAG = "AuthenticationBroadcastReceiverTest"

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
                logoutUseCase = LogoutUseCase(authenticationRepository),
                regenerateTokenUseCase = RegenerateTokenUseCase(authenticationRepository)
            )

            navigationControllerFake = NavigationControllerFake()
            logoutHandler = LogoutHandler(
                authenticationUseCases = authenticationUseCases,
                credentialsDataStore = credentialsDataStoreFake,
                navigationController = navigationControllerFake
            )

            logoutBroadcastReceiver = LogoutBroadcastReceiver(
                logoutHandler = logoutHandler
            )

            regenerateTokenHandler = RegenerateTokenHandler(
                authenticationUseCases = authenticationUseCases,
                tokenDataStore = tokenDataStoreFake
            )
            regenerateTokenBroadcastReceiver = RegenerateTokenBroadcastReceiver(
                regenerateTokenHandler = regenerateTokenHandler
            )

            logger = TestLogger(isLogging)
        }
    }

    @Test
    fun logoutUseCaseTest() = runTest {
        val logoutIntent = Intent(Constants.ACTION_LOGOUT)

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        logoutBroadcastReceiver.onReceive(appContext, logoutIntent)

        assertTrue(credentialsDataStoreFake.isCleared)

        assertTrue(navigationControllerFake.isBackstackCleared)
        assertNotNull(navigationControllerFake.lastDestination)
    }

    @Test
    fun regenerateTokenUseCaseTest() = runTest {
        val regenerateTokenIntent = Intent(Constants.ACTION_REGENERATE_TOKEN)

        val appContext = InstrumentationRegistry.getInstrumentation().targetContext

        regenerateTokenBroadcastReceiver.onReceive(appContext, regenerateTokenIntent)

        assertNotNull(tokenDataStoreFake.getTokens())
    }
}