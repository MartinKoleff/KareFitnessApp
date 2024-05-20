package com.koleff.kare_android.ui.compose.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.koleff.kare_android.R

@Composable
fun LogoutSettingsListItem(
    title: String,
    icon: Painter,
    description: String,
    onLogout: () -> Unit
) {
    SettingsListItem(
        modifier = Modifier.clickable { onLogout() },
        title = title,
        icon = icon,
        description = description
    )
}

@Composable
fun SettingsListItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    description: String,
    hasSwitch: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    isChecked: Boolean = false
) {
    val textColor = MaterialTheme.colorScheme.onSurface
    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = title, style = textStyle) },
        leadingContent = {
            Icon(
                icon,
                contentDescription = description,
            )
        },
        trailingContent = {
            if (hasSwitch) {
                SwitchButton(isChecked = isChecked, onCheckedChange = onCheckedChange)
            } else {
                Icon(
                    painterResource(id = R.drawable.ic_vector_arrow_forward),
                    contentDescription = "Go inside"
                )
            }
        }
    )
    HorizontalDivider()
}

@Composable
fun SettingsListItem(
    modifier: Modifier = Modifier,
    title: String,
    icon: Painter,
    description: String,
    hasSwitch: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    isChecked: Boolean = false
) {
    val textColor = MaterialTheme.colorScheme.onSurface

    val textStyle = MaterialTheme.typography.titleSmall.copy(
        color = textColor
    )

    ListItem(
        modifier = modifier,
        headlineContent = { Text(text = title, style = textStyle) },
        leadingContent = {
            Icon(
                icon,
                contentDescription = description,
            )
        },
        trailingContent = {
            if (hasSwitch) {
                SwitchButton(isChecked = isChecked, onCheckedChange = onCheckedChange)
            } else {
                Icon(
                    painterResource(id = R.drawable.ic_vector_arrow_forward),
                    contentDescription = "Go inside",
                )
            }
        }
    )
    HorizontalDivider()
}

@Composable
fun SettingsCategory(title: String) {

}

@Composable
fun SwitchButton(isChecked: Boolean = false, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        checked = isChecked,
        onCheckedChange = onCheckedChange
    )
}

@Composable
fun SettingsList(
    modifier: Modifier = Modifier,
    notificationIsChecked: Boolean,
    biometricsIsChecked: Boolean,
    onNotificationSwitchChange: (Boolean) -> Unit,
    onBiometricsSwitchChange: (Boolean) -> Unit,
    onLogout: () -> Unit
) {
    Column(modifier = modifier) {
        LogoutSettingsListItem(
            title = "Logout",
            icon = painterResource(R.drawable.ic_vector_logout),
            description = "Logout from account",
            onLogout = onLogout
        )
        SettingsListItem(
            title = "Change email",
            icon = Icons.Default.Email,
            description = "Change login email"
        )
        SettingsListItem(
            title = "Change password",
            icon = painterResource(R.drawable.ic_vector_password),
            description = "Change password"
        )

        SettingsListItem(
            title = "Push notifications",
            icon = Icons.Default.Notifications,
            description = "Push notifications settings",
            hasSwitch = true,
            isChecked = notificationIsChecked,
            onCheckedChange = onNotificationSwitchChange
        )
        SettingsListItem(
            title = "Change language",
            icon = painterResource(R.drawable.ic_vector_language),
            description = "Change language"
        )
        SettingsListItem(
            title = "Biometric authentication",
            icon = painterResource(R.drawable.ic_faceid),
            description = "Biometric authentication",
            hasSwitch = true,
            isChecked = biometricsIsChecked,
            onCheckedChange = onBiometricsSwitchChange
        )

        SettingsCategory(title = "Privacy policy")
        SettingsListItem(
            title = "Privacy policy",
            icon = Icons.Default.Info,
            description = "Privacy policy"
        )
        SettingsListItem(
            title = "Support",
            icon = Icons.Default.MailOutline,
            description = "Contact us via email"
        )
    }
}