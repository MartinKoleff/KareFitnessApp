package com.koleff.kare_android.common.credentials_validator

import com.koleff.kare_android.ui.state.BaseState
import kotlinx.coroutines.flow.StateFlow

@Deprecated("Unused")
interface AuthenticationNotifier {
    val authenticationState: StateFlow<BaseState>
}