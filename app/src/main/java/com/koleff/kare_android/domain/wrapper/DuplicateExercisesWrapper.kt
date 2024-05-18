package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.DashboardMuscleGroupsResponse
import com.koleff.kare_android.data.model.response.GetDuplicateExercisesResponse

class DuplicateExercisesWrapper(getDuplicateExercisesResponse: GetDuplicateExercisesResponse):
    ServerResponseData(getDuplicateExercisesResponse) {
    val containsDuplicates = getDuplicateExercisesResponse.containsDuplicates
}