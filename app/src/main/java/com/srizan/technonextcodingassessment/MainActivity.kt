package com.srizan.technonextcodingassessment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.srizan.technonextcodingassessment.designsystem.theme.SrizansTechnoNextSubmissionTheme
import com.srizan.technonextcodingassessment.favourites.FavouritesNavKey
import com.srizan.technonextcodingassessment.navigation.AppNavHost
import com.srizan.technonextcodingassessment.posts.PostNavKey
import com.srizan.technonextcodingassessment.signin.SignInNavKey
import com.srizan.technonextcodingassessment.signup.SignUpNavKey
import dagger.hilt.android.AndroidEntryPoint


enum class Destination(
    val navKey: Any, val label: String, val icon: ImageVector, val contentDescription: String
) {
    POSTS(PostNavKey, "Posts", Icons.Default.Create, "Posts"), FAVOURITES(
        FavouritesNavKey, "Favourites", Icons.Default.Favorite, "Favourites"
    )
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SrizansTechnoNextSubmissionTheme {
                val navController = rememberNavController()

                val currentBackStackEntry by navController.currentBackStackEntryAsState()

                currentBackStackEntry?.destination
                val startDestination = Destination.POSTS
                val start = if (true) SignInNavKey else SignInNavKey
                var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
                Scaffold(
                    bottomBar = {
                        AnimatedVisibility(
                            visible = shouldShowAppBar(currentBackStackEntry?.destination),
                            modifier = Modifier,
                            enter = slideInVertically { it },
                            exit = slideOutVertically {
                                it
                            }
                        ) {
                            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                                Destination.entries.forEachIndexed { index, destination ->
                                    NavigationBarItem(
                                        selected = selectedDestination == index,
                                        onClick = {
                                            navController.navigate(route = destination.navKey)
                                            selectedDestination = index
                                        },
                                        icon = {
                                            Icon(
                                                destination.icon,
                                                contentDescription = destination.contentDescription
                                            )
                                        },
                                        label = { Text(destination.label) })
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
    }
}


@Composable
fun shouldShowAppBar(currentDestination: NavDestination?): Boolean {
    val signUpScreen = currentDestination?.hasRoute(SignUpNavKey::class) ?: false
    val signInScreen = currentDestination?.hasRoute(SignInNavKey::class) ?: false
    return signUpScreen.or(signInScreen).not()
}