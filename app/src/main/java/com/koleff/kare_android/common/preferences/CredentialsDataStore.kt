package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.common.auth.Credentials

interface CredentialsDataStore {

    fun getCredentials(): Credentials?

    fun saveCredentials(credentials: Credentials)

    fun deleteCredentials()
}