package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.common.timer.DoWorkoutTimer
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.TimerResponse
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TimerWrapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResetTimerUseCase() {
    suspend operator fun invoke(timer: DoWorkoutTimer, defaultTime: ExerciseTime): Flow<ResultWrapper<TimerWrapper>> =
        flow {
            val resetTimerResult =
                TimerWrapper(
                    TimerResponse(
                        time = defaultTime
                    )
                )

            //Reset workout timer and state
            timer.resetTimer().also {
                emit(ResultWrapper.Success(resetTimerResult))
            }
        }
}