package com.koleff.kare_android.authentication.data

import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.auth.CredentialsDataStore

class CredentialsDataStoreFake: CredentialsDataStore {
    override fun getCredentials(): Credentials? {
        return null
    }

    override fun saveCredentials(credentials: Credentials) {

    }
}