package com.koleff.kare_android.ui.view_model

import com.koleff.kare_android.common.navigation.NavigationController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val navigationController: NavigationController
) : BaseViewModel(navigationController = navigationController) {

    override fun clearError() {
        TODO("Not yet implemented")
    }
}
