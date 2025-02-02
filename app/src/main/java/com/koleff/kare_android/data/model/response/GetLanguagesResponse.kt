package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.KareLanguage
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetLanguagesResponse (
    @Json(name = "data")
    val languages: List<KareLanguage>
): BaseResponse()