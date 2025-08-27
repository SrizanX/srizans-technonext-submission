package com.srizan.technonextcodingassessment.posts

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PostNavKey

/**
 * Navigation graph for the Posts feature.
 * Sets up the composable route and connects the ViewModel to the UI.
 */
fun NavGraphBuilder.postGraph() {
    composable<PostNavKey> {
        PostScreen()
    }
}