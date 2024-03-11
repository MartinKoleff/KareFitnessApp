package com.koleff.kare_android.common.auth

interface CredentialsDataStore {

    fun getCredentials(): Credentials?
    fun saveCredentials(credentials: Credentials)
}