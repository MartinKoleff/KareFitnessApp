package com.koleff.kare_android.domain.usecases

import com.koleff.kare_android.common.timer.DoWorkoutTimer
import com.koleff.kare_android.common.timer.TimerUtil
import com.koleff.kare_android.data.model.dto.ExerciseTime
import com.koleff.kare_android.data.model.response.TimerResponse
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.wrapper.TimerWrapper
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.receiveAsFlow

class ResumeTimerUseCase() {
    operator fun invoke(
        timer: DoWorkoutTimer,
        time: ExerciseTime
    ): Flow<ResultWrapper<TimerWrapper>> = flow {
        val channel =
            Channel<ResultWrapper<TimerWrapper>>(Channel.CONFLATED)

        timer.resumeTimer() { timeLeft ->
            val result = TimerWrapper(
                TimerResponse(
                    time = timeLeft
                )
            )
            channel.trySend(ResultWrapper.Success(result))
        }

        //Receive and send time from timer...
        channel.receiveAsFlow().collect { timeUpdate ->
            emit(timeUpdate)
        }

//        channel.close()
    }
}
