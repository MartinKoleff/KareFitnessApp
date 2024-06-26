package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.broadcast.LogoutBroadcastReceiver
import com.koleff.kare_android.common.broadcast.handler.LogoutHandler
import com.koleff.kare_android.common.broadcast.RegenerateTokenBroadcastReceiver
import com.koleff.kare_android.common.broadcast.handler.RegenerateTokenHandler
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.preferences.CredentialsDataStore
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BroadcastReceiverModule {

    @Provides
    @Singleton
    fun provideRegenerateTokenBroadcastReceiver(
        regenerateTokenHandler: RegenerateTokenHandler
    ): RegenerateTokenBroadcastReceiver {
        return RegenerateTokenBroadcastReceiver(
            regenerateTokenHandler = regenerateTokenHandler
        )
    }

    @Provides
    @Singleton
    fun provideLogoutBroadcastReceiver(
        logoutHandler: LogoutHandler
    ): LogoutBroadcastReceiver {
       return LogoutBroadcastReceiver(logoutHandler)
    }

    @Provides
    @Singleton
    fun provideLogoutHandler(
        authenticationUseCases: Provider<AuthenticationUseCases>,
        credentialsDataStore: CredentialsDataStore,
        navigationController: NavigationController
    ): LogoutHandler {
        return LogoutHandler(
            authenticationUseCases = authenticationUseCases,
            credentialsDataStore = credentialsDataStore,
            navigationController = navigationController
        )
    }
}