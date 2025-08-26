package com.srizan.technonextcodingassessment.profile

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavKey

fun NavGraphBuilder.profileGraph() {
    composable<ProfileNavKey> {
        val viewModel: ProfileViewModel = hiltViewModel()

        val ab by viewModel.appThemeConfig.collectAsStateWithLifecycle()
        ProfileScreen(
            themeConfig = ab,
            onChangeDarkThemeConfig = viewModel::changeAppThemeConfig,
            onSignOut = viewModel::signOut
        )
    }
}