package com.koleff.kare_android.data.datasource

import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.flow.Flow

interface UserDataSource {

    suspend fun getUserByEmail(email: String): Flow<ResultWrapper<UserWrapper>>

    suspend fun getUserByUsername(username: String): Flow<ResultWrapper<UserWrapper>>
}