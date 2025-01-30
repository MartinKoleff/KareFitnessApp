package com.koleff.kare_android.ui.compose.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.koleff.kare_android.R
import com.koleff.kare_android.common.ColorManager.colorFromResource
import com.koleff.kare_android.common.auth.Credentials
import com.koleff.kare_android.ui.theme.LocalExtendedColors
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Local

@Composable
fun AuthorizationTitleAndSubtitle(title: String, subtitle: String) {
    val titleTextColor = LocalExtendedColors.current.title
    val titleTextStyle = MaterialTheme.typography.displayMedium.copy(
        color = titleTextColor
    )

    val subtitleTextColor = LocalExtendedColors.current.subtitle
    val subtitleTextStyle = MaterialTheme.typography.titleSmall.copy(
        color = subtitleTextColor
    )

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
            bottom = 24.dp,
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
            style = titleTextStyle,
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
            style = subtitleTextStyle,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun AuthorizationTitleAndSubtitlePreview() {
    AuthorizationTitleAndSubtitle("Sign in", "Welcome back")
}

@Composable
fun CustomTextField(
    label: String,
    iconResourceId: Int,
    onValueChange: (String) -> Unit
) {
    var text by rememberSaveable {
        mutableStateOf("")
    }

    val paddingValues = PaddingValues(
        horizontal = 32.dp,
        vertical = 8.dp
    )

    val cornerSize = 32.dp

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val labelTextColor = LocalExtendedColors.current.label
    val tintColor = LocalExtendedColors.current.label

    val labelTextStyle = MaterialTheme.typography.labelLarge.copy(
        color = labelTextColor
    )

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        label = {
            Text(text = label, style = labelTextStyle)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        singleLine = true,
        placeholder = {
            Text(text = label, style = labelTextStyle)
        },
        leadingIcon = {
            Image(
                modifier = Modifier
                    .size(45.dp)
                    .padding(vertical = 8.dp, horizontal = 2.dp),
                painter = painterResource(iconResourceId),
                contentDescription = "Icon",
                contentScale = ContentScale.Inside,
                colorFilter = ColorFilter.tint(
                    color = tintColor,
                    blendMode = BlendMode.SrcIn
                )
            )
//            Icon(
//                painter = painterResource(iconResourceId),
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
        iconResourceId = R.drawable.ic_user_2,
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

    val cornerSize = 32.dp

    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val labelTextColor = LocalExtendedColors.current.label
    val tintColor = LocalExtendedColors.current.label

    val labelTextStyle = MaterialTheme.typography.labelLarge.copy(
        color = labelTextColor
    )

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
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            ),
        label = {
            Text(text = label, style = labelTextStyle)
        },
        singleLine = true,
        placeholder = {
            Text(text = label, style = labelTextStyle)
        },
        leadingIcon = {
            Image(
                modifier = Modifier
                    .size(45.dp)
                    .padding(vertical = 8.dp, horizontal = 2.dp),
                painter = painterResource(id = R.drawable.ic_vector_password),
                contentDescription = "Password icon",
                colorFilter = ColorFilter.tint(
                    color = tintColor,
                    blendMode = BlendMode.SrcIn
                )
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
                Icon(
                    imageVector = image,
                    tint = tintColor,
                    contentDescription = contentDescription
                )
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
    text: String
) {
    val paddingValues = PaddingValues(
        horizontal = 32.dp
    )

    val outlineColor = MaterialTheme.colorScheme.outlineVariant //onSurface
    val labelTextColor = LocalExtendedColors.current.label
    val labelTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = labelTextColor
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(paddingValues)
    ) {

        //Left divider
        Box(modifier = Modifier.weight(1f)) {
            HorizontalDivider(color = outlineColor)
        }

        Text(
            text = text,
            style = labelTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        //Right divider
        Box(modifier = Modifier.weight(1f)) {
            HorizontalDivider(color = outlineColor)
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
    modifier: Modifier = Modifier,
    text: String,
    onAction: (Credentials) -> Unit,
    credentials: Credentials
) {
    val cornerSize = 32.dp

//    val paddingValues = PaddingValues(
//        horizontal = 32.dp,
//        vertical = 16.dp
//    )
    val outlineColor = MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = MaterialTheme.colorScheme.primary
    val textColor = LocalExtendedColors.current.title
    val buttonTextStyle = MaterialTheme.typography.titleLarge.copy(
        color = textColor
    )

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(RoundedCornerShape(cornerSize))
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = RoundedCornerShape(cornerSize)
            )
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(cornerSize)
            )
            .clickable(onClick = { onAction(credentials) }),
        contentAlignment = Alignment.Center
    ) {

        //Sign in text
        Text(
            modifier = Modifier.padding(
                PaddingValues(8.dp)
            ),
            text = text,
            style = buttonTextStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
fun SignInButtonPreview() {
    AuthenticationButton(
        modifier = Modifier,
        text = "Sign in",
        onAction = {

        },
        credentials = Credentials()
    )
}

@Composable
fun SignInFooter(onGoogleSign: () -> Unit) {
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
    val backgroundColor = MaterialTheme.colorScheme.surface
    val outlineColor = MaterialTheme.colorScheme.outlineVariant

    val paddingValues = PaddingValues(
        vertical = 4.dp
    )
    Box(
        modifier = Modifier
            .size(40.dp)
            .padding(paddingValues)
            .clip(CircleShape)
            .background(color = backgroundColor)
            .border(
                border = BorderStroke(2.dp, color = outlineColor),
                shape = CircleShape
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

@Composable
fun ForgotPasswordFooter(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val paddingValues = PaddingValues(
        bottom = 10.dp
    )

    val textColor = LocalExtendedColors.current.label
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable {
                onClick()
            },
            text = "Forgot your password?",
            style = textStyle,
            maxLines = 2,
            textAlign = TextAlign.Center,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Preview
@Composable
private fun ForgotPasswordFooterPreview() {
    ForgotPasswordFooter{

    }
}

@Composable
fun HyperlinkText(
    normalText: String,
    hyperlinkText: String,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    val textColor = LocalExtendedColors.current.label
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    val paddingValues = PaddingValues(
        vertical = 6.dp
    )

    val annotatedText = buildAnnotatedString {
        withStyle(
            style = SpanStyle(
                color = textColor,
            )
        ) {
            append(normalText)
        }

        pushStringAnnotation(
            tag = "Hyperlink",
            annotation = "Hyperlink"
        )

        withStyle(
            style = SpanStyle(
                color = colorFromResource(context, R.color.hyperlink),
            )
        ) {
            append(hyperlinkText)
        }
        pop()
    }

    ClickableText(
        modifier = Modifier.padding(paddingValues),
        text = annotatedText,
        style = textStyle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        onClick = { offset ->
            try {
                annotatedText.getStringAnnotations(
                    tag = "Hyperlink",
                    start = offset,
                    end = offset
                )[0].let { annotation ->
                    onClick()
                }
            }catch (e: IndexOutOfBoundsException){
                Log.d("HyperlinkText", e.message.toString())
            }
        }
    )
}

@Composable
fun SignUpHypertext(onClick: () -> Unit) {
    HyperlinkText(
        normalText = "Don't have an account? ",
        hyperlinkText = "Sign Up.",
        onClick = onClick
    )
}

@Composable
fun SignInHypertext(onClick: () -> Unit) {
    HyperlinkText(
        normalText = "Already have an account? ",
        hyperlinkText = "Sign In.",
        onClick = onClick
    )
}

@Preview
@Composable
private fun SignInHypertextPreview() {
    SignInHypertext {  }
}

@Preview
@Composable
private fun SignUpHypertextPreview() {
    SignUpHypertext {  }
}