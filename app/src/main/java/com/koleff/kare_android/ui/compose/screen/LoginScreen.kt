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
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {

        //Left divider
        Box(modifier = Modifier.weight(1f)) {
            Divider(color = color)
        }

        //Login text
        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = TextStyle(
                color = color,
                fontSize = 16.sp,
                fontWeight = FontWeight.Thin
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Right divider
        Box(modifier = Modifier.weight(1f)) {
            Divider(color = color)
        }
    }
}

//TODO: title with subtitle...
//TODO: SignUpFooter...
//TODO: background image (space theme?)
//TODO: textFieldBox... (strong password meter?)
@Preview
@Composable
fun HorizontalLineWithTextPreview() {
    HorizontalLineWithText("Or continue with")
}

@Composable
fun GoogleBox() {
    Box(
        modifier = Modifier
            .width(150.dp)
            .height(75.dp)
            .background(color = Color.Black)
            .border(
                border = BorderStroke(2.dp, color = Color.White),
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        //Google logo
        Image(
            modifier = Modifier.size(50.dp),
            painter = painterResource(R.drawable.ic_google_logo),
            contentDescription = "Google logo image",
            contentScale = ContentScale.Crop
        )
    }
}

@Preview
@Composable
fun GoogleBoxPreview() {
    GoogleBox()
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}