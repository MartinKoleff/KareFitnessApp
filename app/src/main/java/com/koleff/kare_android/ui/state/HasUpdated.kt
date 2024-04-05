package com.koleff.kare_android.ui.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HasUpdated @Inject constructor(){
    private val _hasUpdated = MutableStateFlow(false)
    val hasUpdated: StateFlow<Boolean>
        get() = _hasUpdated.asStateFlow()

    fun notifyUpdate(newUpdate: Boolean) {
        _hasUpdated.value = newUpdate
    }
}