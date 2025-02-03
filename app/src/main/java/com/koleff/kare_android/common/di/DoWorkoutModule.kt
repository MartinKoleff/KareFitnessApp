package com.koleff.kare_android.common.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.datasource.DoWorkoutDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutLocalDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutRemoteDataSource
import com.koleff.kare_android.data.remote.DoWorkoutApi
import com.koleff.kare_android.data.repository.DoWorkoutRepositoryImpl
import com.koleff.kare_android.domain.repository.DoWorkoutRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutInitialSetupUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutUseCases
import com.koleff.kare_android.domain.usecases.ResetTimerUseCase
import com.koleff.kare_android.domain.usecases.PauseTimerUseCase
import com.koleff.kare_android.domain.usecases.ResumeTimerUseCase
import com.koleff.kare_android.domain.usecases.StartTimerUseCase
import com.koleff.kare_android.domain.usecases.UpdateExerciseSetsAfterTimerUseCase
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
object DoWorkoutModule {

    /**
     * API
     */

    @Provides
    @Singleton
    fun provideDoWorkoutApi(okHttpClient: OkHttpClient, moshi: Moshi): DoWorkoutApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DoWorkoutApi::class.java)
    }

    /**
     * Data source
     */
    @Provides
    @Singleton
    fun provideDoWorkoutDataSource(
        doWorkoutApi: DoWorkoutApi,
        apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
    ): DoWorkoutDataSource {
        return if (Constants.useLocalDataSource) DoWorkoutLocalDataSource()
        else DoWorkoutRemoteDataSource(
            doWorkoutApi = doWorkoutApi,
            apiAuthorizationCallWrapper = apiAuthorizationCallWrapper
        )
    }


    /**
     * Repository
     */
    @Provides
    @Singleton
    fun provideDoWorkoutRepository(doWorkoutDataSource: DoWorkoutDataSource): DoWorkoutRepository {
        return DoWorkoutRepositoryImpl(doWorkoutDataSource)
    }

    /**
     * Use Cases
     */

    @Provides
    @Singleton
    fun provideDoWorkoutUseCases(
        doWorkoutRepository: DoWorkoutRepository,
        exerciseRepository: ExerciseRepository
    ): DoWorkoutUseCases {
        return DoWorkoutUseCases(
            doWorkoutInitialSetupUseCase = DoWorkoutInitialSetupUseCase(doWorkoutRepository),
            updateExerciseSetsAfterTimerUseCase = UpdateExerciseSetsAfterTimerUseCase(
                doWorkoutRepository
            ),
            addNewExerciseSetUseCase = AddNewExerciseSetUseCase(exerciseRepository),
            deleteExerciseSetUseCase = DeleteExerciseSetUseCase(exerciseRepository),
            startTimerUseCase = StartTimerUseCase(),
            resetTimerUseCase = ResetTimerUseCase(),
            pauseTimerUseCase = PauseTimerUseCase(),
            resumeTimerUseCase = ResumeTimerUseCase()
        )
    }
}