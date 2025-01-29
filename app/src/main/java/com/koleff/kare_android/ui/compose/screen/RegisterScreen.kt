package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
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
import com.koleff.kare_android.ui.compose.components.SignInHypertext
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.AuthenticationScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.SuccessDialog
import com.koleff.kare_android.ui.view_model.RegisterViewModel

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = hiltViewModel()) {

    //State and callbacks
    val registerState by registerViewModel.state.collectAsState()
    val onSignUp: (Credentials) -> Unit = { credentials ->
        Log.d("RegisterScreen", "Signing up with credentials: $credentials")
        registerViewModel.register(credentials)
    }

    //Dialog visibility
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var showLoadingDialog by remember { mutableStateOf(false) }


    //Error handling
    var error by remember { mutableStateOf<KareError?>(null) }
    LaunchedEffect(registerState) {
        Log.d("RegisterScreen", "Register state updated: $registerState")

        //Update showSuccessDialog based on loginState
        showSuccessDialog = registerState.isSuccessful

        //Update showErrorDialog based on loginState
        showErrorDialog = registerState.isError
        error = registerState.error

        //Update showLoadingDialog based on loginState
        showLoadingDialog = registerState.isLoading
    }

    val onDismiss = {
        showErrorDialog = false
        registerViewModel.clearError() //Enters launched effect to update showErrorDialog...
        registerViewModel.clearState() //Clear showSuccessDialog...
    }

    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }
    var email by remember {
        mutableStateOf("")
    }

    if (showErrorDialog) {
        error?.let {
            ErrorDialog(it, onDismiss)
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            title = "You have registered successfully!",
            onDismiss = onDismiss,
            onClick = onDismiss
        )
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val onGoogleSign: () -> Unit = {} //TODO: wire with OAuth2...

    AuthenticationScaffold(
        screenTitle = "",
        onNavigateBackAction = {
            registerViewModel.navigateToWelcome()
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
                title = "Sign up",
                subtitle = "Create your account"
            )

            //User text box
            CustomTextField(label = "Username", iconResourceId = R.drawable.ic_user_3) {
                username = it
            }

            //Password text box
            PasswordTextField(label = "Password") {
                password = it
            }

            //Email text box
            CustomTextField(label = "Email", iconResourceId = R.drawable.ic_email) {
                email = it
            }

            AuthenticationButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        PaddingValues(
                            start = 32.dp,
                            end = 32.dp,
                            top = 12.dp,
                            bottom = 8.dp
                        )
                    ),
                text = "Sign up",
                onAction = onSignUp,
                credentials =
                Credentials(
                    username = username,
                    password = password,
                    email = email
                )
            )

            SignInFooter(onGoogleSign = onGoogleSign)
        }

        //Footer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 10.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            SignInHypertext {
                registerViewModel.navigateToLogin()
            }
        }
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}