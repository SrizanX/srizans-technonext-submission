package com.srizan.technonextcodingassessment.profile

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object ProfileNavKey

fun NavGraphBuilder.profileGraph() {
    composable<ProfileNavKey> {
        ProfileScreen()
    }
}