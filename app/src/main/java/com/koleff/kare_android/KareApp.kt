package com.koleff.kare_android

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.multidex.MultiDexApplication
import com.koleff.kare_android.common.Constants.useLocalDataSource
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.room.manager.ExerciseDBManagerV2
import com.koleff.kare_android.data.room.manager.UserDBManager
import com.koleff.kare_android.data.room.manager.WorkoutDBManagerV2
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class KareApp : MultiDexApplication(), DefaultLifecycleObserver {

    @Inject
    lateinit var exerciseDBManager: ExerciseDBManagerV2

    @Inject
    lateinit var workoutDBManager: WorkoutDBManagerV2

    @Inject
    lateinit var userDBManager: UserDBManager

    @Inject
    lateinit var preferences: Preferences

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super<MultiDexApplication>.onCreate()


        //Initialize Room DB on app start (only for local testing)
        if (useLocalDataSource) {
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

        }
        //Create push notification channel
        NotificationManager.createNotificationChannel(this@KareApp)

        //Subscribe to topic -> allow broadcasts.
        GlobalScope.launch(Dispatchers.IO) {
            NotificationManager.subscribeToTopic()
        }
    }
}