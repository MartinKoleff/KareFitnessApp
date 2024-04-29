package com.koleff.kare_android.data.model.response.base_response

import com.koleff.kare_android.R

enum class KareError(
    val errorCode: String,
    val errorMessageResourceId: Int,
    var errorType: ErrorType,
    var extraMessage: String = ""
) {

    OK("success", R.string.text_success, ErrorType.SUCCESS),
    GENERIC("error_generic", R.string.text_internal_exception, ErrorType.INTERNAL),
    NETWORK("error_network", R.string.text_network_exception, ErrorType.SERVER),
    INVALID_CREDENTIALS(
        "error_invalid_credentials",
        R.string.text_invalid_credentials,
        ErrorType.INTERNAL
    ),
    USER_NOT_FOUND("error_user_not_found", R.string.text_user_not_found, ErrorType.INTERNAL),
    INVALID_EXERCISE("error_invalid_exercise", R.string.text_invalid_exercise, ErrorType.INTERNAL),
    INVALID_WORKOUT("error_invalid_workout", R.string.text_invalid_workout, ErrorType.INTERNAL),
    WORKOUT_HAS_NO_EXERCISES("error_workout_has_no_exercises", R.string.text_workout_has_no_exercises, ErrorType.INTERNAL),
    EXERCISE_NOT_FOUND("error_exercise_not_found", R.string.text_exercise_not_found, ErrorType.INTERNAL),
    EXERCISE_SET_NOT_FOUND("error_exercise_set_not_found", R.string.text_exercise_set_not_found, ErrorType.INTERNAL),
    WORKOUT_NOT_FOUND("error_workout_not_found", R.string.text_workout_not_found, ErrorType.INTERNAL),
    DO_WORKOUT_PERFORMANCE_METRICS_NOT_FOUND("error_do_workout_performance_metrics_not_found", R.string.text_do_workout_performance_metrics_not_found, ErrorType.INTERNAL),
    WORKOUT_CONFIGURATION_NOT_FOUND("error_workout_configuration_not_found", R.string.text_workout_configuration_not_found, ErrorType.INTERNAL);


    companion object {


        fun fromErrorCode(errorCode: String?): KareError =
            values().find { it.errorCode == errorCode } ?: GENERIC

    }
}

enum class ErrorType {
    SERVER, INTERNAL, LOGIN, SUCCESS, ACCESS_DENIED
}
