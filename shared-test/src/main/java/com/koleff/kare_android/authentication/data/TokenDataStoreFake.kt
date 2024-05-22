package com.koleff.kare_android.authentication.data

import com.koleff.kare_android.common.preferences.TokenDataStore
import com.koleff.kare_android.data.model.dto.Tokens

class TokenDataStoreFake: TokenDataStore {
    private var tokens: Tokens? = null
    var isCleared = false
        private set

    override fun getTokens(): Tokens? {
        return tokens
    }

    override fun updateTokens(tokens: Tokens) {
        this.tokens = tokens
        isCleared = false
    }

    override fun deleteTokens() {
        tokens = null
        isCleared = true
    }
}