package com.koleff.kare_android.do_workout.data

import com.koleff.kare_android.common.timer.DoWorkoutTimer
import com.koleff.kare_android.data.model.dto.ExerciseTime
import kotlinx.coroutines.*

class TimerUtilFake : DoWorkoutTimer {
    private var job: Job? = null
    private var timeLeft = 0
    private val scope = CoroutineScope(Dispatchers.IO) //No Dispatchers.Main in testing...

    override suspend fun startTimer(totalSeconds: Int, updateTime: (ExerciseTime) -> Unit) {
        timeLeft = totalSeconds
        job = scope.launch {
            while (isActive && timeLeft > 0) {
                updateTime(ExerciseTime.fromSeconds(timeLeft))
                timeLeft--
                delay(1000) //Simulates the delay to advance time without really waiting
            }
            if (isActive) {
                //Timer finished, update time with zeros.
                updateTime(ExerciseTime(0, 0, 0))
            }
        }
    }


    override fun pauseTimer() {
        job?.cancel()
    }

    override suspend fun resumeTimer(updateTime: (ExerciseTime) -> Unit) {

        //Only resume if there is time left and the job is not active.
        if (timeLeft > 0 && (job?.isActive != true)) {
            startTimer(timeLeft, updateTime)
        }
    }

    override fun resetTimer() {
        job?.cancel()
        timeLeft = 0
    }
}