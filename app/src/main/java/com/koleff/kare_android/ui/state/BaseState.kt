package com.koleff.kare_android.ui.state

import com.koleff.kare_android.data.model.response.base_response.KareError

open class BaseState (
    open val isSuccessful: Boolean = false,
    open val isLoading: Boolean = false,
    open val isError: Boolean = false,
    open val error : KareError = KareError.GENERIC
)