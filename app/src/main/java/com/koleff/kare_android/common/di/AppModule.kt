package com.koleff.kare_android.common.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.multidex.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.Constants.useLocalDataSource
import com.koleff.kare_android.common.Constants.useMockupDataSource
import com.koleff.kare_android.common.preferences.DefaultPreferences
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.DashboardDataSource
import com.koleff.kare_android.data.datasource.DashboardMockupDataSource
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseLocalDataSource
import com.koleff.kare_android.data.datasource.ExerciseMockupDataSource
import com.koleff.kare_android.data.datasource.ExerciseRemoteDataSource
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSource
import com.koleff.kare_android.data.datasource.WorkoutRemoteDataSource
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.remote.WorkoutApi
import com.koleff.kare_android.data.repository.DashboardRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.database.KareDatabase
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import com.koleff.kare_android.domain.repository.DashboardRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.CreateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
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
//                    .addHeader()
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
    fun provideKareDatabase(@ApplicationContext appContext: Context): KareDatabase {
        return KareDatabase.buildDatabase(appContext)
    }

    @Provides
    @Singleton
    fun provideExerciseDao(kareDatabase: KareDatabase): ExerciseDao {
        return kareDatabase.exerciseDao
    }

    @Provides
    @Singleton
    fun provideExerciseDetailsDao(kareDatabase: KareDatabase): ExerciseDetailsDao {
        return kareDatabase.exerciseDetailsDao
    }

    @Provides
    @Singleton
    fun provideExerciseDBManager(
        preferences: Preferences,
        exerciseDao: ExerciseDao,
        exerciseDetailsDao: ExerciseDetailsDao,
        exerciseSetDao: ExerciseSetDao
    ): ExerciseDBManager {
        return ExerciseDBManager(preferences, exerciseDao, exerciseDetailsDao, exerciseSetDao)
    }

    @Provides
    @Singleton
    fun provideWorkoutDao(kareDatabase: KareDatabase): WorkoutDao {
        return kareDatabase.workoutDao
    }

    @Provides
    @Singleton
    fun provideWorkoutDetailsDao(kareDatabase: KareDatabase): WorkoutDetailsDao {
        return kareDatabase.workoutDetailsDao
    }

    @Provides
    @Singleton
    fun provideExerciseSetDao(kareDatabase: KareDatabase): ExerciseSetDao {
        return kareDatabase.exerciseSetDao
    }

    @Provides
    @Singleton
    fun provideWorkoutDBManager(
        preferences: Preferences,
        workoutDao: WorkoutDao,
        workoutDetailsDao: WorkoutDetailsDao
    ): WorkoutDBManager {
        return WorkoutDBManager(preferences, workoutDao, workoutDetailsDao)
    }

    @Provides
    @Singleton
    fun provideExerciseDataSource(
        exerciseApi: ExerciseApi,
        exerciseDao: ExerciseDao,
        exerciseDetailsDao: ExerciseDetailsDao,
        exerciseDBManager: ExerciseDBManager
    ): ExerciseDataSource {
        return if (useLocalDataSource) ExerciseLocalDataSource(
            exerciseDao = exerciseDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseDBManager = exerciseDBManager
        )
        else if (useMockupDataSource) ExerciseMockupDataSource()
        else ExerciseRemoteDataSource(exerciseApi)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDataSource: ExerciseDataSource): ExerciseRepository {
        return ExerciseRepositoryImpl(exerciseDataSource)
    }

    @Provides
    @Singleton
    fun provideWorkoutDataSource(
        workoutApi: WorkoutApi,
        workoutDao: WorkoutDao,
        exerciseDao: ExerciseDao,
        workoutDetailsDao: WorkoutDetailsDao,
        exerciseSetDao: ExerciseSetDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): WorkoutDataSource {
        return if (useLocalDataSource) WorkoutLocalDataSource(
            workoutDao = workoutDao,
            exerciseDao = exerciseDao,
            workoutDetailsDao = workoutDetailsDao,
            exerciseSetDao = exerciseSetDao
        )
//        else if (useMockupDataSource) WorkoutMockupDataSource()
        else WorkoutRemoteDataSource(workoutApi, dispatcher)
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

    @Provides
    @Singleton
    fun provideWorkoutUseCases(workoutRepository: WorkoutRepository): WorkoutUseCases {
        return WorkoutUseCases(
            getWorkoutDetailsUseCase = GetWorkoutsDetailsUseCase(workoutRepository),
            getWorkoutsUseCase = GetWorkoutsUseCase(workoutRepository),
            getWorkoutUseCase = GetWorkoutUseCase(workoutRepository),
            updateWorkoutUseCase = UpdateWorkoutUseCase(workoutRepository),
            onSearchWorkoutUseCase = OnSearchWorkoutUseCase(),
            deleteExerciseUseCase = DeleteExerciseUseCase(workoutRepository),
            deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
            selectWorkoutUseCase = SelectWorkoutUseCase(workoutRepository),
            getSelectedWorkoutUseCase = GetSelectedWorkoutUseCase(workoutRepository),
            createWorkoutUseCase = CreateWorkoutUseCase(workoutRepository)
        )
    }

    @Provides
    @Singleton
    fun provideExerciseUseCases(exerciseRepository: ExerciseRepository): ExerciseUseCases {
        return ExerciseUseCases(
           onSearchExerciseUseCase = OnSearchExerciseUseCase(),
            onFilterExercisesUseCase = OnFilterExercisesUseCase(),
            getExerciseDetailsUseCase = GetExerciseDetailsUseCase(exerciseRepository),
            getExercisesUseCase = GetExercisesUseCase(exerciseRepository),
            getExerciseUseCase = GetExerciseUseCase(exerciseRepository)
        )
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(sharedPreferences: SharedPreferences): Preferences {
        return DefaultPreferences(sharedPreferences)
    }
}