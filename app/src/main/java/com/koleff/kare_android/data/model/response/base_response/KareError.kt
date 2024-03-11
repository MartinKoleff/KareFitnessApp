package com.koleff.kare_android.data.model.response.base_response

import com.koleff.kare_android.R

enum class KareError(
    val errorCode: String,
    val errorMessageResourceId: Int,
    var errorType: ErrorType,
    var extraMessage: String = ""
) {

    OK("success", R.string.text_success, ErrorType.SUCCESS),
    GENERIC("error_generic", R.string.text_internal_exception, ErrorType.INTERNAL),
    NETWORK("error_network", R.string.text_network_exception, ErrorType.SERVER),
    INVALID_CREDENTIALS(
        "error_invalid_credentials",
        R.string.text_invalid_credentials,
        ErrorType.INTERNAL
    ),
    USER_NOT_FOUND("error_user_not_found", R.string.text_user_not_found, ErrorType.INTERNAL);


    companion object {
        fun fromErrorCode(errorCode: String?): KareError =
            values().find { it.errorCode == errorCode } ?: GENERIC

    }
}

enum class ErrorType {
    SERVER, INTERNAL, LOGIN, SUCCESS, ACCESS_DENIED
}
