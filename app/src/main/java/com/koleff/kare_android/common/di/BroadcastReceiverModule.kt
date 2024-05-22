package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.broadcast.LogoutBroadcastReceiver
import com.koleff.kare_android.common.broadcast.RegenerateTokenBroadcastReceiver
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object BroadcastReceiverModule {

    @Provides
    @Singleton
    fun provideRegenerateTokenBroadcastReceiver(
        authenticationUseCases: AuthenticationUseCases,
        preferences: Preferences
    ): RegenerateTokenBroadcastReceiver {
        return RegenerateTokenBroadcastReceiver(
            authenticationUseCases = authenticationUseCases,
            preferences = preferences
        )
    }


    @Provides
    @Singleton
    fun provideLogoutBroadcastReceiver(
        authenticationUseCases: AuthenticationUseCases,
        preferences: Preferences,
        navigationController: NavigationController
    ): LogoutBroadcastReceiver {
        return LogoutBroadcastReceiver(
            authenticationUseCases = authenticationUseCases,
            preferences = preferences,
            navigationController = navigationController
        )
    }
}