package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.common.auth.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.auth.CredentialsDataStore
import com.koleff.kare_android.common.auth.CredentialsDataStoreImpl
import com.koleff.kare_android.common.auth.CredentialsValidator
import com.koleff.kare_android.common.auth.CredentialsValidatorImpl
import com.koleff.kare_android.common.network.NetworkManager
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationLocalDataSource
import com.koleff.kare_android.data.datasource.AuthenticationRemoteDataSource
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.repository.AuthenticationRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.domain.usecases.LoginUseCase
import com.koleff.kare_android.domain.usecases.LogoutUseCase
import com.koleff.kare_android.domain.usecases.RegenerateTokenUseCase
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
        credentialsAuthenticator: CredentialsAuthenticator,
        networkManager: NetworkManager
    ): AuthenticationDataSource {
        val useRemoteAPI = false //Temporary testing authentication with remote API and other functionalities with local impl.

        return if (!useRemoteAPI) AuthenticationLocalDataSource(
            userDao = userDao,
            credentialsAuthenticator = credentialsAuthenticator
        ) else AuthenticationRemoteDataSource(
            authenticationApi = authenticationApi,
            networkManager = networkManager
        )
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
            registerUseCase = RegisterUseCase(authenticationRepository, credentialsAuthenticator),
            logoutUseCase = LogoutUseCase(authenticationRepository),
            regenerateTokenUseCase = RegenerateTokenUseCase(authenticationRepository)
        )
    }
}