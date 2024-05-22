package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.broadcast.RegenerateTokenBroadcastReceiver
import com.koleff.kare_android.common.network.RegenerateTokenNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NetworkModule {

    @Binds
    @Singleton
    abstract fun bindRegenerateTokenNotifier(
        impl: RegenerateTokenBroadcastReceiver
    ): RegenerateTokenNotifier
}