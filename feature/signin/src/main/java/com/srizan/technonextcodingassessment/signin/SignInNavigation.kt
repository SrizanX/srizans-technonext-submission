package com.srizan.technonextcodingassessment.signin

import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.srizan.technonextcodingassessment.ui.HandleEvent
import kotlinx.serialization.Serializable

@Serializable
object SignInNavKey

fun NavGraphBuilder.signInGraph(
    navigateToSignUpScreen: () -> Unit,
    navigateToPostsScreen: () -> Unit,
) {
    composable<SignInNavKey> {
        val viewModel: SignInViewModel = hiltViewModel()
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
            onSignInClick = viewModel::signIn,
            onSignUpClick = navigateToSignUpScreen,
        )
    }
}