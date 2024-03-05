package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import javax.inject.Inject

class CredentialsAuthenticatorImpl @Inject constructor(
    private val credentialsValidator: CredentialsValidator,
    private val preferences: Preferences
) : CredentialsAuthenticator {

    //TODO: add state and observe in view model...

    override suspend fun checkCredentials(credentials: Credentials) {
        when (credentialsValidator.validate(credentials)){
            is ResultWrapper.ApiError -> {

            }

            is ResultWrapper.Loading -> {
                //TODO: convert to flow...
            }

            is ResultWrapper.Success -> {

                //save credentials and login...
                saveCredentials(credentials)
            }
        }
    }

    override suspend fun saveCredentials(credentials: Credentials) {
        preferences.saveCredentials(credentials)
    }
}