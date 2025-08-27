package com.srizan.technonextcodingassessment.profile

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProfileScreen(
        uiState = uiState,
        onChangeDarkThemeConfig = viewModel::changeAppThemeConfig,
        onSignOut = viewModel::signOut,
        onClearError = viewModel::clearError,
        modifier = modifier
    )
}

/**
 * Public composable that accepts UI state and callbacks - for testing and previews
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileViewModel.UiState,
    onChangeDarkThemeConfig: (AppThemeConfig) -> Unit,
    onSignOut: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showSignOutConfirmationDialog by remember { mutableStateOf(false) }

    // Show error message if present
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Could show a snackbar or toast here
            // For now, we'll just log the error
            println("Profile Error: $error")
        }
    }

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
                .weight(1f), horizontalAlignment = Alignment.Start
        ) {
            Image(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Avatar",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(96.dp)
                    .clip(CircleShape),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)

            )

            Spacer(Modifier.height(12.dp))


            Text(
                text = stringResource(R.string.profile_email_label),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )

            Text(
                text = uiState.email.ifEmpty { "user@example.com" },
                style = MaterialTheme.typography.titleMedium
            )


            Text(
                text = stringResource(R.string.app_theme_preference),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .align(Alignment.Start),
            )
            Column(Modifier.selectableGroup()) {
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.app_theme_mode_config_system_default),
                    selected = uiState.appThemeConfig == AppThemeConfig.SYSTEM,
                    onClick = { onChangeDarkThemeConfig(AppThemeConfig.SYSTEM) },
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.app_theme_mode_config_light),
                    selected = uiState.appThemeConfig == AppThemeConfig.LIGHT,
                    onClick = { onChangeDarkThemeConfig(AppThemeConfig.LIGHT) },
                )
                SettingsDialogThemeChooserRow(
                    text = stringResource(R.string.app_theme_mode_config_dark),
                    selected = uiState.appThemeConfig == AppThemeConfig.DARK,
                    onClick = { onChangeDarkThemeConfig(AppThemeConfig.DARK) },
                )
            }
        }

        OutlinedButton(
            onClick = { showSignOutConfirmationDialog = true },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isSigningOut
        ) {
            if (uiState.isSigningOut) {
                Text(text = "Signing Out...")
            } else {
                Text(text = "Sign Out")
            }
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

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Composable
private fun ProfileScreenPreview() {
    AppTheme {
        Surface {
            ProfileScreen(
                uiState = ProfileViewModel.UiState(
                    email = "test@example.com", appThemeConfig = AppThemeConfig.SYSTEM
                ),
                onChangeDarkThemeConfig = {},
                onSignOut = {},
                onClearError = {},
            )
        }
    }
}
