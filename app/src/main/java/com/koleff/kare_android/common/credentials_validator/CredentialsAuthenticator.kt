package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow


interface CredentialsAuthenticator {
    
    suspend fun checkLoginCredentials(username: String, password: String): Flow<BaseState>
    suspend fun checkRegisterCredentials(credentials: Credentials): Flow<BaseState>

    suspend fun saveCredentials(credentials: Credentials)
}