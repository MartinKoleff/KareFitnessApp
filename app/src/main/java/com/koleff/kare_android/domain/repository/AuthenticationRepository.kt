package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.domain.wrapper.DashboardWrapper
import com.koleff.kare_android.domain.wrapper.LoginWrapper
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {

    suspend fun login(username: String, password: String): Flow<ResultWrapper<LoginWrapper>>

    suspend fun register(user: UserDto): Flow<ResultWrapper<ServerResponseData>>

    suspend fun logout(user: UserDto): Flow<ResultWrapper<ServerResponseData>>
}