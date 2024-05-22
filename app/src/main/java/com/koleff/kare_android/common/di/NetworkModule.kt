package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.broadcast.RegenerateTokenHandler
import com.koleff.kare_android.common.broadcast.RegenerateTokenNotifier
import com.koleff.kare_android.common.network.NetworkManager
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
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

    //TODO: removed due to circular dependency...
    @Provides
    @Singleton
    fun provideNetworkManager(
//        broadcastManager: LocalBroadcastManager,
//        regenerateTokenNotifier: RegenerateTokenNotifier
    ): NetworkManager {
        return NetworkManager(
//            broadcastManager = broadcastManager,
//            regenerateTokenNotifier = regenerateTokenNotifier
        )
    }

    @Provides
    @Singleton
    fun provideRegenerateTokenHandler(
        authenticationUseCases: AuthenticationUseCases,
        preferences: Preferences
    ): RegenerateTokenHandler {
        return RegenerateTokenHandler(
            authenticationUseCases = authenticationUseCases,
            preferences = preferences
        )
    }
}