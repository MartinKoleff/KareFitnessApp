package com.koleff.kare_android.common.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.ApiCallWrapper
import com.koleff.kare_android.data.datasource.DashboardDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.datasource.UserRemoteDataSource
import com.koleff.kare_android.data.remote.UserApi
import com.koleff.kare_android.data.repository.DashboardRepositoryImpl
import com.koleff.kare_android.data.repository.UserRepositoryImpl
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.repository.DashboardRepository
import com.koleff.kare_android.domain.repository.UserRepository
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
object UserModule {

    /**
     * API
     */

    @Provides
    @Singleton
    fun provideUserApi(okHttpClient: OkHttpClient, moshi: Moshi): UserApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApi::class.java)
    }

    /**
     * Data sources
     */

    @Provides
    @Singleton
    fun provideUserDataSource(
        userDao: UserDao,
        userApi: UserApi,
        apiCallWrapper: ApiCallWrapper,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): UserDataSource {
        return if (Constants.useLocalDataSource) UserLocalDataSource(
            userDao = userDao
        )
        else UserRemoteDataSource(
            userApi = userApi,
            apiCallWrapper = apiCallWrapper,
            dispatcher = dispatcher
        )
    }

    /**
     * Repositories
     */

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }
}