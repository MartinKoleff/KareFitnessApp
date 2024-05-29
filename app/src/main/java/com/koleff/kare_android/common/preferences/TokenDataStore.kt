package com.koleff.kare_android.common.preferences

import com.koleff.kare_android.data.model.dto.Tokens

interface TokenDataStore {

    fun getTokens(): Tokens?

    fun updateTokens(tokens: Tokens)

    fun deleteTokens()
}