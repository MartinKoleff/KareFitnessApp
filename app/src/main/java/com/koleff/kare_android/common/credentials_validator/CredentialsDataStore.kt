package com.koleff.kare_android.common.credentials_validator

interface CredentialsDataStore {

    fun getCredentials(): Credentials?
    fun saveCredentials(credentials: Credentials)
}