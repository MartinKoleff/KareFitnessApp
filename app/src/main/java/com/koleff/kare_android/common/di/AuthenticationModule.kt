package com.koleff.kare_android.common.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.auth.CredentialsAuthenticator
import com.koleff.kare_android.common.auth.CredentialsAuthenticatorImpl
import com.koleff.kare_android.common.auth.CredentialsValidator
import com.koleff.kare_android.common.auth.CredentialsValidatorImpl
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.common.network.ApiCallWrapper
import com.koleff.kare_android.common.preferences.CredentialsDataStore
import com.koleff.kare_android.common.preferences.CredentialsDataStoreImpl
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
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthenticationModule {

    /**
     * API
     */

    @Provides
    @Singleton
    fun provideAuthenticationApi(okHttpClient: OkHttpClient, moshi: Moshi): AuthenticationApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthenticationApi::class.java)
    }

    /**
     * Components
     */

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

    /**
     * Data source
     */

    @Provides
    @Singleton
    fun provideAuthenticationDataSource(
        authenticationApi: AuthenticationApi,
        userDao: UserDao,
        credentialsAuthenticator: CredentialsAuthenticator,
        apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
        apiCallWrapper: ApiCallWrapper
    ): AuthenticationDataSource {
        val useRemoteAPI =
            false //Temporary testing authentication with remote API and other functionalities with local impl.

        return if (!useRemoteAPI) AuthenticationLocalDataSource(
            userDao = userDao,
            credentialsAuthenticator = credentialsAuthenticator
        ) else AuthenticationRemoteDataSource(
            authenticationApi = authenticationApi,
            apiAuthorizationCallWrapper = apiAuthorizationCallWrapper,
            apiCallWrapper = apiCallWrapper
        )
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideAuthenticationRepository(authenticationDataSource: AuthenticationDataSource): AuthenticationRepository {
        return AuthenticationRepositoryImpl(authenticationDataSource)
    }

    /**
     * Use cases
     */

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