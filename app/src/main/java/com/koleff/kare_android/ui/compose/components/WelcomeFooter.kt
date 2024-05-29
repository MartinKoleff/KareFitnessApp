package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R

@Composable
fun WelcomeFooter(onLogin: () -> Unit, onRegister: () -> Unit) {
    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp

    val sizeModifier = Modifier
        .fillMaxWidth()
        .height(screenHeight / 2)

    //Background
    Box(modifier = sizeModifier) {

        //Texture background
        Image(
            painter = painterResource(id = R.drawable.background_metal_texture_2),
            contentDescription = "Metal texture background",
            modifier = Modifier
                .alpha(0.75f)
                .drawWithContent {

                    val colors = listOf(
                        Color.Transparent,
                        Color.Transparent,
                        Color.Transparent,
                        Color.Gray,
                        Color.Gray,
                        Color.Gray
                    )
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(colors),
                        blendMode = BlendMode.DstIn
                    )
                },
            contentScale = ContentScale.Crop
        )
    }

    val buttonPadding = PaddingValues(
        start = 16.dp,
        end = 16.dp,
        top =  8.dp,
        bottom = 24.dp
    )

    //Footer
    Column(
        modifier = sizeModifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
    ) {

        //Logo
        Image(
            modifier = Modifier
                .size(175.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo image",
            contentScale = ContentScale.Crop
        )

        //Buttons
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(buttonPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {

            //Login button
            LoginButton(
                modifier = Modifier.padding(bottom = 8.dp),
                onLogin
            )

            //Register/ Sign up button
            RegisterButton(
                modifier = Modifier.padding(top = 8.dp),
                onRegister
            )
        }
    }
}

/**
 * Currently unused
 */
@Composable
fun LogoRow() {
    val textColor = MaterialTheme.colorScheme.primary
    val textStyle = MaterialTheme.typography.headlineLarge.copy(
        color = textColor
    )


    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        //Welcome to
        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = "Welcome to",
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Logo
        Image(
            modifier = Modifier
                .size(175.dp),
            painter = painterResource(R.drawable.logo),
            contentDescription = "Logo image",
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun RegisterButton(modifier: Modifier = Modifier, onRegister: () -> Unit) {
    WideRoundButton(
        modifier = modifier,
        text = "Register",
        callback = onRegister
    )
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, onLogin: () -> Unit) {
    WideRoundButton(
        modifier = modifier,
        text = "Login",
        callback = onLogin
    )
}

@Composable
fun WideRoundButton(
    modifier: Modifier = Modifier,
    text: String,
    callback: () -> Unit
) {
    val cornerSize = 24.dp

    val buttonColor = MaterialTheme.colorScheme.tertiary
    val textColor = MaterialTheme.colorScheme.onSurface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val textStyle = MaterialTheme.typography.headlineSmall.copy(
        color = textColor
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = buttonColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = callback),
        contentAlignment = Alignment.Center
    ) {

        //Texture background
        Image(
            painter = painterResource(id = R.drawable.background_metal_texture_3),
            contentDescription = "Metal texture background",
            modifier = Modifier.alpha(0.65f),
            contentScale = ContentScale.Crop
        )

        //Login text
        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}


@Preview()
@Composable
fun WelcomeFooterPreview() {
    WelcomeFooter(
        onLogin = {},
        onRegister = {}
    )
}