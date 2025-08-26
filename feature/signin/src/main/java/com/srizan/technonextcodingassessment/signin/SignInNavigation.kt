package com.srizan.technonextcodingassessment.signin

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
        SignInScreen(
            navigateToSignUpScreen = navigateToSignUpScreen,
            navigateToPostsScreen = navigateToPostsScreen,
        )
    }
}