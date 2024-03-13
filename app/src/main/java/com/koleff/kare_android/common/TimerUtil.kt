package com.koleff.kare_android.common

import kotlinx.coroutines.delay

object TimerUtil {
    suspend fun startTimer(totalSeconds: Int, updateTime: (String) -> Unit) {
        var totalTime = totalSeconds
        while (totalTime > 0) {
            val hours = totalTime / 3600
            val minutes = (totalTime % 3600) / 60
            val seconds = totalTime % 60

            //Callback
            updateTime(String.format("%02d:%02d:%02d", hours, minutes, seconds))

            delay(1000)
            totalTime--
        }
    }
}