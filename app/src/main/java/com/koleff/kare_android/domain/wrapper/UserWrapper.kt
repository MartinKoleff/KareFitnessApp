package com.koleff.kare_android.domain.wrapper

import com.koleff.kare_android.data.model.response.ExerciseResponse
import com.koleff.kare_android.data.model.response.UserResponse

class UserWrapper(userResponse: UserResponse) :
    ServerResponseData(userResponse) {
    val user = userResponse.user
}