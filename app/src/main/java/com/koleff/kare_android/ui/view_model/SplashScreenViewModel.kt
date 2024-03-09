package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.Constants.splashScreenDelay
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.SplashScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel @Inject constructor(
    private val preferences: Preferences
) : ViewModel() {

    private val _state = MutableStateFlow(SplashScreenState())
    val state = _state.asStateFlow()

    init {
        getCredentials()
    }

    private fun getCredentials() {
        viewModelScope.launch {
            delay(splashScreenDelay)

            val credentials = preferences.getCredentials()
            val tokens = preferences.getTokens()

            val isSuccessful = credentials != null && tokens != null
            Log.d("SplashScreenViewModel", "hasCredentials: $isSuccessful")
            Log.d("SplashScreenViewModel", "Credentials: $credentials")
            Log.d("SplashScreenViewModel", "Tokens: $tokens")

            //TODO: do login request and if successful then skip login otherwise go through welcome process...
            if (isSuccessful) {
                _state.value = SplashScreenState(
                    isSuccessful = true,
                    hasCredentials = true,
                    credentials = credentials!!,
                    tokens = tokens!!,
                )
            } else {
                _state.value = SplashScreenState(
                    isSuccessful = true,
                    hasCredentials = false,
                )
            }
        }
    }
}