package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.LanguagesResponse

class KareLanguagesWrapper(
    private val response: LanguagesResponse
) : ServerResponseData(response) {
    val languages = response.languages
}