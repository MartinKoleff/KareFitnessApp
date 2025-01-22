package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.MainDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.state.DashboardState
import com.koleff.kare_android.domain.wrapper.ResultWrapper
import com.koleff.kare_android.domain.repository.DashboardRepository
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.data.model.dto.MuscleGroup
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: Preferences,
    private val navigationController: NavigationController,
    @MainDispatcher private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : BaseViewModel(navigationController = navigationController) { //, OnboardingScreenNavigation


    fun onNavigateBack() {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }

    override fun clearError() {

    }
}
