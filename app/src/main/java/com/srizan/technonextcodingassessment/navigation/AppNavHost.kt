package com.srizan.technonextcodingassessment.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.srizan.technonextcodingassessment.favourites.favouritesGraph
import com.srizan.technonextcodingassessment.posts.PostNavKey
import com.srizan.technonextcodingassessment.posts.postGraph
import com.srizan.technonextcodingassessment.profile.profileGraph
import com.srizan.technonextcodingassessment.signin.signInGraph
import com.srizan.technonextcodingassessment.signup.navigateToSignUpScreen
import com.srizan.technonextcodingassessment.signup.signUpGraph

@Composable
fun AppNavHost(
    navController: NavHostController, startDestination: Any, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = startDestination, modifier = modifier,
    ) {
        signInGraph(
            navigateToSignUpScreen = { navController.navigateToSignUpScreen() },
            navigateToPostsScreen = {
                navController.navigateClearingStack(PostNavKey)
            })
        signUpGraph(navigateToPostsScreen = {
            navController.navigateClearingStack(PostNavKey)
        }, popBack = { navController.popBackStack() })
        postGraph()
        favouritesGraph()
        profileGraph()
    }
}


