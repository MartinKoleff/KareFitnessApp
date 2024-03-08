package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.common.preferences.Preferences

class CredentialsDataStoreImpl(
    private val preferences: Preferences
): CredentialsDataStore {
    override fun getCredentials(): Credentials? {
        return preferences.getCredentials()
    }

    override fun saveCredentials(credentials: Credentials) {
        preferences.saveCredentials(credentials)
    }
}