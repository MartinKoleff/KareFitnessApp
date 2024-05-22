package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.DoWorkoutDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutLocalDataSource
import com.koleff.kare_android.data.repository.DoWorkoutRepositoryImpl
import com.koleff.kare_android.domain.repository.DoWorkoutRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutInitialSetupUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutUseCases
import com.koleff.kare_android.domain.usecases.ResetTimerUseCase
import com.koleff.kare_android.domain.usecases.StartTimerUseCase
import com.koleff.kare_android.domain.usecases.UpdateExerciseSetsAfterTimerUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DoWorkoutModule {

    /**
     * API
     */

//    @Provides
//    @Singleton
//    fun provideDoWorkoutApi(okHttpClient: OkHttpClient, moshi: Moshi): UserApi {
//        return Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL_FULL)
//            .client(okHttpClient)
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
////            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(DoWorkoutApi::class.java)
//    }

    /**
     * Data source
     */
    @Provides
    @Singleton
    fun provideDoWorkoutDataSource(): DoWorkoutDataSource {
        return DoWorkoutLocalDataSource() //Local for now...
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
            resetTimerUseCase = ResetTimerUseCase()
        )
    }
}