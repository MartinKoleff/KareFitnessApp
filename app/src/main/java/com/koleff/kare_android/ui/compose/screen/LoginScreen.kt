package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.data.model.response.base_response.KareError
import com.koleff.kare_android.ui.compose.components.AuthenticationButton
import com.koleff.kare_android.ui.compose.components.AuthorizationTitleAndSubtitle
import com.koleff.kare_android.ui.compose.components.CustomTextField
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.PasswordTextField
import com.koleff.kare_android.ui.compose.components.SignInFooter
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.AuthenticationScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.view_model.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    //Keyboard
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val usernameFocusRequester = remember { FocusRequester() }
    val passwordFocusRequester = remember { FocusRequester() }

    //State and callbacks
    val loginState by loginViewModel.state.collectAsState()

    val onSignIn: (Credentials) -> Unit = { credentials ->
        Log.d("LoginScreen", "Signing in with credentials: $credentials")
        loginViewModel.login(credentials)
    }

    //Dialog visibility
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }

    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(loginState) {
        Log.d("LoginScreen", "Login state updated: $loginState")

        //Update showErrorDialog based on loginState
        showErrorDialog = loginState.isError
        error = loginState.error

        //Update showLoadingDialog based on loginState
        showLoadingDialog = loginState.isLoading

        if (loginState.isSuccessful) {
            Log.d("LoginScreen", "Successfully signed in! Caching credentials and tokens!")

            //Cache credentials and tokens
            loginViewModel.saveCredentials()

            //Navigates to Dashboard
            loginViewModel.navigateToDashboard()
        }
    }

    val onDismiss = {
        showErrorDialog = false
        loginViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val onGoogleSign: () -> Unit = {} //TODO: wire with OAuth2...

    //Error dialog
    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onDismiss)
        }
    }

    AuthenticationScaffold(
        screenTitle = "",
        onNavigateBackAction = {
            loginViewModel.navigateToWelcome()
        }
    ) { innerPadding ->

        //Loading screen
        if (showLoadingDialog) {
            LoadingWheel(innerPadding = PaddingValues(top = 72.dp))
        }

        //Screen
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {

                    //Hide keyboard on tap outside text field boxes
                    detectTapGestures(
                        onTap = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            AuthorizationTitleAndSubtitle(
                title = "Welcome back!",
                subtitle = "We missed you!"
            )

            //User text box
            CustomTextField(
                label = "Username",
                iconResourceId = R.drawable.ic_user_3,
                focusRequester = usernameFocusRequester,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        passwordFocusRequester.requestFocus()
                    }
                ),
                onValueChange = {
                    username = it
                }
            )

            //Password text box
            PasswordTextField(
                label = "Password",
                focusRequester = passwordFocusRequester,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                ),
                onValueChange = {
                    password = it
                }
            )

            AuthenticationButton(
                text = "Sign in",
                onAction = onSignIn,
                credentials =
                Credentials(
                    username = username,
                    password = password
                )
            )
            SignInFooter(onGoogleSign = onGoogleSign)
            //TODO: add don't have an account register redirect to registerScreen...
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}
