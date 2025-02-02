package com.koleff.kare_android.common.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.datasource.OnboardingDataSource
import com.koleff.kare_android.data.datasource.OnboardingLocalDataSource
import com.koleff.kare_android.data.datasource.OnboardingRemoteDataSource
import com.koleff.kare_android.data.remote.OnboardingApi
import com.koleff.kare_android.data.repository.OnboardingRepositoryImpl
import com.koleff.kare_android.data.room.dao.OnboardingDao
import com.koleff.kare_android.domain.repository.OnboardingRepository
import com.koleff.kare_android.domain.usecases.GetOnboardingUseCase
import com.koleff.kare_android.domain.usecases.OnboardingUseCases
import com.koleff.kare_android.domain.usecases.SaveOnboardingUseCase
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    /**
     * API
     */

    @Provides
    @Singleton
    fun provideOnboardingApi(okHttpClient: OkHttpClient, moshi: Moshi): OnboardingApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OnboardingApi::class.java)
    }

    /**
     * Data source
     */

    @Provides
    @Singleton
    fun provideOnboardingDataSource(
        onboardingApi: OnboardingApi,
        onboardingDao: OnboardingDao,
        apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): OnboardingDataSource {
        return if (Constants.useLocalDataSource) OnboardingLocalDataSource(
            onboardingDao = onboardingDao
        )
        else OnboardingRemoteDataSource(
            onboardingApi = onboardingApi,
            apiAuthorizationCallWrapper = apiAuthorizationCallWrapper,
            dispatcher = dispatcher
        )
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideOnboardingRepository(onboardingDataSource: OnboardingDataSource): OnboardingRepository {
        return OnboardingRepositoryImpl(onboardingDataSource)
    }

    /**
     * Use cases
     */

    @Provides
    @Singleton
    fun provideOnboardingUseCases(onboardingRepository: OnboardingRepository): OnboardingUseCases {
        return OnboardingUseCases(
            getOnboardingUseCase = GetOnboardingUseCase(onboardingRepository),
            saveOnboardingUseCase = SaveOnboardingUseCase(onboardingRepository)
        )
    }
}