package com.koleff.kare_android

import android.content.IntentFilter
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.multidex.MultiDexApplication
import com.koleff.kare_android.common.Constants
import com.koleff.kare_android.common.Constants.useLocalDataSource
import com.koleff.kare_android.common.NotificationManager
import com.koleff.kare_android.common.broadcast.LogoutBroadcastReceiver
import com.koleff.kare_android.common.broadcast.RegenerateTokenBroadcastReceiver
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


    /**
     * Local Room DB Managers
     */

    @Inject
    lateinit var exerciseDBManager: ExerciseDBManagerV2

    @Inject
    lateinit var workoutDBManager: WorkoutDBManagerV2

    @Inject
    lateinit var userDBManager: UserDBManager

    /**
     * Shared preferences
     */

    @Inject
    lateinit var preferences: Preferences

    /**
     * Broadcast Manager
     */

    @Inject
    lateinit var broadcastManager: LocalBroadcastManager

    /**
     * Broadcast Receivers
     */

    @Inject
    lateinit var regenerateTokenBroadcastReceiver: RegenerateTokenBroadcastReceiver

    @Inject
    lateinit var logoutBroadcastReceiver: LogoutBroadcastReceiver

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

        //Register broadcast receivers
        broadcastManager.registerReceiver(
            regenerateTokenBroadcastReceiver,
            IntentFilter(Constants.ACTION_REGENERATE_TOKEN)
        )

        broadcastManager.registerReceiver(
            logoutBroadcastReceiver,
            IntentFilter(Constants.ACTION_LOGOUT)
        )

        //Create push notification channel
        NotificationManager.createNotificationChannel(this@KareApp)

        //Subscribe to topic -> allow broadcasts.
        GlobalScope.launch(Dispatchers.IO) {
            NotificationManager.subscribeToTopic()
        }
    }

//    override fun onDestroy(owner: LifecycleOwner) {
//        super.onDestroy(owner)
//
//        broadcastManager.unregisterReceiver(regenerateTokenBroadcastReceiver)
//        broadcastManager.unregisterReceiver(logoutBroadcastReceiver)
//    }
}