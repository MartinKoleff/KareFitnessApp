package com.koleff.kare_android

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.multidex.MultiDexApplication
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.room.manager.ExerciseDBManager
import com.koleff.kare_android.data.room.manager.UserDBManager
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

    @Inject
    lateinit var userDBManager: UserDBManager

    @Inject
    lateinit var preferences: Preferences

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super<MultiDexApplication>.onCreate()

        //Initialize Room DB on app start
        GlobalScope.launch(Dispatchers.IO) {
            exerciseDBManager.initializeExerciseTable {
                preferences.initializeExerciseTable()
            }

            workoutDBManager.initializeWorkoutTable {
                preferences.initializeWorkoutTable()
            }
            
            userDBManager.initializeUserTable {
                preferences.initializeUserTable()
            }
        }

        //Create push notification channel
        NotificationManager.createNotificationChannel(this@KareApp)

        //Subscribe to topic -> allow broadcasts.
        GlobalScope.launch(Dispatchers.IO) {
            NotificationManager.subscribeToTopic()
        }
    }
}