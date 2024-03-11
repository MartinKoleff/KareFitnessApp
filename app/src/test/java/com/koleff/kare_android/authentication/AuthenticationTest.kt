package com.koleff.kare_android.authentication

import com.koleff.kare_android.authentication.data.CredentialsDataStoreFake
import com.koleff.kare_android.authentication.data.UserDaoFake
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.common.auth.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.auth.CredentialsValidator
import com.koleff.kare_android.common.auth.CredentialsValidatorImpl
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.data.repository.UserRepositoryImpl
import com.koleff.kare_android.data.room.entity.User
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.RegisterUseCase
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.utils.TestLogger
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.MethodSource
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

    companion object {
        private const val TAG = "AuthenticationTest"

        @JvmStatic
        fun provideValidUsernames(): Stream<Arguments> {
            val usernames = listOf(
                "koleff777",
                "mkoleff",
                "vankatadlg",
                "starshinata",
                "1234567",
                "Sliv3nT0pa"
            )
            val saveToDBFlags =
                listOf(
                    true,
                    false
                )
            return usernames.flatMap { username ->
                saveToDBFlags.map { flag ->
                    Arguments.of(username, flag)
                }
            }.stream()
        }

        @JvmStatic
        fun provideInvalidUsernames(): Stream<Arguments> {
            val usernames = listOf(
                "a",
                "123",
                "ivo",
                "a3c",
                "mk",
                " "
            )
            val saveToDBFlags =
                listOf(
                    true,
                    false
                )
            return usernames.flatMap { username ->
                saveToDBFlags.map { flag ->
                    Arguments.of(username, flag)
                }
            }.stream()
        }

        @JvmStatic
        fun provideInvalidCredentials(): Stream<Arguments> {
            val usernames = listOf(
                "a",
                "123",
                "ivo",
                "a3c",
                "mk",
                " "
            )
            val passwords = listOf(
                "k0leff",
                "123456",
                "sliv3n",
                "parola",
                "a",
                "abc"
            )

            val emails = listOf(
                "@gmail.com",
                "test@gmail",
                "testgmail.com",
                "testgmail.com",
                "@gmail.com"
            )

            val saveToDBFlags =
                listOf(
                    true,
                    false
                )

            return usernames.flatMap { username ->
                passwords.flatMap { password ->
                    emails.flatMap { email ->
                        saveToDBFlags.map { flag ->
                            Arguments.of(
                                Credentials(
                                    UUID.randomUUID(),
                                    username,
                                    password,
                                    email
                                ), flag
                            )
                        }
                    }
                }
            }.stream()
        }

        @JvmStatic
        fun provideValidCredentials(): Stream<Arguments> {
            val usernames = listOf(
                "koleff777",
                "mkoleff",
                "vankatadlg",
                "1234567",
                "Sliv3nT0pa"
            )

            val passwords = listOf(
                "password",
                "mkoleff",
                "sliv3nT0pa",
                "spasKoleff",
                "valioIspaneca",
                "1234567"
            )

            val emails = listOf(
                "ivan@gmail.com",
                "valio@abv.bg",
                "martin@amazon.co.uk",
                "koleff@kare.com",
                "test@gmail.com.com"
            )

            val saveToDBFlags =
                listOf(
                    true,
                    false
                )

            return usernames.flatMap { username ->
                passwords.flatMap { password ->
                    emails.flatMap { email ->
                        saveToDBFlags.map { flag ->
                            Arguments.of(
                                Credentials(
                                    UUID.randomUUID(),
                                    username,
                                    password,
                                    email
                                ), flag
                            )
                        }
                    }
                }
            }.stream()
        }
    }

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
            "@gmail.com"
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

    @ParameterizedTest(name = "Validates username: {0} and save to DB: {1}")
    @MethodSource("provideValidUsernames")
    fun `username validation test with valid usernames`(username: String, saveToDB: Boolean) =
        runTest {

            //Save entry to db before test...
            if (saveToDB) {
                userDao.saveUser(
                    User(
                        UUID.randomUUID(),
                        username,
                        "password",
                        "email@gmail.com"
                    )
                )
            }

            assertDoesNotThrow {
                credentialsValidator.validateUsername(username)
            }
        }

    @ParameterizedTest(name = "Validates username: {0} and save to DB: {1}")
    @MethodSource("provideInvalidUsernames")
    fun `username validation test with invalid username`(username: String, saveToDB: Boolean) =
        runTest {

            //Save entry to db before test...
            if (saveToDB) {
                userDao.saveUser(
                    User(
                        UUID.randomUUID(),
                        username,
                        "password",
                        "email@gmail.com"
                    )
                )
            }

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

    @ParameterizedTest(name = "Validates password: {0}")
    @CsvSource(
        value = [
            "k0leff",
            "123456",
            "bojk0",
            "sliv3n",
            "parola",
            "a",
            "ab",
            "abc"
        ]
    )
    fun `password validation test with invalid password`(password: String) = runTest {
        val exception: Exception? = try {
            credentialsValidator.validatePassword(password)

            //Successfully validated
            null
        } catch (e: IllegalArgumentException) {
            e
        }

        assertThrows(IllegalArgumentException::class.java) {
            throw exception ?: return@assertThrows
        }
    }

    @ParameterizedTest(name = "Validates password: {0}")
    @CsvSource(
        value = [
            "password",
            "mkoleff",
            "sliv3nT0pa",
            "spasKoleff",
            "valioIspaneca",
            "1234567"
        ]
    )
    fun `password validation test with valid password`(password: String) = runTest {
        assertDoesNotThrow {
            credentialsValidator.validatePassword(password)
        }
    }

    @ParameterizedTest(name = "Validates credentials: {0} and save to DB: {1}")
    @MethodSource("provideValidCredentials")
    fun `credentials validation test with valid credentials`(
        credentials: Credentials,
        saveToDB: Boolean
    ) =
        runTest {

            //Save entry to db before test...
            if (saveToDB) {
                userDao.saveUser(
                    credentials.toEntity()
                )
            }

            assertDoesNotThrow {
                when (val result = credentialsValidator.validateRegister(credentials)) {
                    is ResultWrapper.Success -> {
                        assertTrue(result.data.isSuccessful)
                    }

                    else -> {

                        //Failed test
                        assert(false)
                    }
                }
            }
        }

    @ParameterizedTest(name = "Validates credentials: {0} and save to DB: {1}")
    @MethodSource("provideInvalidCredentials")
    fun `credentials validation test with invalid credentials`(
        credentials: Credentials,
        saveToDB: Boolean
    ) = runTest {

        //Save entry to db before test...
        if (saveToDB) {
            userDao.saveUser(
                credentials.toEntity()
            )
        }

        when (val result = credentialsValidator.validateRegister(credentials)) {
            is ResultWrapper.ApiError -> {
                assertTrue(result.error == KareError.INVALID_CREDENTIALS)
            }

            else -> {

                //Failed test
                assert(false)
            }
        }
    }

    @Test
    fun `login using LoginUseCase with valid credentials`() = runTest {
        val credentials = Credentials(
            UUID.randomUUID(),
            "mkoleff",
            "password",
            "koleff@kare.com"
        )

        //Save entry to DB
        userDao.saveUser(
            credentials.toEntity()
        )

        val loginState = authenticationUseCases.loginUseCase.invoke(
            credentials.username,
            credentials.password
        ).toList()

        logger.i(TAG, "Login -> isLoading state raised.")
        assertTrue { loginState[0].isLoading }

        logger.i(TAG, "Login -> isSuccessful state raised.")
        assertTrue { loginState[1].isSuccessful }

        val accessToken = loginState[1].data.accessToken
        val refreshToken = loginState[1].data.refreshToken
        logger.i(TAG, "Assert access and refresh token are generated.")
        logger.i(TAG, "Data: ${loginState[1]}")
        assertTrue {
            accessToken == "access_token" && refreshToken == "refresh_token"
        }
    }

    @Test
    fun `login using LoginUseCase with invalid credentials`() = runTest {
        val credentials = Credentials(
            UUID.randomUUID(),
            "koleff",
            "parola",
            "koleff@karecom"
        )

        //Save entry to DB
        userDao.saveUser(
            credentials.toEntity()
        )

        val loginState = authenticationUseCases.loginUseCase.invoke(
            credentials.username,
            credentials.password
        ).first()

        logger.i(TAG, "Data: $loginState")
        assertTrue { loginState.isError && loginState.error == KareError.INVALID_CREDENTIALS }
    }


    @Test
    fun `register using RegisterUseCase with valid credentials`() = runTest {
        val credentials = Credentials(
            UUID.randomUUID(),
            "mkoleff",
            "password",
            "koleff@kare.com"
        )
        logger.i(TAG, "Credentials: $credentials")

        val registerState = authenticationUseCases.registerUseCase.invoke(
            credentials
        ).toList()

        logger.i(TAG, "Register -> isLoading state raised.")
        assertTrue { registerState[0].isLoading }

        logger.i(TAG, "Register -> isSuccessful state raised.")
        assertTrue { registerState[1].isSuccessful }

        logger.i(TAG, "Data: ${registerState[1]}")

        logger.i(TAG, "Assert DB contains user.")
        val dbEntry = userDao.getUserByUsername(credentials.username)
        logger.i(TAG, "DB entry: $dbEntry")

        assertTrue { dbEntry == credentials.toEntity() }
    }

    @Test
    fun `register using RegisterUseCase with invalid credentials`() = runTest {
        val credentials = Credentials(
            UUID.randomUUID(),
            "koleff",
            "parola",
            "koleff@karecom"
        )

        val registerState = authenticationUseCases.registerUseCase.invoke(
            credentials
        ).first()

        logger.i(TAG, "Data: $registerState")
        assertTrue { registerState.isError && registerState.error == KareError.INVALID_CREDENTIALS }
    }
}