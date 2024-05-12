package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.ui.compose.components.WelcomeFooter
import com.koleff.kare_android.ui.compose.components.WelcomeVideoPlayer
import com.koleff.kare_android.ui.view_model.WelcomeViewModel

@Composable
fun WelcomeScreen(welcomeViewModel: WelcomeViewModel = hiltViewModel()) {
    val navigateToLogin = {
        welcomeViewModel.navigateToLogin()
    }
    val navigateToRegister = {
        welcomeViewModel.navigateToRegister()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {

        //Video player
        WelcomeVideoPlayer()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        //Login and sign in footer
        WelcomeFooter(onLogin = navigateToLogin, onRegister = navigateToRegister)
    }
}
