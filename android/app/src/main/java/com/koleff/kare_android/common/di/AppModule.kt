package com.koleff.kare_android.common.di

import androidx.multidex.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.datasource.DashboardDataSource
import com.koleff.kare_android.data.datasource.DashboardMockupDataSource
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseRemoteDataSource
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.datasource.WorkoutRemoteDataSource
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.remote.WorkoutApi
import com.koleff.kare_android.data.repository.DashboardRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.domain.repository.DashboardRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()

                val newUrl = original.url.newBuilder()
                    .scheme(Constants.SCHEME)
                    .host(Constants.BASE_URL_FULL)
//                    .port(Constants.PORT) //if port is needed
//                    .addQueryParameter("access_key", Constants.API_KEY) //if API key is needed
                    .build()

                val request = original.newBuilder()
                    .method(original.method, original.body)
                    .url(newUrl)
                    .build()

                chain.proceed(request)
            })

        //Logging
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        if (BuildConfig.DEBUG) okHttpClientBuilder.addInterceptor(logging)

        return okHttpClientBuilder.build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideExerciseApi(okHttpClient: OkHttpClient, moshi: Moshi): ExerciseApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi)) //TODO: test with backend to decide which converter...
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ExerciseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWorkoutApi(okHttpClient: OkHttpClient, moshi: Moshi): WorkoutApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi)) //TODO: test with backend to decide which converter...
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WorkoutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExerciseDataSource(exerciseApi: ExerciseApi): ExerciseDataSource {
        return ExerciseRemoteDataSource(exerciseApi) //Can swap for local data source...
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDataSource: ExerciseDataSource): ExerciseRepository {
        return ExerciseRepositoryImpl(exerciseDataSource)
    }

    @Provides
    @Singleton
    fun provideWorkoutDataSource(workoutApi: WorkoutApi, dispatcher: CoroutineDispatcher): WorkoutDataSource {
        return WorkoutRemoteDataSource(workoutApi, dispatcher) //Can swap for local data source...
    }

    @Provides
    @Singleton
    fun provideWorkoutRepository(workoutDataSource: WorkoutDataSource): WorkoutRepository {
        return WorkoutRepositoryImpl(workoutDataSource)
    }

    @Provides
    @Singleton
    fun provideDashboardDataSource(): DashboardDataSource {
        return DashboardMockupDataSource()
    }

    @Provides
    @Singleton
    fun provideDashboardRepository(dashboardDataSource: DashboardDataSource): DashboardRepository {
        return DashboardRepositoryImpl(dashboardDataSource)
    }
}