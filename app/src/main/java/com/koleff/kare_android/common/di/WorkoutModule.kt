package com.koleff.kare_android.common.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.network.ApiAuthorizationCallWrapper
import com.koleff.kare_android.data.datasource.WorkoutDataSource
import com.koleff.kare_android.data.datasource.WorkoutLocalDataSourceV2
import com.koleff.kare_android.data.datasource.WorkoutRemoteDataSource
import com.koleff.kare_android.data.remote.WorkoutApi
import com.koleff.kare_android.data.repository.WorkoutRepositoryImpl
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.dao.ExerciseSetDao
import com.koleff.kare_android.data.room.dao.WorkoutConfigurationDao
import com.koleff.kare_android.data.room.dao.WorkoutDao
import com.koleff.kare_android.data.room.dao.WorkoutDetailsDao
import com.koleff.kare_android.domain.repository.WorkoutRepository
import com.koleff.kare_android.domain.usecases.AddExerciseUseCase
import com.koleff.kare_android.domain.usecases.AddMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.CreateCustomWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateNewWorkoutUseCase
import com.koleff.kare_android.domain.usecases.CreateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteExerciseUseCase
import com.koleff.kare_android.domain.usecases.DeleteMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.DeleteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.FavoriteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.FindDuplicateExercisesUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.GetAllWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetFavoriteWorkoutsUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutUseCase
import com.koleff.kare_android.domain.usecases.GetWorkoutsDetailsUseCase
import com.koleff.kare_android.domain.usecases.OnSearchWorkoutUseCase
import com.koleff.kare_android.domain.usecases.SubmitExerciseUseCase
import com.koleff.kare_android.domain.usecases.SubmitMultipleExercisesUseCase
import com.koleff.kare_android.domain.usecases.UnfavoriteWorkoutUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutConfigurationUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutDetailsUseCase
import com.koleff.kare_android.domain.usecases.UpdateWorkoutUseCase
import com.koleff.kare_android.domain.usecases.WorkoutUseCases
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
object WorkoutModule {

    /**
     * API
     */

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

    /**
     * Data sources
     */

    @Provides
    @Singleton
    fun provideWorkoutDataSource(
        workoutApi: WorkoutApi,
        workoutDao: WorkoutDao,
        exerciseDao: ExerciseDao,
        workoutDetailsDao: WorkoutDetailsDao,
        exerciseSetDao: ExerciseSetDao,
        workoutConfigurationDao: WorkoutConfigurationDao,
        apiAuthorizationCallWrapper: ApiAuthorizationCallWrapper,
        @IoDispatcher dispatcher: CoroutineDispatcher
    ): WorkoutDataSource {
        return if (Constants.useLocalDataSource) WorkoutLocalDataSourceV2(
            workoutDao = workoutDao,
            exerciseDao = exerciseDao,
            workoutDetailsDao = workoutDetailsDao,
            exerciseSetDao = exerciseSetDao,
            workoutConfigurationDao = workoutConfigurationDao

        )
        else WorkoutRemoteDataSource(
            workoutApi = workoutApi,
            apiAuthorizationCallWrapper = apiAuthorizationCallWrapper,
            dispatcher = dispatcher
        )
    }

    /**
     * Repository
     */

    @Provides
    @Singleton
    fun provideWorkoutRepository(workoutDataSource: WorkoutDataSource): WorkoutRepository {
        return WorkoutRepositoryImpl(workoutDataSource)
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
            submitMultipleExercisesUseCase = SubmitMultipleExercisesUseCase(workoutRepository),
            findDuplicateExercisesUseCase = FindDuplicateExercisesUseCase(workoutRepository),
            deleteWorkoutUseCase = DeleteWorkoutUseCase(workoutRepository),
            favoriteWorkoutUseCase = FavoriteWorkoutUseCase(workoutRepository),
            unfavoriteWorkoutUseCase = UnfavoriteWorkoutUseCase(workoutRepository),
            getFavoriteWorkoutsUseCase = GetFavoriteWorkoutsUseCase(workoutRepository),
            createNewWorkoutUseCase = CreateNewWorkoutUseCase(workoutRepository),
            createCustomWorkoutUseCase = CreateCustomWorkoutUseCase(workoutRepository),
            createCustomWorkoutDetailsUseCase = CreateCustomWorkoutDetailsUseCase(workoutRepository),
            getWorkoutConfigurationUseCase = GetWorkoutConfigurationUseCase(workoutRepository),
            createWorkoutConfigurationUseCase = CreateWorkoutConfigurationUseCase(workoutRepository),
            updateWorkoutConfigurationUseCase = UpdateWorkoutConfigurationUseCase(workoutRepository),
            deleteWorkoutConfigurationUseCase = DeleteWorkoutConfigurationUseCase(workoutRepository)
        )
    }
}