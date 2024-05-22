package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.network.ApiCallWrapper
import com.koleff.kare_android.data.model.request.FetchUserByEmail
import com.koleff.kare_android.data.model.request.FetchUserByUsername
import com.koleff.kare_android.data.remote.UserApi
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor(
    private val userApi: UserApi,
    private val apiCallWrapper: ApiCallWrapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : UserDataSource {
    override suspend fun getUserByEmail(email: String): Flow<ResultWrapper<UserWrapper>> {
        val body = FetchUserByEmail(email)

        return apiCallWrapper.executeApiCall(
            dispatcher,
            { UserWrapper(userApi.getUserByEmail(body)) }
        )
    }

    override suspend fun getUserByUsername(username: String): Flow<ResultWrapper<UserWrapper>> {
        val body = FetchUserByUsername(username)

        return apiCallWrapper.executeApiCall(
            dispatcher,
            { UserWrapper(userApi.getUserByUsername(body)) }
        )
    }
}
