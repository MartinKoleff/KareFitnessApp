package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class DuplicateExercisesResponse(
    @Json(name = "contains_duplicates")
    val containsDuplicates: Boolean
) : BaseResponse()