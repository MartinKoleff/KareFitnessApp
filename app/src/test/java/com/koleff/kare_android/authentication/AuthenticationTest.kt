package com.koleff.kare_android.authentication

import com.koleff.kare_android.authentication.data.CredentialsDataStoreFake
import com.koleff.kare_android.authentication.data.UserDaoFake
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.credentials_validator.CredentialsValidator
import com.koleff.kare_android.common.credentials_validator.CredentialsValidatorImpl
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.data.repository.UserRepositoryImpl
import com.koleff.kare_android.data.room.entity.User
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.RegisterUseCase
import com.koleff.kare_android.utils.TestLogger
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import java.util.UUID
import java.util.stream.Stream

class AuthenticationTest {

    private lateinit var credentialsAuthenticator: CredentialsAuthenticator
    private lateinit var credentialsValidator: CredentialsValidator
    private lateinit var credentialsDataStoreFake: CredentialsDataStoreFake
    private lateinit var authenticationUseCases: AuthenticationUseCases
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var authenticationDataSource: AuthenticationDataSource
    private lateinit var userRepository: UserRepository
    private lateinit var userDataSource: UserDataSource
    private lateinit var userDao: UserDaoFake

    private val isLogging = true
    private lateinit var logger: TestLogger

    @BeforeEach
    fun setup() {
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

        loginUseCase = LoginUseCase(authenticationRepository, credentialsAuthenticator)
        registerUseCase = RegisterUseCase(authenticationRepository)
        authenticationUseCases = AuthenticationUseCases(
            loginUseCase,
            registerUseCase
        )

        logger = TestLogger(isLogging)
    }

    @ParameterizedTest(name = "Validates email {0}")
    @CsvSource(
        value = [
            "@gmail.com",
            "test@gmail",
            "testgmail.com",
            "testgmail.com",
            "@gmail.com",
            " "
        ]
    )
    fun `email validation test with invalid emails`(email: String) = runTest {
        val exception: Exception? = try {
            credentialsValidator.validateEmail(email)

            //Successfully validated
            null
        } catch (e: IllegalArgumentException) {
            e
        }

        assertThrows(IllegalArgumentException::class.java) {
            throw exception ?: return@assertThrows
        }
    }

    @ParameterizedTest(name = "Validates email {0}")
    @CsvSource(
        value = [
            "ivan@gmail.com",
            "valio@abv.bg",
            "martin@amazon.co.uk",
            "koleff@kare.com",
            "test@gmail.com.com"
        ]
    )
    fun `email validation test with valid emails`(email: String) = runTest {
        assertDoesNotThrow {
            credentialsValidator.validateEmail(email)
        }
    }

    @ParameterizedTest(name = "Validates username {0}")
    @CsvSource( //TODO: add to test to DB save flag for all tests...
        value = [
            "koleff777",
            "mkoleff",
            "vankatadlg",
            "starshinata",
            "1234567",
            "Sliv3nT0pa"
        ]
    )
    fun `username validation test with valid usernames`(username: String) = runTest {

        //Save entry to db before test...
        userDao.saveUser(
            User(
               UUID.randomUUID(),
                username,
                "password",
                "email@gmail.com"
            )
        )

        assertDoesNotThrow {
            credentialsValidator.validateUsername(username)
        }
    }

    @ParameterizedTest(name = "Validates username {0}")
    @CsvSource( //TODO: add to test to DB save flag for all tests...
        value = [
            "a",
            "123",
            "ivo",
            "a3c",
            "mk",
            " "
        ]
    )
    fun `username validation test with invalid username`(username: String) = runTest {

        //Save entry to db before test...
        userDao.saveUser(
            User(
                UUID.randomUUID(),
                username,
                "password",
                "email@gmail.com"
            )
        )

        val exception: Exception? = try {
            credentialsValidator.validateUsername(username)

            //Successfully validated
            null
        } catch (e: IllegalArgumentException) {
            e
        }

        assertThrows(IllegalArgumentException::class.java) {
            throw exception ?: return@assertThrows
        }
    }


    //TODO: password validation test...
    //TODO: credentials validation test...
    //TODO: login use case test
}