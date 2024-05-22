package com.koleff.kare_android.common.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.ApiCallWrapper
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseLocalDataSourceV2
import com.koleff.kare_android.data.datasource.ExerciseRemoteDataSource
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetCatalogExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
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
object ExerciseModule {

    /**
     * API
     */

    @Provides
    @Singleton
    fun provideExerciseApi(okHttpClient: OkHttpClient, moshi: Moshi): ExerciseApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseApi::class.java)
    }

    /**
     * Data source
     */

    @Provides
    @Singleton
    fun provideExerciseDataSource(
        exerciseApi: ExerciseApi,
        exerciseDao: ExerciseDao,
        exerciseDetailsDao: ExerciseDetailsDao,
        exerciseSetDao: ExerciseSetDao,
        apiCallWrapper: ApiCallWrapper
    ): ExerciseDataSource {
        return if (Constants.useLocalDataSource) ExerciseLocalDataSourceV2(
            exerciseDao = exerciseDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseSetDao = exerciseSetDao
        )
        else ExerciseRemoteDataSource(
            exerciseApi = exerciseApi,
            apiCallWrapper = apiCallWrapper
        )
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDataSource: ExerciseDataSource): ExerciseRepository {
        return ExerciseRepositoryImpl(exerciseDataSource)
    }

    /**
     * Use cases
     */

    @Provides
    @Singleton
    fun provideExerciseUseCases(exerciseRepository: ExerciseRepository): ExerciseUseCases {
        return ExerciseUseCases(
            onSearchExerciseUseCase = OnSearchExerciseUseCase(),
            onFilterExercisesUseCase = OnFilterExercisesUseCase(),
            getExerciseDetailsUseCase = GetExerciseDetailsUseCase(exerciseRepository),
            getCatalogExercisesUseCase = GetCatalogExercisesUseCase(exerciseRepository),
            getCatalogExerciseUseCase = GetCatalogExerciseUseCase(exerciseRepository),
            deleteExerciseSetUseCase = DeleteExerciseSetUseCase(exerciseRepository),
            addNewExerciseSetUseCase = AddNewExerciseSetUseCase(exerciseRepository),
            getExerciseUseCase = GetExerciseUseCase(exerciseRepository)
        )
    }
}