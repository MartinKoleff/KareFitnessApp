package com.koleff.kare_android.ui.compose.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val onLogin = {}

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onLogin) {
            Text("Login")
        }
    }
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
fun SignInButton(
    onSignIn: () -> Unit
) {
    val cornerSize = 24.dp
    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )
    val textColor = Color.White

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
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color.Yellow,
                        Color.Red,
                        Color.Red,
                        Color.Red,
                        Color.Yellow,
                    )
                ),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = onSignIn),
        contentAlignment = Alignment.Center
    ) {

        //Sign in text
        Text( //TODO: and cooler font...
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = "Sign in",
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
    SignInButton(onSignIn = {})
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
            .background(color = Color.Transparent)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable {

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
