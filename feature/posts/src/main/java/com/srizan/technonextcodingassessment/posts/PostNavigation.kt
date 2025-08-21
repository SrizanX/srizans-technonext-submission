package com.srizan.technonextcodingassessment.posts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PostNavKey

// Nav2
fun NavGraphBuilder.postGraph() {
    composable<PostNavKey> {
        val viewModel: PostViewModel = hiltViewModel()
        val poss by viewModel.posts.collectAsStateWithLifecycle(initialValue = emptyList())
        PostScreen(posts = poss )
    }
}

fun NavController.navigateToPostScreen(navOptions: NavOptions? = null) {
    navigate(PostNavKey, navOptions)
}