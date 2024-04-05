package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HasUpdatedSharedViewModel @Inject constructor(): ViewModel() {
    private val _hasUpdated = MutableSharedFlow<Boolean>(replay = 1)
    val hasUpdated: SharedFlow<Boolean> = _hasUpdated.asSharedFlow()

    fun notifyUpdate(hasUpdated: Boolean) {
        viewModelScope.launch {
            _hasUpdated.emit(hasUpdated)
        }
    }
}