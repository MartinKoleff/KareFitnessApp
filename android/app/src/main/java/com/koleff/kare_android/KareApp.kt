package com.koleff.kare_android

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class KareApp : MultiDexApplication(), DefaultLifecycleObserver {

    override fun onCreate() {
        super<MultiDexApplication>.onCreate()
    }
}