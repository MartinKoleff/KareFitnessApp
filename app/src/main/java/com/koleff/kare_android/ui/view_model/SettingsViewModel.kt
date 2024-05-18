package com.koleff.kare_android.ui.view_model

import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.common.preferences.Preferences
import com.koleff.kare_android.data.model.dto.UserDto
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val authenticationUseCases: AuthenticationUseCases,
    private val navigationController: NavigationController,
    private val preferences: Preferences,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController = navigationController), MainScreenNavigation {

    private var _logoutState: MutableStateFlow<BaseState> =
        MutableStateFlow(BaseState())
    val logoutState: StateFlow<BaseState>
        get() = _logoutState

    fun logout(){
        val user = preferences.getCredentials() as UserDto

        viewModelScope.launch(dispatcher) {
            authenticationUseCases.logoutUseCase(user).collect{ logoutState ->
                _logoutState.value = logoutState

                if(logoutState.isSuccessful){

                    //Navigate to Welcome Screen
                    preferences.deleteCredentials()
                    navigateToWelcomeScreen()
                }
            }
        }
    }

    private fun navigateToWelcomeScreen() {
        super.onNavigationEvent(
            NavigationEvent.ClearBackstackAndNavigateTo(
                destination = Destination.Welcome
            )
        )
    }

    override fun clearError() {
        if(logoutState.value.isError){
            _logoutState.value = BaseState()
        }
    }

    override fun onNavigateToDashboard() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Dashboard))
    }

    override fun onNavigateToWorkouts() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Workouts))
    }

    override fun onNavigateToSettings() {
        super.onNavigationEvent(NavigationEvent.NavigateTo(Destination.Settings))
    }

    override fun onNavigateBack()  {
        super.onNavigationEvent(NavigationEvent.NavigateBack)
    }
}
