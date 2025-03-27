package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.LanguageDataSource
import com.koleff.kare_android.data.datasource.LanguageLocalDataSource
import com.koleff.kare_android.data.repository.LanguageRepositoryImpl
import com.koleff.kare_android.domain.repository.LanguageRepository
import com.koleff.kare_android.domain.usecases.ChangeLanguageUseCase
import com.koleff.kare_android.domain.usecases.GetSupportedLanguagesUseCase
import com.koleff.kare_android.domain.usecases.LanguageUseCases
import com.koleff.kare_android.domain.usecases.OnSearchLanguageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LanguagesModule {

    @Provides
    @Singleton
    fun provideLanguageRepository(languageDataSource: LanguageDataSource): LanguageRepository {
        return LanguageRepositoryImpl(languageDataSource)
    }

    @Provides
    @Singleton
    fun provideLanguageDataSource(): LanguageDataSource {
        return LanguageLocalDataSource() //Remote datasource not needed
    }

    @Provides
    @Singleton
    fun provideLanguageUseCases(languageRepository: LanguageRepository): LanguageUseCases {
        return LanguageUseCases(
            changeLanguageUseCase = ChangeLanguageUseCase(languageRepository),
            getSupportedLanguagesUseCase = GetSupportedLanguagesUseCase(languageRepository),
            onSearchLanguageUseCase = OnSearchLanguageUseCase()
        )
    }
}