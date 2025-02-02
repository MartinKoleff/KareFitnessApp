package com.koleff.kare_android.common.timer

import com.koleff.kare_android.data.model.dto.ExerciseTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

open class TimerUtil(private var totalTime: Int = 0) : DoWorkoutTimer {
    private var job: Job? = null
    private val timerScope = CoroutineScope(Dispatchers.Main)
    private var isRunning: Boolean = false

    override suspend fun startTimer(totalSeconds: Int, updateTime: (ExerciseTime) -> Unit) {
        totalTime = totalSeconds

        job?.cancel()  //Cancel any existing job to ensure no duplicate timers are running
        job = timerScope.launch {
            while (isActive && totalTime >= 0) {
                isRunning = true

                val hours = totalTime / 3600
                val minutes = (totalTime % 3600) / 60
                val seconds = totalTime % 60

                updateTime(ExerciseTime(hours, minutes, seconds))
                delay(1000)
                totalTime--
            }
        }
    }

    override fun pauseTimer() {
        if (isRunning) {
            job?.cancel()
            isRunning = false
        }
    }

    override suspend fun resumeTimer(updateTime: (ExerciseTime) -> Unit) {
        if (!isRunning && totalTime > 0) {
            startTimer(totalTime, updateTime)
        }
    }

    override fun resetTimer() {
        totalTime = 0
        job?.cancel()
        isRunning = false
    }


    //Used by ExerciseTimer
    companion object {
        fun calculateTimeLeftPercentage(
            startTime: ExerciseTime,
            currentTime: ExerciseTime
        ): Float {
            val totalTimeInSeconds = startTime.toSeconds()
            val currentTimeInSeconds = currentTime.toSeconds()
            val elapsedTimeInSeconds = totalTimeInSeconds - currentTimeInSeconds

            //Negative elapsed time check
            val safeElapsedTimeInSeconds = if (elapsedTimeInSeconds < 0) 0 else elapsedTimeInSeconds

            return safeElapsedTimeInSeconds.toFloat() / totalTimeInSeconds * 100
        }

        fun calculateMarkedLines(percentageTimeLeft: Float, totalLines: Int): Int {
            val markedLines = (totalLines * (100 - percentageTimeLeft) / 100).toInt()

            //More than the total lines or less than 0 check
            return when {
                markedLines > totalLines -> totalLines
                markedLines < 0 -> 0
                else -> markedLines
            }
        }
    }

    fun isRunning(): Boolean {
        return isRunning
    }
}