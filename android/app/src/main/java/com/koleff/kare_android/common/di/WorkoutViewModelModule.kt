package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseRemoteDataSource
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.datasource.WorkoutRemoteDataSource
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.remote.WorkoutApi
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.ui.view_model.ExerciseViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object WorkoutViewModelModule {
    @Provides
    @Singleton
    fun provideWorkoutDataSource(workoutApi: WorkoutApi): WorkoutDataSource {
        return WorkoutRemoteDataSource(workoutApi) //Can swap for local data source...
    }

    @Provides
    @Singleton
    //    @ViewModelScoped
    fun provideWorkoutRepository(workoutDataSource: WorkoutDataSource): WorkoutRepository {
        return WorkoutRepositoryImpl(workoutDataSource)
    }

//    @Provides
//    fun providesWorkoutViewModel(workoutRepository: WorkoutRepository): WorkoutViewModel {
//        return WorkoutViewModel(workoutRepository)
//    }
}