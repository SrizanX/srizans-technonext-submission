package com.srizan.technonextcodingassessment.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination
    val startDestination = Destination.POSTS
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }

    AnimatedVisibility(
        visible = NavUtils.shouldShowBottomBar(currentDestination),
        modifier = modifier,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
    ) {
        NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
            Destination.entries.forEachIndexed { index, destination ->
                NavigationBarItem(
                    selected = selectedDestination == index,
                    onClick = {
                        navController.navigate(
                            route = destination.navKey, navController.getTopLevelNavOptions()
                        )
                        selectedDestination = index
                    },
                    icon = {
                        Icon(
                            destination.icon, contentDescription = destination.contentDescription
                        )
                    },
                    label = { Text(destination.label) },
                )
            }
        }
    }
}