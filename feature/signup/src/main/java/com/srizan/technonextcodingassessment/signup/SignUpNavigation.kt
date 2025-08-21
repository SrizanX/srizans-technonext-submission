package com.srizan.technonextcodingassessment.signup

import androidx.hilt.navigation.compose.hiltViewModel
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
        SignUpScreen()
    }
}

fun NavController.navigateToSignUpScreen() {
    navigate(SignUpNavKey)
}