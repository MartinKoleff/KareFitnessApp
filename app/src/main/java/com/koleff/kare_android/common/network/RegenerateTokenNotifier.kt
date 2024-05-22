package com.koleff.kare_android.common.network

import com.koleff.kare_android.ui.state.TokenState
import kotlinx.coroutines.flow.Flow

interface RegenerateTokenNotifier {
    val regenerateTokenState: Flow<TokenState>
}