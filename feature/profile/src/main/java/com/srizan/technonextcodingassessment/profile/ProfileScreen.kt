package com.srizan.technonextcodingassessment.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.srizan.technonextcodingassessment.designsystem.R
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.model.AppThemeConfig
import com.srizan.technonextcodingassessment.ui.AppAlertDialog

/**
 * Internal composable that integrates with ViewModel and handles state management
 */
@Composable
internal fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val themeConfig by viewModel.appThemeConfig.collectAsStateWithLifecycle()
    
    ProfileScreen(
        themeConfig = themeConfig,
        onChangeDarkThemeConfig = viewModel::changeAppThemeConfig,
        onSignOut = viewModel::signOut,
        modifier = modifier
    )
}

/**
 * Public composable that accepts UI state and callbacks - for testing and previews
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    themeConfig: AppThemeConfig,
    onChangeDarkThemeConfig: (AppThemeConfig) -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
    email: String = "user@example.com", // Inject or pass from ViewModel
) {
    var showSignOutConfirmationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(12.dp)
            .fillMaxSize()
    ) {
        ProfileAppBar()

        Spacer(Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = email, style = MaterialTheme.typography.titleMedium
            )




            SettingsDialogSectionTitle(text = stringResource(R.string.app_theme_preference))
            Column(Modifier.selectableGroup()) {
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.app_theme_mode_config_system_default),
                    selected = themeConfig == AppThemeConfig.SYSTEM,
                    onClick = { onChangeDarkThemeConfig(AppThemeConfig.SYSTEM) },
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.app_theme_mode_config_light),
                    selected = themeConfig == AppThemeConfig.LIGHT,
                    onClick = { onChangeDarkThemeConfig(AppThemeConfig.LIGHT) },
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.app_theme_mode_config_dark),
                    selected = themeConfig == AppThemeConfig.DARK,
                    onClick = { onChangeDarkThemeConfig(AppThemeConfig.DARK) },
                )
            }
        }

        OutlinedButton(
            onClick = { showSignOutConfirmationDialog = true }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign Out")
        }
    }

    if (showSignOutConfirmationDialog) {
        AppAlertDialog(
            onDismissRequest = { showSignOutConfirmationDialog = false },
            onConfirmation = {
                showSignOutConfirmationDialog = false
                onSignOut()
            },
            dialogTitle = "Confirm Sign Out",
            dialogText = "Are you sure you want to sign out?",
            icon = Icons.Default.Warning,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(modifier: Modifier = Modifier) {
    TopAppBar(
        title = { Text("Profile") }, modifier = modifier
    )
}


@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}


@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    AppTheme {
        Surface {
            ProfileScreen(
                themeConfig = AppThemeConfig.SYSTEM,
                onChangeDarkThemeConfig = {},
                onSignOut = {},
            )
        }
    }
}
