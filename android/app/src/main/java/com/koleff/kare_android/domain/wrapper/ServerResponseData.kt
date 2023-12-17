package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.koleff.kare_android.data.model.response.base_response.KareError

open class ServerResponseData(
    var isSuccessful: Boolean,
    val error: KareError = KareError.GENERIC,
) {
    constructor(response: BaseResponse?) : this(
        isSuccessful = response?.isSuccessful ?: false || response?.error == KareError.GENERIC,
        error = KareError.fromErrorCode(response?.error?.errorCode)
    )
}