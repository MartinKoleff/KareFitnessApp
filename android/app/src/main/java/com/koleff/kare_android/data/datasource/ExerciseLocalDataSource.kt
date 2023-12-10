package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.ExerciseDetailsDto
import com.koleff.kare_android.data.model.dto.ExerciseDto
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.GetExerciseDetailsResponse
import com.koleff.kare_android.data.model.response.GetExercisesResponse
import com.koleff.kare_android.data.model.wrapper.GetExerciseDetailsWrapper
import com.koleff.kare_android.data.model.wrapper.GetExercisesWrapper
import com.koleff.kare_android.data.model.wrapper.ResultWrapper
import com.koleff.kare_android.data.room.dao.ExerciseDao
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExerciseLocalDataSource @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val exerciseDBManager: ExerciseDBManager
) : ExerciseDataSource {

    override suspend fun getExercises(muscleGroupId: Int): Flow<ResultWrapper<GetExercisesWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            //Check if Room DB has data
            if (!exerciseDBManager.hasInitializedRoomDB) {
                exerciseDBManager.initializeRoomDB(exerciseDao)

            }

            val data = exerciseDao.getExercisesOrderedById(
                MuscleGroup.fromId(muscleGroupId)
            )

            val result = GetExercisesWrapper(
                GetExercisesResponse(data)
            )

            emit(ResultWrapper.Success(result))
        }



    override suspend fun getExerciseDetails(exerciseId: Int): Flow<ResultWrapper<GetExerciseDetailsWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            val data = exerciseDao.getExerciseById(exerciseId)

            val result = GetExerciseDetailsWrapper( //TODO: wire with remote datasource...
                GetExerciseDetailsResponse(
                    ExerciseDetailsDto(
                        name = data.name,
                        description = "",
                        muscleGroup = data.muscleGroup,
                        machineType = data.machineType,
                        videoUrl = ""
                    )
                )
            )

            emit(ResultWrapper.Success(result))
        }
}