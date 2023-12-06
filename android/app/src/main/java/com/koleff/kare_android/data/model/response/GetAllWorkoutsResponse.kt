package com.koleff.kare_android.data.model.response

import com.koleff.kare_android.data.model.dto.WorkoutDto
import com.koleff.kare_android.data.model.response.base_response.BaseResponse
import com.squareup.moshi.Json

data class GetAllWorkoutsResponse(
//    @Json(name = "pagination")
//    val paginationData: PaginationDataDto,
    @Json(name = "data")
    val workouts: List<WorkoutDto>
) : BaseResponse()