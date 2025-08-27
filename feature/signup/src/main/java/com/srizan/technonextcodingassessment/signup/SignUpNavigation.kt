package com.srizan.technonextcodingassessment.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object SignUpNavKey

fun NavGraphBuilder.signUpGraph(
    navigateToPostsScreen: () -> Unit,
    popBack: () -> Unit = { }
) {
    composable<SignUpNavKey> {
        SignUpScreen(
            onRegisterSuccess = navigateToPostsScreen,
            onNavigateToSignIn = popBack
        )
    }
}

fun NavController.navigateToSignUpScreen() {
    navigate(SignUpNavKey)
}