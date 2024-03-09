package com.koleff.kare_android.data.repository

import com.koleff.kare_android.data.datasource.UserDataSource
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.UserWrapper
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun getUserByEmail(email: String): Flow<ResultWrapper<UserWrapper>> {
        return userDataSource.getUserByEmail(email)
    }

    override suspend fun getUserByUsername(username: String): Flow<ResultWrapper<UserWrapper>> {
        return userDataSource.getUserByUsername(username)

    }
}