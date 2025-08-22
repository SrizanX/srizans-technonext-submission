package com.srizan.technonextcodingassessment.signup

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SignUpNavKey

fun NavGraphBuilder.signUpGraph(
    navigateToPostsScreen: () -> Unit,
) {
    composable<SignUpNavKey> {
        val viewModel: SignUpViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        SignUpScreenContent(
            uiState = uiState,
            onEmailChange = viewModel::updateEmail,
            onPasswordChange = viewModel::updatePassword,
            onConfirmPasswordChange = viewModel::updateConfirmPassword,
            onRegisterClick = {
                viewModel.register(uiState.email, uiState.password, uiState.confirmPassword)
            })
    }
}

fun NavController.navigateToSignUpScreen() {
    navigate(SignUpNavKey)
}