package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.data.model.dto.Tokens
import javax.inject.Inject

class TokenDataStoreImpl @Inject constructor(
    private val preferences: Preferences
): TokenDataStore {
    override fun getTokens(): Tokens? {
        return preferences.getTokens()
    }

    override fun updateTokens(tokens: Tokens) {
        preferences.updateTokens(tokens)
    }

    override fun deleteTokens() {
        preferences.deleteTokens()
    }
}