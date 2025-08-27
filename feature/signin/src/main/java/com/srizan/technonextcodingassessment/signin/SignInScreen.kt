package com.srizan.technonextcodingassessment.signin


import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.srizan.technonextcodingassessment.designsystem.R
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.ui.HandleEvent


@Composable
internal fun SignInScreen(
    viewModel: SignInViewModel = hiltViewModel(),
    navigateToSignUpScreen: () -> Unit,
    navigateToPostsScreen: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    HandleEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SignInViewModel.SignInUiEvent.SignInError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }

            SignInViewModel.SignInUiEvent.SignInSuccess -> navigateToPostsScreen()
        }
    }
    SignInScreen(
        uiState = uiState,
        onEmailInputChange = viewModel::updateEmail,
        onPasswordInputChange = viewModel::updatePassword,
        onSignInClick = viewModel::signIn,
        onSignUpClick = navigateToSignUpScreen,
    )
}

@Composable
internal fun SignInScreen(
    uiState: SignInViewModel.UiState,
    onEmailInputChange: (String) -> Unit,
    onPasswordInputChange: (String) -> Unit,
    onSignInClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    
    // Extract string resources for use throughout the composable
    val showPasswordDesc = stringResource(R.string.signin_password_show_content_description)
    val hidePasswordDesc = stringResource(R.string.signin_password_hide_content_description)
    val emailContentDesc = stringResource(R.string.signin_email_content_description)
    val passwordContentDesc = stringResource(R.string.signin_password_content_description)
    val buttonContentDesc = stringResource(R.string.signin_button_content_description)
    val signupSectionContentDesc = stringResource(R.string.signin_signup_section_content_description)

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Header Section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Text(
                    text = stringResource(R.string.signin_title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signin_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            var isPasswordVisible by remember { mutableStateOf(false) }

            // Email Field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailInputChange,
                label = { Text(stringResource(R.string.signin_email_label)) },
                placeholder = { Text(stringResource(R.string.signin_email_placeholder)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = stringResource(R.string.signin_email_icon_content_description),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                isError = uiState.emailError != null,
                supportingText = uiState.emailError?.let {
                    {
                        Text(
                            text = it, color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = emailContentDesc })

            Spacer(modifier = Modifier.height(12.dp))

            // Password Field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordInputChange,
                label = { Text(stringResource(R.string.signin_password_label)) },
                placeholder = { Text(stringResource(R.string.signin_password_placeholder)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = stringResource(R.string.signin_password_icon_content_description),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            imageVector = if (isPasswordVisible) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = if (isPasswordVisible) hidePasswordDesc else showPasswordDesc,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                isError = false, // No password validation errors for sign-in
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (uiState.isFormValid) {
                            onSignInClick()
                        }
                        focusManager.clearFocus()
                    }),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = passwordContentDesc })

            Spacer(modifier = Modifier.height(32.dp))

            // Sign In Button
            Button(
                onClick = onSignInClick,
                enabled = uiState.isFormValid && !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .semantics { contentDescription = buttonContentDesc }) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.signin_button_loading))
                } else {
                    Text(
                        stringResource(R.string.signin_button_label),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign Up Section
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.semantics { contentDescription = signupSectionContentDesc }) {
                Text(
                    stringResource(R.string.signin_signup_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onSignUpClick, enabled = !uiState.isLoading
                ) {
                    Text(
                        stringResource(R.string.signin_signup_button), fontWeight = FontWeight.Medium
                    )
                }
            }

            // Error Message Display
            uiState.errorMessage?.let { error ->
                val errorContentDesc = stringResource(R.string.signin_error_content_description, error)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = errorContentDesc })
            }
        }
    }
}

@Preview(showBackground = true, name = "Light Mode")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark Mode")
@Preview(showBackground = true, device = "spec:width=360dp,height=640dp", name = "Small Screen")
@Composable
private fun SignInPreview() {
    AppTheme {
        Surface {
            SignInScreen(
                uiState = SignInViewModel.UiState(),
                onEmailInputChange = {},
                onPasswordInputChange = {},
                onSignInClick = { },
                onSignUpClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Loading State")
@Composable
private fun SignInLoadingPreview() {
    AppTheme {
        Surface {
            SignInScreen(
                uiState = SignInViewModel.UiState(isLoading = true),
                onEmailInputChange = {},
                onPasswordInputChange = {},
                onSignInClick = { },
                onSignUpClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Error State")
@Composable
private fun SignInErrorPreview() {
    AppTheme {
        Surface {
            SignInScreen(
                uiState = SignInViewModel.UiState(errorMessage = "Invalid credentials. Please try again."),
                onEmailInputChange = {},
                onPasswordInputChange = {},
                onSignInClick = { },
                onSignUpClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Validation Errors")
@Composable
private fun SignInValidationErrorsPreview() {
    AppTheme {
        Surface {
            SignInScreen(
                uiState = SignInViewModel.UiState(
                    email = "invalid-email", password = "", // Empty password
                    emailError = "Invalid email format", isFormValid = false
                ),
                onEmailInputChange = {},
                onPasswordInputChange = {},
                onSignInClick = { },
                onSignUpClick = {},
            )
        }
    }
}

@Preview(showBackground = true, name = "Form Valid")
@Composable
private fun SignInFormValidPreview() {
    AppTheme {
        Surface {
            SignInScreen(
                uiState = SignInViewModel.UiState(
                    email = "user@example.com", password = "password123", isFormValid = true
                ),
                onEmailInputChange = {},
                onPasswordInputChange = {},
                onSignInClick = { },
                onSignUpClick = {},
            )
        }
    }
}