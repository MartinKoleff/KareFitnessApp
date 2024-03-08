package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.common.credentials_validator.Credentials
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.compose.dialogs.SuccessDialog
import com.koleff.kare_android.ui.view_model.RegisterViewModel

@Composable
fun RegisterScreen(registerViewModel: RegisterViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val cornerSize = 36.dp

    val gymImageModifier = Modifier
        .fillMaxWidth()
        .height(screenHeight * 0.33f)

    //State and callbacks
    val registerState by registerViewModel.state.collectAsState()
    val onSignUp: (Credentials) -> Unit = { credentials ->
        Log.d("RegisterScreen", "Signing up with credentials: $credentials")
        registerViewModel.register(credentials)
    }

    var showSuccessDialog by remember { mutableStateOf(false) }

    //Show success dialog
    LaunchedEffect(registerState.isSuccessful) {
        showSuccessDialog = registerState.isSuccessful
    }

    var showErrorDialog by remember { mutableStateOf(false) }

    //Update showErrorDialog based on loginState
    LaunchedEffect(registerState.isError) {
        showErrorDialog = registerState.isError
    }

    val onDismiss = {
        registerViewModel.clearError() //Enters launched effect to update showErrorDialog...
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
    //Background image
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                val colors = listOf(
                    Color.Red,
                    Color.Red,
                    Color.Black,
                    Color.Blue
                )
                drawContent()
                drawRect(
                    brush = Brush.linearGradient(colors),
                    blendMode = BlendMode.Overlay //ColorBurn
                )
            }
    ) {

        //Texture background
        Image(
            painter = painterResource(id = R.drawable.ic_login_background_4),
            contentDescription = "Background",
            contentScale = ContentScale.Crop
        )
    }

    //Loading screen
    if (registerState.isLoading) {
        LoadingWheel()
    }

    if (showErrorDialog) {
        ErrorDialog(registerState.error, onDismiss)
    }

    if (showSuccessDialog) {
        SuccessDialog(
            title = "You have registered successfully!",
            onDismiss = onDismiss,
            onClick = onDismiss
        )
    }

    //Screen
    Column(
        modifier = Modifier.fillMaxSize(), //screenContentModifier
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        //Gym image
        Image(
            modifier = gymImageModifier
                .clip(RoundedCornerShape(cornerSize))
                .padding(bottom = 6.dp),
            painter = painterResource(id = R.drawable.ic_default),
            contentDescription = "Top Image",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}