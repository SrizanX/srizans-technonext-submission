package com.srizan.technonextcodingassessment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.navigation.AppNavHost
import com.srizan.technonextcodingassessment.navigation.Destination
import com.srizan.technonextcodingassessment.signin.SignInNavKey
import com.srizan.technonextcodingassessment.signup.SignUpNavKey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    AppTheme {
        val navController = rememberNavController()
        val currentBackStackEntry by navController.currentBackStackEntryAsState()
        currentBackStackEntry?.destination
        val startDestination = Destination.POSTS
        val start = if (true) SignInNavKey else SignInNavKey
        var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
        Scaffold(topBar = {
            AnimatedVisibility(
                visible = shouldShowAppBar(currentBackStackEntry?.destination),
                modifier = Modifier,
                enter = slideInVertically { it },
                exit = slideOutVertically { it }) {
                TopAppBar(title = { Text("Posts") })
            }
        }, bottomBar = {
            AnimatedVisibility(
                visible = shouldShowAppBar(currentBackStackEntry?.destination),
                modifier = Modifier,
                enter = slideInVertically { it },
                exit = slideOutVertically {
                    it
                }) {
                NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                    Destination.entries.forEachIndexed { index, destination ->
                        NavigationBarItem(selected = selectedDestination == index, onClick = {
                            navController.navigate(route = destination.navKey)
                            selectedDestination = index
                        }, icon = {
                            Icon(
                                destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        }, label = { Text(destination.label) })
                    }
                }
            }

        }) { contentPadding ->
            AppNavHost(
                navController,
                startDestination = start,
                modifier = Modifier.padding(contentPadding),
            )
        }
    }
}


@Composable
fun shouldShowAppBar(currentDestination: NavDestination?): Boolean {
    val signUpScreen = currentDestination?.hasRoute(SignUpNavKey::class) ?: false
    val signInScreen = currentDestination?.hasRoute(SignInNavKey::class) ?: false
    return signUpScreen.or(signInScreen).not()
}