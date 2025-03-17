package com.koleff.kare_android.common.preferences

interface OnboardingDataStore {

    fun getHasOnboarded(): Boolean?

    fun saveHasOnboarded(hasOnboarded: Boolean)

    fun deleteHasOnboarded()
}