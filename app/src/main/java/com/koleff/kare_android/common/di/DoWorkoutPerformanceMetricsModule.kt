package com.koleff.kare_android.common.di

import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsLocalDataSource
import com.koleff.kare_android.data.repository.DoWorkoutPerformanceMetricsRepositoryImpl
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.usecases.DeleteDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutPerformanceMetricsUseCases
import com.koleff.kare_android.domain.usecases.GetAllDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.SaveAllDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.UpdateDoWorkoutPerformanceMetricsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DoWorkoutPerformanceMetricsModule {

    /**
     * API
     */

//    @Provides
//    @Singleton
//    fun provideDoWorkoutPerformanceMetricsApi(okHttpClient: OkHttpClient, moshi: Moshi): UserApi {
//        return Retrofit.Builder()
//            .baseUrl(Constants.BASE_URL_FULL)
//            .client(okHttpClient)
//            .addCallAdapterFactory(CoroutineCallAdapterFactory())
//            .addConverterFactory(MoshiConverterFactory.create(moshi))
////            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//            .create(DoWorkoutPerformanceMetricsApi::class.java)
//    }

    /**
     * Data source
     */

    @Provides
    @Singleton
    fun provideDoWorkoutPerformanceMetricsDataSource(
        doWorkoutPerformanceMetricsDao: DoWorkoutPerformanceMetricsDao,
        doWorkoutExerciseSetDao: DoWorkoutExerciseSetDao
    ): DoWorkoutPerformanceMetricsDataSource {
        return DoWorkoutPerformanceMetricsLocalDataSource(
            doWorkoutPerformanceMetricsDao = doWorkoutPerformanceMetricsDao,
            doWorkoutExerciseSetDao = doWorkoutExerciseSetDao
        ) //Local for now...
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideDoWorkoutPerformanceMetricsRepository(doWorkoutPerformanceMetricsDataSource: DoWorkoutPerformanceMetricsDataSource)
            : DoWorkoutPerformanceMetricsRepository {
        return DoWorkoutPerformanceMetricsRepositoryImpl(doWorkoutPerformanceMetricsDataSource)
    }

    /**
     * Use cases
     */

    @Provides
    @Singleton
    fun provideDoWorkoutPerformanceMetricsUseCases(
        doWorkoutPerformanceMetricsRepository: DoWorkoutPerformanceMetricsRepository
    ): DoWorkoutPerformanceMetricsUseCases {
        return DoWorkoutPerformanceMetricsUseCases(
            deleteDoWorkoutPerformanceMetricsUseCase = DeleteDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            updateDoWorkoutPerformanceMetricsUseCase = UpdateDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            getAllDoWorkoutPerformanceMetricsUseCase = GetAllDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            getDoWorkoutPerformanceMetricsUseCase = GetDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            saveAllDoWorkoutExerciseSetUseCase = SaveAllDoWorkoutExerciseSetUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            saveDoWorkoutExerciseSetUseCase = SaveDoWorkoutExerciseSetUseCase(
                doWorkoutPerformanceMetricsRepository
            ),
            saveDoWorkoutPerformanceMetricsUseCase = SaveDoWorkoutPerformanceMetricsUseCase(
                doWorkoutPerformanceMetricsRepository
            )
        )
    }
}