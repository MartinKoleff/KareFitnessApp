package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.Flow


interface CredentialsAuthenticator {
    
    suspend fun checkCredentials(credentials: Credentials): Flow<BaseState>

    suspend fun saveCredentials(credentials: Credentials)
}