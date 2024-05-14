package com.koleff.kare_android.common.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.multidex.BuildConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.Constants.useLocalDataSource
import com.koleff.kare_android.common.network.UUIDJsonAdapter
import com.koleff.kare_android.common.preferences.DefaultPreferences
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.datasource.DashboardDataSource
import com.koleff.kare_android.data.datasource.DashboardMockupDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutLocalDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsDataSource
import com.koleff.kare_android.data.datasource.DoWorkoutPerformanceMetricsLocalDataSource
import com.koleff.kare_android.data.datasource.ExerciseDataSource
import com.koleff.kare_android.data.datasource.ExerciseLocalDataSourceV2
import com.koleff.kare_android.data.datasource.ExerciseRemoteDataSource
import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.data.datasource.UserLocalDataSource
import com.koleff.kare_android.data.datasource.UserRemoteDataSource
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSourceV2
import com.koleff.kare_android.data.datasource.WorkoutRemoteDataSource
import com.koleff.kare_android.data.remote.AuthenticationApi
import com.koleff.kare_android.data.remote.ExerciseApi
import com.koleff.kare_android.data.remote.UserApi
import com.koleff.kare_android.data.remote.WorkoutApi
import com.koleff.kare_android.data.repository.DashboardRepositoryImpl
import com.koleff.kare_android.data.repository.DoWorkoutPerformanceMetricsRepositoryImpl
import com.koleff.kare_android.data.repository.DoWorkoutRepositoryImpl
import com.koleff.kare_android.data.repository.ExerciseRepositoryImpl
import com.koleff.kare_android.data.repository.UserRepositoryImpl
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.dao.DoWorkoutExerciseSetDao
import com.koleff.kare_android.data.room.dao.DoWorkoutPerformanceMetricsDao
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseDetailsDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.data.room.database.KareDatabase
import com.koleff.kare_android.data.room.manager.ExerciseDBManagerV2
import com.koleff.kare_android.data.room.manager.UserDBManager
import com.koleff.kare_android.data.room.manager.WorkoutDBManagerV2
import com.koleff.kare_android.domain.repository.DashboardRepository
import com.koleff.kare_android.domain.repository.DoWorkoutPerformanceMetricsRepository
import com.koleff.kare_android.domain.repository.DoWorkoutRepository
import com.koleff.kare_android.domain.repository.ExerciseRepository
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.AddMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.AddNewExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DeselectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutInitialSetupUseCase
import com.koleff.kare_android.domain.usecases.DoWorkoutPerformanceMetricsUseCases
import com.koleff.kare_android.domain.usecases.DoWorkoutUseCases
import com.koleff.kare_android.domain.usecases.ExerciseUseCases
import com.koleff.kare_android.domain.usecases.GetAllDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetCatalogExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetSelectedWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.GetExerciseUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.OnFilterExercisesUseCase
import com.koleff.kare_android.domain.usecases.OnSearchExerciseUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.ResetTimerUseCase
import com.koleff.kare_android.domain.usecases.SaveAllDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutExerciseSetUseCase
import com.koleff.kare_android.domain.usecases.SaveDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.SelectWorkoutUseCase
import com.koleff.kare_android.domain.usecases.StartTimerUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.UpdateDoWorkoutPerformanceMetricsUseCase
import com.koleff.kare_android.domain.usecases.UpdateExerciseSetsAfterTimerUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
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

    /**
     * Retrofit/OKHttp configurations
     */

    @Provides
    @Singleton
    fun provideOkHttpClient(preferences: Preferences): OkHttpClient {
        val accessToken = preferences.getTokens()?.accessToken ?: ""

        val okHttpClientBuilder = OkHttpClient.Builder()
            .addInterceptor(Interceptor { chain ->
                val original = chain.request()

                val newUrl = original.url.newBuilder()
                    .scheme(Constants.SCHEME)
                    .host(Constants.BASE_URL)
//                    .port(Constants.PORT) //if port is needed
//                    .addQueryParameter("access_key", Constants.API_KEY) //if API key is needed
                    .build()

                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)
                    .url(newUrl)

                val unauthorizedRequestPaths = listOf("/login", "/register")
                val requestPath = original.url.encodedPath

                //Add token authorization for all requests except auth ones
                if (!requestPath.endsWith(unauthorizedRequestPaths[0]) && !requestPath.endsWith(
                        unauthorizedRequestPaths[1]
                    )
                ) {
                    requestBuilder.addHeader("Authorization", "Bearer $accessToken")
                }

                val request = requestBuilder.build()
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
            .add(UUIDJsonAdapter())
            .build()
    }

    /**
     * APIs
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

    @Provides
    @Singleton
    fun provideWorkoutApi(okHttpClient: OkHttpClient, moshi: Moshi): WorkoutApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WorkoutApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthenticationApi(okHttpClient: OkHttpClient, moshi: Moshi): AuthenticationApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL_FULL)
            .client(okHttpClient)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
//            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthenticationApi::class.java)
    }

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
     * Room DB
     */

    @Provides
    @Singleton
    fun provideKareDatabase(@ApplicationContext appContext: Context): KareDatabase {
        return KareDatabase.buildDatabase(appContext)
    }


    /**
     * DAOs
     */

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
    fun provideUserDao(kareDatabase: KareDatabase): UserDao {
        return kareDatabase.userDao
    }

    @Provides
    @Singleton
    fun provideDoWorkoutPerformanceMetricsDao(kareDatabase: KareDatabase): DoWorkoutPerformanceMetricsDao {
        return kareDatabase.doWorkoutPerformanceMetricsDao
    }

    @Provides
    @Singleton
    fun provideDoWorkoutExerciseSetDao(kareDatabase: KareDatabase): DoWorkoutExerciseSetDao {
        return kareDatabase.doWorkoutExerciseSetDao
    }

    @Provides
    @Singleton
    fun provideWorkoutConfigurationDao(kareDatabase: KareDatabase): WorkoutConfigurationDao {
        return kareDatabase.workoutConfigurationDao
    }

    /**
     * DB Managers
     */

    @Provides
    @Singleton
    fun provideWorkoutDBManager(
        preferences: Preferences,
        workoutDao: WorkoutDao,
        workoutDetailsDao: WorkoutDetailsDao,
        workoutConfigurationDao: WorkoutConfigurationDao,
        exerciseDao: ExerciseDao,
        exerciseSetDao: ExerciseSetDao
    ): WorkoutDBManagerV2 {
        val hasInitializedDB = preferences.hasInitializedWorkoutTable()

        return WorkoutDBManagerV2(
            workoutDao = workoutDao,
            workoutDetailsDao = workoutDetailsDao,
            workoutConfigurationDao = workoutConfigurationDao,
            exerciseDao = exerciseDao,
            exerciseSetDao = exerciseSetDao,
            hasInitializedDB = hasInitializedDB
        )
    }

    @Provides
    @Singleton
    fun provideExerciseDBManager(
        preferences: Preferences,
        exerciseDao: ExerciseDao,
        exerciseDetailsDao: ExerciseDetailsDao,
        exerciseSetDao: ExerciseSetDao,
        workoutDao: WorkoutDao,
        workoutDetailsDao: WorkoutDetailsDao
    ): ExerciseDBManagerV2 {
        val hasInitializedDB = preferences.hasInitializedExerciseTable()

        return ExerciseDBManagerV2(
            exerciseDao = exerciseDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseSetDao = exerciseSetDao,
            workoutDao = workoutDao,
            workoutDetailsDao = workoutDetailsDao,
            hasInitializedDB = hasInitializedDB
        )
    }

    @Provides
    @Singleton
    fun provideUserDBManager(
        preferences: Preferences,
        userDao: UserDao
    ): UserDBManager {
        val hasInitializedDB = preferences.hasInitializedUserTable()

        return UserDBManager(
            userDao = userDao,
            hasInitializedDB = hasInitializedDB
        )
    }

    /**
     * Data sources
     */

    @Provides
    @Singleton
    fun provideExerciseDataSource(
        exerciseApi: ExerciseApi,
        exerciseDao: ExerciseDao,
        exerciseDetailsDao: ExerciseDetailsDao,
        exerciseSetDao: ExerciseSetDao
    ): ExerciseDataSource {
        return if (useLocalDataSource) ExerciseLocalDataSourceV2(
            exerciseDao = exerciseDao,
            exerciseDetailsDao = exerciseDetailsDao,
            exerciseSetDao = exerciseSetDao
        )
        else ExerciseRemoteDataSource(exerciseApi)
    }


    @Provides
    @Singleton
    fun provideWorkoutDataSource(
        workoutApi: WorkoutApi,
        workoutDao: WorkoutDao,
        exerciseDao: ExerciseDao,
        workoutDetailsDao: WorkoutDetailsDao,
        exerciseSetDao: ExerciseSetDao,
        workoutConfigurationDao: WorkoutConfigurationDao,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): WorkoutDataSource {
        return if (useLocalDataSource) WorkoutLocalDataSourceV2(
            workoutDao = workoutDao,
            exerciseDao = exerciseDao,
            workoutDetailsDao = workoutDetailsDao,
            exerciseSetDao = exerciseSetDao,
            workoutConfigurationDao = workoutConfigurationDao

        )
        else WorkoutRemoteDataSource(workoutApi, dispatcher)
    }

    @Provides
    @Singleton
    fun provideDashboardDataSource(): DashboardDataSource {
        return DashboardMockupDataSource()
    }

    @Provides
    @Singleton
    fun provideUserDataSource(
        userDao: UserDao,
        userApi: UserApi,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): UserDataSource {
        return if (useLocalDataSource) UserLocalDataSource(
            userDao = userDao
        )
        else UserRemoteDataSource(userApi, dispatcher)
    }

    @Provides
    @Singleton
    fun provideDoWorkoutDataSource(): DoWorkoutDataSource {
        return DoWorkoutLocalDataSource() //Local for now...
    }

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
     * Repositories
     */

    @Provides
    @Singleton
    fun provideWorkoutRepository(workoutDataSource: WorkoutDataSource): WorkoutRepository {
        return WorkoutRepositoryImpl(workoutDataSource)
    }

    @Provides
    @Singleton
    fun provideDashboardRepository(dashboardDataSource: DashboardDataSource): DashboardRepository {
        return DashboardRepositoryImpl(dashboardDataSource)
    }

    @Provides
    @Singleton
    fun provideExerciseRepository(exerciseDataSource: ExerciseDataSource): ExerciseRepository {
        return ExerciseRepositoryImpl(exerciseDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDataSource: UserDataSource): UserRepository {
        return UserRepositoryImpl(userDataSource)
    }

    @Provides
    @Singleton
    fun provideDoWorkoutRepository(doWorkoutDataSource: DoWorkoutDataSource): DoWorkoutRepository {
        return DoWorkoutRepositoryImpl(doWorkoutDataSource)
    }

    @Provides
    @Singleton
    fun provideDoWorkoutPerformanceMetricsRepository(doWorkoutPerformanceMetricsDataSource: DoWorkoutPerformanceMetricsDataSource)
            : DoWorkoutPerformanceMetricsRepository {
        return DoWorkoutPerformanceMetricsRepositoryImpl(doWorkoutPerformanceMetricsDataSource)
    }

    /**
     * Use Cases
     */

    @Provides
    @Singleton
    fun provideWorkoutUseCases(workoutRepository: WorkoutRepository): WorkoutUseCases {
        return WorkoutUseCases(
            getWorkoutDetailsUseCase = GetWorkoutsDetailsUseCase(workoutRepository),
            getAllWorkoutsUseCase = GetAllWorkoutsUseCase(workoutRepository),
            getAllWorkoutDetailsUseCase = GetAllWorkoutDetailsUseCase(workoutRepository),
            getWorkoutUseCase = GetWorkoutUseCase(workoutRepository),
            updateWorkoutUseCase = UpdateWorkoutUseCase(workoutRepository),
            updateWorkoutDetailsUseCase = UpdateWorkoutDetailsUseCase(workoutRepository),
            onSearchWorkoutUseCase = OnSearchWorkoutUseCase(),
            deleteExerciseUseCase = DeleteExerciseUseCase(workoutRepository),
            deleteMultipleExercisesUseCase = DeleteMultipleExercisesUseCase(workoutRepository),
            addExerciseUseCase = AddExerciseUseCase(workoutRepository),
            addMultipleExercisesUseCase = AddMultipleExercisesUseCase(workoutRepository),
            submitExerciseUseCase = SubmitExerciseUseCase(workoutRepository),
            deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
            selectWorkoutUseCase = SelectWorkoutUseCase(workoutRepository),
            deselectWorkoutUseCase = DeselectWorkoutUseCase(workoutRepository),
            getSelectedWorkoutUseCase = GetSelectedWorkoutUseCase(workoutRepository),
            createNewWorkoutUseCase = CreateNewWorkoutUseCase(workoutRepository),
            createCustomWorkoutUseCase = CreateCustomWorkoutUseCase(workoutRepository),
            createCustomWorkoutDetailsUseCase = CreateCustomWorkoutDetailsUseCase(workoutRepository),
            getWorkoutConfigurationUseCase = GetWorkoutConfigurationUseCase(workoutRepository),
            createWorkoutConfigurationUseCase = CreateWorkoutConfigurationUseCase(workoutRepository),
            updateWorkoutConfigurationUseCase = UpdateWorkoutConfigurationUseCase(workoutRepository),
            deleteWorkoutConfigurationUseCase = DeleteWorkoutConfigurationUseCase(workoutRepository)
        )
    }

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


    @Provides
    @Singleton
    fun provideDoWorkoutPerformanceMetricsUseCases(
        doWorkoutPerformanceMetricsRepository: DoWorkoutPerformanceMetricsRepository
    ): DoWorkoutPerformanceMetricsUseCases {
        return DoWorkoutPerformanceMetricsUseCases(
            deleteDoWorkoutPerformanceMetricsUseCase = DeleteDoWorkoutPerformanceMetricsUseCase(doWorkoutPerformanceMetricsRepository),
            updateDoWorkoutPerformanceMetricsUseCase = UpdateDoWorkoutPerformanceMetricsUseCase(doWorkoutPerformanceMetricsRepository),
            getAllDoWorkoutPerformanceMetricsUseCase = GetAllDoWorkoutPerformanceMetricsUseCase(doWorkoutPerformanceMetricsRepository),
            getDoWorkoutPerformanceMetricsUseCase = GetDoWorkoutPerformanceMetricsUseCase(doWorkoutPerformanceMetricsRepository),
            saveAllDoWorkoutExerciseSetUseCase = SaveAllDoWorkoutExerciseSetUseCase(doWorkoutPerformanceMetricsRepository),
            saveDoWorkoutExerciseSetUseCase = SaveDoWorkoutExerciseSetUseCase(doWorkoutPerformanceMetricsRepository),
            saveDoWorkoutPerformanceMetricsUseCase = SaveDoWorkoutPerformanceMetricsUseCase(doWorkoutPerformanceMetricsRepository)
        )
    }

    /**
     * Shared preferences
     */
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