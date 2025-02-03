package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.GetLanguagesResponse

class KareLanguagesWrapper(
    private val response: GetLanguagesResponse
) : ServerResponseData(response) {
    val languages = response.languages
}