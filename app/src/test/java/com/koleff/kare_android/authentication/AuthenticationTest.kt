package com.koleff.kare_android.authentication

import com.koleff.kare_android.authentication.data.UserDaoFake
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.credentials_validator.CredentialsValidator
import com.koleff.kare_android.common.credentials_validator.CredentialsValidatorImpl
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.RegisterUseCase
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class AuthenticationTest {

    private lateinit var credentialsAuthenticator: CredentialsAuthenticator
    private lateinit var credentialsValidator: CredentialsValidator
    private lateinit var authenticationUseCases: AuthenticationUseCases
    private lateinit var loginUseCase: LoginUseCase
    private lateinit var registerUseCase: RegisterUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var authenticationDataSource: AuthenticationDataSource
    private lateinit var userRepository: UserRepository
    private lateinit var userDataSource: UserDataSource
    private lateinit var userDao: UserDaoFake
    @BeforeEach
    fun setup(){
        userDao = UserDaoFake()
        userDataSource = UserLocalDataSource(userDao)

        credentialsValidator = CredentialsValidatorImpl(userRepository)
        credentialsAuthenticator = CredentialsAuthenticatorImpl(credentialsValidator, preferences)

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
    }

    @ParameterizedTest(name = "Validates email {0}")
    @CsvSource(
        value = [
            "e@gmail.com",
            "@gmail.com",
            "test@gmail",
            "testgmail.com",
            "testgmail.com",
            "@gmail.com",
            "",
            "test@gmail.com.com",
        ]
    )
    fun `email validation test`(email: String) {

    }

    //TODO: username validation test...
    //TODO: password validation test...
    //TODO: credentials validation test...
    //TODO: login use case test
}