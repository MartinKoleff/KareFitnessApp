package com.koleff.kare_android.ui.view_model

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.di.IoDispatcher
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationController
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.domain.usecases.AuthenticationUseCases
import com.koleff.kare_android.ui.state.BaseState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val navigationController: NavigationController,
    private val authenticationUseCases: AuthenticationUseCases,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : BaseViewModel(navigationController) {

    private var _state: MutableStateFlow<BaseState> = MutableStateFlow(BaseState())
    val state: StateFlow<BaseState> = _state

    fun register(credentials: Credentials) {
        viewModelScope.launch(dispatcher) {
            authenticationUseCases.registerUseCase.invoke(
                credentials
            ).collect { registerState ->

                Log.d("RegisterViewModel", "$registerState")
                _state.value = registerState
            }
        }
    }

    override fun clearError() {
        if (state.value.isError) {
            _state.value = BaseState()
        }
    }

    fun clearState() {
        _state.value = BaseState()
    }

    fun navigateToWelcome(){
        onNavigationEvent(NavigationEvent.NavigateBack)
    }
}