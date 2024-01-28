package com.koleff.kare_android

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.multidex.MultiDexApplication
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class KareApp : MultiDexApplication(), DefaultLifecycleObserver {

    @Inject
    lateinit var exerciseDBManager: ExerciseDBManager

    @Inject
    lateinit var workoutDBManager: WorkoutDBManager
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super<MultiDexApplication>.onCreate()

        //Initialize Room DB on app start
        GlobalScope.launch {
            exerciseDBManager.initializeExerciseTableRoomDB()
            workoutDBManager.initializeWorkoutTableRoomDB()
        }

        //Push notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Push notifications channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}