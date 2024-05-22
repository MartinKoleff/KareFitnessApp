package com.koleff.kare_android.authentication.data

import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.preferences.CredentialsDataStore

class CredentialsDataStoreFake : CredentialsDataStore {
    private var credentials: Credentials? = null
    var isCleared = false
        private set

    override fun getCredentials(): Credentials? {
        return credentials
    }

    override fun saveCredentials(credentials: Credentials) {
        this.credentials = credentials
        isCleared = false
    }

    override fun deleteCredentials() {
        credentials = null
        isCleared = true
    }
}