package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseRemoteDataSource
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ExerciseViewModelModule {
    @Provides
    @Singleton
    fun provideExerciseDataSource(exerciseApi: ExerciseApi): ExerciseDataSource {
        return ExerciseRemoteDataSource(exerciseApi) //Can swap for local data source...
    }

    @Provides
    @Singleton
    //    @ViewModelScoped
    fun provideExerciseRepository(exerciseDataSource: ExerciseDataSource): ExerciseRepository {
        return ExerciseRepositoryImpl(exerciseDataSource)
    }

    @Provides
    fun providesExerciseViewModel(exerciseRepository: ExerciseRepository): ExerciseViewModel {
        return ExerciseViewModel(exerciseRepository)
    }
}