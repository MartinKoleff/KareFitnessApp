package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.common.auth.Credentials
import javax.inject.Inject

class CredentialsDataStoreImpl @Inject constructor(
    private val preferences: Preferences
): CredentialsDataStore {
    override fun getCredentials(): Credentials? {
        return preferences.getCredentials()
    }

    override fun saveCredentials(credentials: Credentials) {
        preferences.saveCredentials(credentials)
    }

    override fun deleteCredentials() {
        preferences.deleteCredentials()
    }
}