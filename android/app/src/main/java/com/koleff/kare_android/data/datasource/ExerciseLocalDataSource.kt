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
            if (!exerciseDBManager.hasInitializedExerciseTableRoomDB) {
                exerciseDBManager.initializeExerciseTableRoomDB(exerciseDao)
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
            val description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nunc interdum nibh nec pharetra iaculis. Aenean ultricies egestas leo at ultricies. Quisque suscipit, purus ut congue porta, eros eros tincidunt sem, sed commodo magna metus eu nibh. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Vestibulum quis velit eget eros malesuada luctus. Suspendisse iaculis ullamcorper condimentum. Sed metus augue, dapibus eu venenatis vitae, ornare non turpis. Donec suscipit iaculis dolor, id fermentum mauris interdum in. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas."
            val videoUrl = "dQw4w9WgXcQ" //https://www.youtube.com/watch?v=

            val result = GetExerciseDetailsWrapper(
                GetExerciseDetailsResponse(
                    ExerciseDetailsDto(
                        name = data.name,
                        description = description,
                        muscleGroup = data.muscleGroup,
                        machineType = data.machineType,
                        videoUrl = videoUrl
                    )
                )
            )

            emit(ResultWrapper.Success(result))
        }
}