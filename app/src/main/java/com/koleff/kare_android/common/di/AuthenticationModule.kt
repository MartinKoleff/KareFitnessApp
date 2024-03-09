package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.Constants.useLocalDataSource
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.credentials_validator.CredentialsDataStore
import com.koleff.kare_android.common.credentials_validator.CredentialsDataStoreImpl
import com.koleff.kare_android.common.credentials_validator.CredentialsValidator
import com.koleff.kare_android.common.credentials_validator.CredentialsValidatorImpl
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.AuthenticationRemoteDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.RegisterUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    @Provides
    @Singleton
    fun provideCredentialsValidator(userRepository: UserRepository): CredentialsValidator {
        return CredentialsValidatorImpl(userRepository)
    }

    @Provides
    @Singleton
    fun provideCredentialsDataStore(preferences: Preferences): CredentialsDataStore {
        return CredentialsDataStoreImpl(preferences)
    }

    @Provides
    @Singleton
    fun provideCredentialsAuthenticator(
        credentialsValidator: CredentialsValidator,
        credentialsDataStore: CredentialsDataStore
    ): CredentialsAuthenticator {
        return CredentialsAuthenticatorImpl(credentialsValidator, credentialsDataStore)
    }

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(
        authenticationApi: AuthenticationApi,
        userDao: UserDao,
        credentialsAuthenticator: CredentialsAuthenticator
    ): AuthenticationDataSource {
        return if (useLocalDataSource) AuthenticationLocalDataSource(
            userDao = userDao,
            credentialsAuthenticator = credentialsAuthenticator
        ) else AuthenticationRemoteDataSource(authenticationApi) //TODO: add credentials authenticator check before calling server?
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(authenticationDataSource: AuthenticationDataSource): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authenticationDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(
        authenticationRepository: AuthenticationRepository,
        credentialsAuthenticator: CredentialsAuthenticator
    ): AuthenticationUseCases {
        return AuthenticationUseCases(
            loginUseCase = LoginUseCase(authenticationRepository, credentialsAuthenticator),
            registerUseCase = RegisterUseCase(authenticationRepository, credentialsAuthenticator)
        )
    }
}