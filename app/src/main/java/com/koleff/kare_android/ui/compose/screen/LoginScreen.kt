package com.koleff.kare_android.ui.compose.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.koleff.kare_android.R
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.common.navigation.Destination
import com.koleff.kare_android.common.navigation.NavigationEvent
import com.koleff.kare_android.ui.compose.components.LoadingWheel
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.AuthenticationScaffold
import com.koleff.kare_android.ui.compose.components.navigation_components.scaffolds.ExerciseDetailsConfiguratorScaffold
import com.koleff.kare_android.ui.compose.dialogs.ErrorDialog
import com.koleff.kare_android.ui.view_model.LoginViewModel

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp

    val cornerSize = 36.dp

    val gymImageModifier = Modifier
        .fillMaxWidth()
        .height(screenHeight * 0.33f)

    //State and callbacks
    val loginState by loginViewModel.state.collectAsState()

    val onSignIn: (Credentials) -> Unit = { credentials ->
        Log.d("LoginScreen", "Signing in with credentials: $credentials")
        loginViewModel.login(credentials)
    }

    LaunchedEffect(loginState) {
        if (loginState.isSuccessful) {
            Log.d("LoginScreen", "Successfully signed in! Caching credentials and tokens!")

            //Cache credentials and tokens
            loginViewModel.saveCredentials()

            //Navigates to Dashboard
            loginViewModel.navigateToDashboard()
        }
    }

    var showErrorDialog by remember { mutableStateOf(false) }

    //Update showErrorDialog based on loginState
    LaunchedEffect(loginState.isError) {
        showErrorDialog = loginState.isError
    }

    val onDismiss = {
        loginViewModel.clearError() //Enters launched effect to update showErrorDialog...
    }

    var username by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val onGoogleSign: () -> Unit = {} //TODO: wire with OAuth2...

    //Loading screen
    if (loginState.isLoading) {
        LoadingWheel()
    }

    if (showErrorDialog) {
        ErrorDialog(loginState.error, onDismiss)
    }

    //Screen
    AuthenticationScaffold(
        screenTitle = "",
        onNavigateBackAction = {
            loginViewModel.navigateToWelcome()
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            CustomTitleAndSubtitle(
                title = "Welcome back!",
                subtitle = "We missed you!"
            )

            //User text box
            CustomTextField(label = "Username", iconId = R.drawable.ic_user_3) {
                username = it
            }

            //Password text box
            PasswordTextField(label = "Password") {
                password = it
            }

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

@Composable
fun CustomTitleAndSubtitle(title: String, subtitle: String) {
    val textColor = Color.White

    val titlePadding =
        PaddingValues(
            top = 8.dp,
            bottom = 0.dp,
            start = 8.dp,
            end = 8.dp
        )

    val subtitlePadding =
        PaddingValues(
            top = 2.dp,
            bottom = 64.dp,
            start = 8.dp,
            end = 8.dp
        )

    //Title
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                titlePadding
            ),
            text = title,
            style = TextStyle(
                color = textColor,
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }

    //Subtitle
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(
                subtitlePadding
            ),
            text = subtitle,
            style = TextStyle(
                color = textColor,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun CustomTextField(
    label: String,
    iconId: Int,
    onValueChange: (String) -> Unit
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )
    val cornerSize = 8.dp //slight round corners

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = {
            Text(label)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            ),
        singleLine = true,
        placeholder = {
            Text(label)
        },
        leadingIcon = {
            Image(
                modifier = Modifier
                    .size(45.dp)
                    .padding(vertical = 8.dp, horizontal = 2.dp),
                painter = painterResource(iconId),
                contentDescription = "Icon",
                contentScale = ContentScale.Inside
//                    colorFilter = ColorFilter.tint(
//                        color = MaterialTheme.colorScheme.primary,
//                        blendMode = BlendMode.DstIn
//                    )
            )
//            Icon(
//                painter = painterResource(iconId),
//                contentDescription = "Text box icon"
//            )
        }
    )
}

@Preview
@Composable
fun CustomTextFieldPreview() {
    CustomTextField(
        label = "Username",
        iconId = R.drawable.ic_user_2,
        onValueChange = {}
    )
}

@Composable
fun PasswordTextField(
    label: String = "Password",
    onValueChange: (String) -> Unit,
) {
    var password by rememberSaveable {
        mutableStateOf("")
    }
    var passwordVisible by rememberSaveable {
        mutableStateOf(false)
    }

    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )
    val cornerSize = 8.dp //slight round corners

    TextField(
        value = password,
        onValueChange = {
            password = it
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            ),
        label = {
            Text(label)
        },
        singleLine = true,
        placeholder = {
            Text(label)
        },
        leadingIcon = {
            Image(
                modifier = Modifier
                    .size(45.dp)
                    .padding(vertical = 8.dp, horizontal = 2.dp),
                painter = painterResource(id = R.drawable.ic_vector_password),
                contentDescription = "Password icon",
//                    colorFilter = ColorFilter.tint(
//                        color = MaterialTheme.colorScheme.primary,
//                        blendMode = BlendMode.DstIn
//                    )
            )
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image = if (passwordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            val contentDescription = if (passwordVisible) "Hide password" else "Show password"
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(imageVector = image, contentDescription)
            }
        })
}

@Preview
@Composable
fun PasswordTextFieldPreview() {
    PasswordTextField(
        label = "Password",
        onValueChange = {}
    )
}

@Composable
fun HorizontalLineWithText(
    text: String,
    color: Color = Color.White
) {
    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(paddingValues)
    ) {

        //Left divider
        Box(modifier = Modifier.weight(1f)) {
            HorizontalDivider(color = color)
        }

        //Login text
        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = TextStyle(
                color = color,
                fontSize = 18.sp,
                fontWeight = FontWeight.Thin
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Right divider
        Box(modifier = Modifier.weight(1f)) {
            HorizontalDivider(color = color)
        }
    }
}

@Preview
@Composable
fun HorizontalLineWithTextPreview() {
    HorizontalLineWithText("Or continue with")
}

@Composable
fun AuthenticationButton(
    text: String,
    onAction: (Credentials) -> Unit,
    credentials: Credentials
) {
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )
    val textColor = Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = { onAction(credentials) }),
        contentAlignment = Alignment.Center
    ) {

        //Sign in text
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = TextStyle(
                color = textColor,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun SignInButtonPreview() {
    AuthenticationButton(
        text = "Sign in",
        onAction = {

        },
        credentials = Credentials()
    )
}

@Composable
fun SignInFooter(onGoogleSign: () -> Unit) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalLineWithText("Or continue with")

        GoogleSignInBox(onGoogleSign = onGoogleSign)
    }
}

@Preview
@Composable
fun SignInFooterPreview() {
    SignInFooter(onGoogleSign = {})
}


@Composable
fun GoogleSignInBox(onGoogleSign: () -> Unit) {
    val cornerSize = 16.dp

    Box(
        modifier = Modifier
            .width(75.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .background(color = MaterialTheme.colorScheme.primaryContainer)
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable {
                onGoogleSign()
            },
        contentAlignment = Alignment.Center
    ) {

        //Google logo
        Image(
            modifier = Modifier.size(36.dp),
            painter = painterResource(R.drawable.ic_google_logo),
            contentDescription = "Google logo image",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun GoogleSignInBoxPreview() {
    GoogleSignInBox(onGoogleSign = {})
}
