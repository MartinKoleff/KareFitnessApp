package com.koleff.kare_android.common.preferences

import javax.inject.Inject

class OnboardingDataStoreImpl @Inject constructor(
    private val preferences: Preferences
): OnboardingDataStore {
    override fun getHasOnboarded(): Boolean {
        return preferences.getHasOnboarded()
    }

    override fun saveHasOnboarded(hasOnboarded: Boolean) {
        preferences.saveHasOnboarded(hasOnboarded)
    }

    override fun deleteHasOnboarded() {
        preferences.deleteHasOnboarded()
    }
}