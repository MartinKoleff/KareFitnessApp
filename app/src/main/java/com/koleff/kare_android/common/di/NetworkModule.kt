package com.koleff.kare_android.common.di

import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.koleff.kare_android.common.broadcast.handler.RegenerateTokenHandler
import com.koleff.kare_android.common.broadcast.handler.RegenerateTokenNotifier
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.common.network.ApiCallWrapper
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.common.preferences.TokenDataStore
import com.koleff.kare_android.common.preferences.TokenDataStoreImpl
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkBindModule {

    @Binds
    @Singleton
    abstract fun bindRegenerateTokenNotifier(
        impl: RegenerateTokenHandler
    ): RegenerateTokenNotifier
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Requiring authorization
     */
    @Provides
    @Singleton
    fun provideApiAuthorizationCallWrapper(
        broadcastManager: LocalBroadcastManager,
        regenerateTokenNotifier: RegenerateTokenNotifier
    ): ApiAuthorizationCallWrapper {
        return ApiAuthorizationCallWrapper(
            broadcastManager = broadcastManager,
            regenerateTokenNotifier = regenerateTokenNotifier
        )
    }

    @Provides
    @Singleton
    fun provideRegenerateTokenHandler(
        authenticationUseCases: Provider<AuthenticationUseCases>,
        tokenDataStore: TokenDataStore
    ): RegenerateTokenHandler {
        return RegenerateTokenHandler(
            authenticationUseCases = authenticationUseCases,
            tokenDataStore = tokenDataStore
        )
    }

    @Provides
    @Singleton
    fun provideTokenDataStore(
        preferences: Preferences
    ): TokenDataStore {
        return TokenDataStoreImpl(
            preferences = preferences
        )
    }

    /**
     * Not requiring authorization
     */

    @Provides
    @Singleton
    fun provideApiCallWrapper(): ApiCallWrapper {
        return ApiCallWrapper()
    }
}