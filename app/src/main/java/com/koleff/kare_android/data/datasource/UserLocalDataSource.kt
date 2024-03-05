package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.R
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.model.dto.MuscleGroup
import com.koleff.kare_android.data.model.response.DashboardMuscleGroupsResponse
import com.koleff.kare_android.data.model.response.UserResponse
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.data.room.dao.UserDao
import com.koleff.kare_android.domain.wrapper.DashboardWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(
    private val userDao: UserDao
) : UserDataSource {
    override suspend fun getUserByEmail(email: String): Flow<ResultWrapper<UserWrapper>> =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data = userDao.getUserByEmail(email)

                val result = UserWrapper(
                    UserResponse(data)
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.USER_NOT_FOUND
                    )
                )
            }
        }

    override suspend fun getUserByUsername(username: String): Flow<ResultWrapper<UserWrapper>>  =
        flow {
            emit(ResultWrapper.Loading())
            delay(Constants.fakeDelay)

            try {
                val data = userDao.getUserByUsername(username)

                val result = UserWrapper(
                    UserResponse(data)
                )

                emit(ResultWrapper.Success(result))
            } catch (e: NoSuchElementException) {
                emit(
                    ResultWrapper.ApiError(
                        error = KareError.USER_NOT_FOUND
                    )
                )
            }
        }
}
