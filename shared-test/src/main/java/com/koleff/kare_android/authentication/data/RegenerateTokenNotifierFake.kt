package com.koleff.kare_android.authentication.data

import com.koleff.kare_android.common.broadcast.handler.RegenerateTokenNotifier
import com.koleff.kare_android.ui.state.TokenState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow

class RegenerateTokenNotifierFake : RegenerateTokenNotifier {
    private var _regenerateTokenState = MutableStateFlow(TokenState())
    override val regenerateTokenState: Flow<TokenState> = _regenerateTokenState.asSharedFlow()

    fun emit(state: TokenState) {
        _regenerateTokenState.value = state
    }
}