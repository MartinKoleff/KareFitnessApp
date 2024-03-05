package com.koleff.kare_android.common.credentials_validator


interface CredentialsAuthenticator {
    
    suspend fun checkCredentials(credentials: Credentials)

    suspend fun saveCredentials(credentials: Credentials)
}