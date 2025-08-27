package com.srizan.technonextcodingassessment.signup

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
import com.srizan.technonextcodingassessment.domain.validation.PasswordStrength
import com.srizan.technonextcodingassessment.signup.components.PasswordCriteria
import com.srizan.technonextcodingassessment.ui.HandleEvent

/**
 * Internal composable that integrates with ViewModel and handles state management
 */
@Composable
internal fun SignUpScreen(
    onRegisterSuccess: () -> Unit, onNavigateToSignIn: () -> Unit, modifier: Modifier = Modifier
) {
    val viewModel: SignUpViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    HandleEvent(viewModel.uiEvent) { event ->
        when (event) {
            is SignUpViewModel.UiEvent.RegisterError -> {
                // Error handling is done through uiState.errorMessage
            }

            SignUpViewModel.UiEvent.RegisterSuccess -> onRegisterSuccess()
        }
    }

    SignUpScreen(
        uiState = uiState,
        onEmailChange = viewModel::updateEmail,
        onPasswordChange = viewModel::updatePassword,
        onConfirmPasswordChange = viewModel::updateConfirmPassword,
        onRegisterClick = {
            viewModel.register(uiState.email, uiState.password, uiState.confirmPassword)
        },
        onSignInClick = onNavigateToSignIn,
        modifier = modifier
    )
}

/**
 * Public composable that accepts UI state and callbacks - for testing and previews
 */

@Composable
fun SignUpScreen(
    uiState: SignUpViewModel.UiState,
    modifier: Modifier = Modifier,
    onEmailChange: (String) -> Unit = {},
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmPasswordVisible by remember { mutableStateOf(false) }

    // Extract string resources for use throughout the composable
    val showPasswordDesc = stringResource(R.string.signup_password_show_content_description)
    val hidePasswordDesc = stringResource(R.string.signup_password_hide_content_description)
    val emailContentDesc = stringResource(R.string.signup_email_content_description)
    val passwordContentDesc = stringResource(R.string.signup_password_content_description)
    val confirmPasswordContentDesc =
        stringResource(R.string.signup_confirm_password_content_description)
    val buttonContentDesc = stringResource(R.string.signup_button_content_description)
    val signInSectionContentDesc =
        stringResource(R.string.signup_signin_section_content_description)

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
                    text = stringResource(R.string.signup_title),
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.signup_subtitle),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Email Field
            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = emailContentDesc
                    },
                label = { Text(stringResource(R.string.signup_email_label)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.emailError != null,
                supportingText = uiState.emailError?.let { { Text(it) } })

            Spacer(modifier = Modifier.height(16.dp))

            // Password Field
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = passwordContentDesc
                    },
                label = { Text(stringResource(R.string.signup_password_label)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Icon(
                            if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isPasswordVisible) hidePasswordDesc else showPasswordDesc
                        )
                    }
                },
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                isError = uiState.passwordError != null,
                supportingText = uiState.passwordError?.let { { Text(it) } })

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm Password Field
            OutlinedTextField(
                value = uiState.confirmPassword,
                onValueChange = onConfirmPasswordChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics {
                        contentDescription = confirmPasswordContentDesc
                    },
                label = { Text(stringResource(R.string.signup_confirm_password_label)) },
                leadingIcon = {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { isConfirmPasswordVisible = !isConfirmPasswordVisible }) {
                        Icon(
                            if (isConfirmPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isConfirmPasswordVisible) hidePasswordDesc else showPasswordDesc
                        )
                    }
                },
                visualTransformation = if (isConfirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password, imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (uiState.isFormValid) onRegisterClick()
                    }),
                singleLine = true,
                isError = uiState.confirmPasswordError != null,
                supportingText = uiState.confirmPasswordError?.let { { Text(it) } })

            Spacer(modifier = Modifier.height(24.dp))

            // Password Criteria
            if (uiState.password.isNotEmpty()) {
                PasswordCriteria(
                    password = uiState.password,
                    passwordStrength = uiState.passwordStrength,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Error Message
            uiState.errorMessage?.let { error ->
                val errorContentDesc =
                    stringResource(R.string.signup_error_content_description, error)
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.semantics { contentDescription = errorContentDesc })
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sign Up Button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    onRegisterClick()
                },
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
                    Text(stringResource(R.string.signup_button_loading))
                } else {
                    Text(
                        stringResource(R.string.signup_button_label),
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Sign In Section
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.semantics { contentDescription = signInSectionContentDesc }) {
                Text(
                    stringResource(R.string.signup_signin_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                TextButton(
                    onClick = onSignInClick, enabled = !uiState.isLoading
                ) {
                    Text(
                        stringResource(R.string.signup_signin_button),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true, name = "Sign Up Screen - Empty")
@Composable
private fun SignUpScreenEmptyPreview() {
    AppTheme {
        Surface {
            SignUpScreen(
                uiState = SignUpViewModel.UiState()
            )
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - With Data")
@Composable
private fun SignUpScreenWithDataPreview() {
    AppTheme {
        Surface {
            SignUpScreen(
                uiState = SignUpViewModel.UiState(
                    email = "user@example.com",
                    password = "StrongPass123!",
                    confirmPassword = "StrongPass123!",
                    isFormValid = true,
                    passwordStrength = PasswordStrength.STRONG
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - Loading")
@Composable
private fun SignUpScreenLoadingPreview() {
    AppTheme {
        Surface {
            SignUpScreen(
                uiState = SignUpViewModel.UiState(
                    email = "user@example.com",
                    password = "StrongPass123!",
                    confirmPassword = "StrongPass123!",
                    isLoading = true,
                    isFormValid = true,
                    passwordStrength = PasswordStrength.STRONG
                )
            )
        }
    }
}

@Preview(showBackground = true, name = "Sign Up Screen - Error")
@Composable
private fun SignUpScreenErrorPreview() {
    AppTheme {
        Surface {
            SignUpScreen(
                uiState = SignUpViewModel.UiState(
                    email = "invalid-email",
                    password = "weak",
                    confirmPassword = "different",
                    emailError = "Invalid email format",
                    passwordError = "Password too weak",
                    confirmPasswordError = "Passwords do not match",
                    errorMessage = "Please fix the errors above",
                    passwordStrength = PasswordStrength.WEAK
                )
            )
        }
    }
}