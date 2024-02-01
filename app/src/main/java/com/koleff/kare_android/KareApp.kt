package com.koleff.kare_android

import android.app.NotificationChannel
import android.content.Context
import android.os.Build
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.multidex.MultiDexApplication
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.data.room.manager.WorkoutDBManager
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
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
        GlobalScope.launch(Dispatchers.IO) {
            exerciseDBManager.initializeExerciseTableRoomDB()
            workoutDBManager.initializeWorkoutTableRoomDB()
        }

        //Create push notification channel
        NotificationManager.createNotificationChannel(this@KareApp)

        //Subscribe to topic -> allow broadcasts.
        GlobalScope.launch(Dispatchers.IO)  {
            NotificationManager.subscribeToTopic()
        }
    }
}