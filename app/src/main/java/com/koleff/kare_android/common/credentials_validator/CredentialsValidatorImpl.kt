package com.koleff.kare_android.common.credentials_validator

import android.util.Patterns.EMAIL_ADDRESS
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.domain.repository.UserRepository
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import kotlinx.coroutines.flow.map
import javax.inject.Inject

typealias Credentials = UserDto

class CredentialsValidatorImpl @Inject constructor(
    private val userRepository: UserRepository
) : CredentialsValidator {
    override suspend fun validate(credentials: Credentials): ResultWrapper<ServerResponseData> { //TODO: convert to flow and add loading...
        return try {
            validateUsername(credentials.username)
            validateEmail(credentials.email)
            validatePassword(credentials.password)

            ResultWrapper.Success(
                ServerResponseData(
                    isSuccessful = true
                )
            )
        } catch (e: IllegalArgumentException) {
            ResultWrapper.ApiError(
                error = KareError.INVALID_CREDENTIALS
            )
        }
    }

    private fun validatePassword(password: String) {
        if (password.isEmpty()) {
            throw IllegalArgumentException("Password cannot be empty.")
        }

        if (password.length < 7) {
            throw IllegalArgumentException("Password should contain at least 7 characters.")
        }
    }

    private suspend fun validateEmail(email: String) {
        if (email.isEmpty()) {
            throw IllegalArgumentException("Email cannot be empty.")
        }

        //Validate email via regex
        if(!EMAIL_ADDRESS.matcher(email).matches()){
            throw IllegalArgumentException("Invalid email.")
        }

        //Check DB for existing entry
        userRepository.getUserByEmail(email).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    throw IllegalArgumentException("Email doesn't exist in DB.")
                }
                else -> {}
            }
        }
    }

    private suspend fun validateUsername(username: String) {
        if (username.isEmpty()) {
            throw IllegalArgumentException("Username cannot be empty.")
        }

        if (username.length < 4) {
            throw IllegalArgumentException("Username should contain at least 4 characters.")
        }

        //Check DB for existing entry
        userRepository.getUserByUsername(username).map { apiResult ->
            when (apiResult) {
                is ResultWrapper.ApiError -> {
                    throw IllegalArgumentException("User doesn't exist in DB.")
                }
                else -> {}
            }
        }
    }
}