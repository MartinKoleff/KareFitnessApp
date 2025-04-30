package com.koleff.kare_android.data.model.request

import com.koleff.kare_android.data.model.dto.KareLanguage
import com.squareup.moshi.Json

data class ChangeLanguageRequest(
    @field:Json(name = "language")
    val language: KareLanguage
)