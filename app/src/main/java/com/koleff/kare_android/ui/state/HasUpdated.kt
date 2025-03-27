package com.koleff.kare_android.ui.state

import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HasUpdated @Inject constructor() {
    private var hasUpdated: Boolean = false

    fun getUpdateStatus(): Boolean {
        Log.d("HasUpdated", "hasUpdated: $hasUpdated")
        return hasUpdated
    }

    fun notifyUpdate(newUpdate: Boolean) {
        Log.d("HasUpdated", "hasUpdated set to $newUpdate")
        hasUpdated = newUpdate
    }
}