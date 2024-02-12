package com.koleff.kare_android.common.di

import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationControllerImpl
import com.koleff.kare_android.common.navigation.NavigationNotifier
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NavigationModule {

    @Binds
    @Singleton
    abstract fun bindNavigationController(impl: NavigationControllerImpl): NavigationController

    @Binds
    @Singleton
    abstract fun bindNavigationNotifier(impl: NavigationControllerImpl): NavigationNotifier
}