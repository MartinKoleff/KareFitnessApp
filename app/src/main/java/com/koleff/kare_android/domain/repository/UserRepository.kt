package com.koleff.kare_android.domain.repository

import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserByEmail(email: String): Flow<ResultWrapper<UserWrapper>>

    suspend fun getUserByUsername(username: String): Flow<ResultWrapper<UserWrapper>>
}