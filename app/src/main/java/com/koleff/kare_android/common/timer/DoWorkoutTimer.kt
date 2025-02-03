package com.koleff.kare_android.common.timer

import com.koleff.kare_android.data.model.dto.ExerciseTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

interface DoWorkoutTimer {
    suspend fun startTimer(totalSeconds: Int, updateTime: (ExerciseTime) -> Unit)
    fun pauseTimer()
    suspend fun resumeTimer(updateTime: (ExerciseTime) -> Unit)
    fun resetTimer()

}