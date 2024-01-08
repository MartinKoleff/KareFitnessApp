package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.base_response.KareError

sealed class ResultWrapper<out T>{
    class Success<out T>(val data: T) : ResultWrapper<T>()
    class ApiError<out T>(
        val error: KareError? = null,
        val value: T? = null
    ) : ResultWrapper<T>()
    class Loading<T> : ResultWrapper<T>()
}