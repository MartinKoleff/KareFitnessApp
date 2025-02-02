package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.common.timer.DoWorkoutTimer
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.TimerResponse
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TimerWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.koleff.kare_android.domain.wrapper.ServerResponseData
import com.koleff.kare_android.data.model.response.base_response.BaseResponse

class PauseTimerUseCase() {
    operator fun invoke(timer: DoWorkoutTimer): Flow<ResultWrapper<ServerResponseData>> =
        flow {
            //Pause workout timer
            timer.pauseTimer().also {
                emit(
                    ResultWrapper.Success(
                        ServerResponseData(
                            BaseResponse()
                        )
                    )
                )
            }
        }
}