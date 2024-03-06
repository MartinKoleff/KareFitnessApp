package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticator
import com.koleff.kare_android.common.credentials_validator.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.credentials_validator.CredentialsValidator
import com.koleff.kare_android.common.credentials_validator.CredentialsValidatorImpl
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.AuthenticationDataSource
import com.koleff.kare_android.data.datasource.AuthenticationRemoteDataSource
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.data.repository.AuthenticationRepositoryImpl
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
    fun provideCredentialsAuthenticator(credentialsValidator: CredentialsValidator, preferences: Preferences): CredentialsAuthenticator {
        return CredentialsAuthenticatorImpl(credentialsValidator, preferences)
    }

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(authenticationApi: AuthenticationApi): AuthenticationDataSource { //TODO: add local datasource for testing...
        return AuthenticationRemoteDataSource(authenticationApi)
    }

    @Provides
    @Singleton
    fun provideAuthenticationRepository(authenticationDataSource: AuthenticationDataSource): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authenticationDataSource)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCases(authenticationRepository: AuthenticationRepository): AuthenticationUseCases {
        return AuthenticationUseCases(
            loginUseCase = LoginUseCase(authenticationRepository),
            registerUseCase = RegisterUseCase(authenticationRepository)
        )
    }
}