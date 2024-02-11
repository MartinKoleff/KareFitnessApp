package com.koleff.kare_android.ui.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.koleff.kare_android.R
import com.koleff.kare_android.common.PermissionManager

@Composable
fun SettingsListItem(
    title: String,
    icon: ImageVector,
    description: String,
    hasSwitch: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    isChecked: Boolean = false
) {
    ListItem(
        headlineContent = { Text(title) },
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
    title: String,
    icon: Painter,
    description: String,
    hasSwitch: Boolean = false,
    onCheckedChange: (Boolean) -> Unit = {},
    isChecked: Boolean = false
) {
    ListItem(
        headlineContent = { Text(title) },
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
    onBiometricsSwitchChange: (Boolean) -> Unit
) {
    Column(modifier = modifier) {
        SettingsListItem(
            "Logout",
            painterResource(R.drawable.ic_vector_logout),
            "Logout from account"
        )
        SettingsListItem("Change email", Icons.Default.Email, "Change login email")
        SettingsListItem(
            "Change password",
            painterResource(R.drawable.ic_vector_password),
            "Change password"
        )

        SettingsListItem(
            "Push notifications",
            Icons.Default.Notifications,
            "Push notifications settings",
            true,
            isChecked = notificationIsChecked,
            onCheckedChange = onNotificationSwitchChange
        )

        SettingsListItem(
            "Change language",
            painterResource(R.drawable.ic_vector_language),
            "Change language"
        )
        SettingsListItem(
            "Biometric authentication",
            painterResource(R.drawable.ic_faceid),
            "Biometric authentication",
            true,
            isChecked = biometricsIsChecked,
            onCheckedChange = onBiometricsSwitchChange
        )

        SettingsCategory("Privacy policy")
        SettingsListItem("Privacy policy", Icons.Default.Info, "Privacy policy")
        SettingsListItem("Support", Icons.Default.MailOutline, "Contact us via email")
    }
}