package com.srizan.technonextcodingassessment.signin

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SignInNavKey

fun NavGraphBuilder.signInGraph(
    navigateToSignUpScreen: () -> Unit,
    navigateToPostsScreen: () -> Unit,
) {
    composable<SignInNavKey> {
        val viewModel: SignInViewModel = hiltViewModel()
        SignInScreen(
            onSignInClick = { email, password ->
                viewModel.signIn(email, password);
                navigateToPostsScreen()
            },
            onSignUpClick = navigateToSignUpScreen,
            onForgotPasswordClick = navigateToSignUpScreen
        )
    }
}