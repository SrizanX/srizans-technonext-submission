package com.srizan.technonextcodingassessment

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.navigation.AppNavHost
import com.srizan.technonextcodingassessment.navigation.BottomNavigationBar
import com.srizan.technonextcodingassessment.posts.PostNavKey
import com.srizan.technonextcodingassessment.signin.SignInNavKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(
    isUserLoggedIn: () -> Flow<Boolean?>
) {
    var isLoading by remember { mutableStateOf(true) }
    var isLoggedIn by remember { mutableStateOf<Boolean?>(null) }

    // Collect the login state
    LaunchedEffect(Unit) {
        isUserLoggedIn().collect { loggedIn ->
            isLoggedIn = loggedIn
            // Wait at least 2 seconds to simulate smooth splash
            delay(1000)
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LinearProgressIndicator()
        }
    } else {
        AppTheme {
            val navController = rememberNavController()
            val start = if (isLoggedIn == true) PostNavKey else SignInNavKey
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) },
            ) { contentPadding ->
                AppNavHost(
                    navController,
                    startDestination = start,
                    modifier = Modifier.padding(contentPadding),
                )
            }
        }
    }
}