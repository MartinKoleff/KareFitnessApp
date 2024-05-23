package com.koleff.kare_android.common.broadcast.handler

import com.koleff.kare_android.ui.state.TokenState
import kotlinx.coroutines.flow.Flow

interface RegenerateTokenNotifier {
    val regenerateTokenState: Flow<TokenState>

    //suspend fun notifyTokenRegenerationResult(result: ResultWrapper<Boolean>)
}