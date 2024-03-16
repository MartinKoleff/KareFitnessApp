package com.koleff.kare_android.common

import com.koleff.kare_android.data.model.dto.ExerciseTime
import kotlinx.coroutines.delay

object TimerUtil {
    suspend fun startTimer(totalSeconds: Int, updateTime: (ExerciseTime) -> Unit) {
        var totalTime = totalSeconds
        while (totalTime >= 0) {
            val hours = totalTime / 3600
            val minutes = (totalTime % 3600) / 60
            val seconds = totalTime % 60

            //Callback
            updateTime(ExerciseTime(hours, minutes, seconds))

            delay(1000)
            totalTime--
        }
    }
}