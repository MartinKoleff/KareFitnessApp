package com.koleff.kare_android.data.model.response.base_response

import com.koleff.kare_android.R

enum class KareError(
    val errorCode: String,
    val errorMessageResourceId: Int,
    var errorType: ErrorType) {

    OK("success", R.string.text_success, ErrorType.SUCCESS),
    GENERIC("error_generic", R.string.text_internal_exception, ErrorType.INTERNAL);

    companion object {
        fun fromErrorCode(errorCode: String?): KareError = values().find { it.errorCode == errorCode } ?: GENERIC
    }
}

enum class ErrorType {
    SERVER, INTERNAL, LOGIN, SUCCESS, ACCESS_DENIED
}
