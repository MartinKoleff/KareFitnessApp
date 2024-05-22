package com.koleff.kare_android.common.di

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.multidex.BuildConfig
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.NetworkManager
import com.koleff.kare_android.common.network.RegenerateTokenNotifier
import com.koleff.kare_android.common.network.UUIDJsonAdapter
import com.koleff.kare_android.common.preferences.DefaultPreferences
import com.koleff.kare_android.common.preferences.Preferences
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
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
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

    /**
     * Network
     */

    @Provides
    @Singleton
    fun provideNetworkManager(
        broadcastManager: LocalBroadcastManager,
        regenerateTokenNotifier: RegenerateTokenNotifier
    ): NetworkManager {
        return NetworkManager(
            broadcastManager = broadcastManager,
            regenerateTokenNotifier = regenerateTokenNotifier
        )
    }

    /**
     * Broadcast manager
     */

    @Provides
    @Singleton
    fun provideLocalBroadcastManager(@ApplicationContext context: Context): LocalBroadcastManager {
        return LocalBroadcastManager.getInstance(context)
    }
}