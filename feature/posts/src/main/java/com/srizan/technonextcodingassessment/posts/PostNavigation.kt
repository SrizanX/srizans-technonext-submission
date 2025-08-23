package com.srizan.technonextcodingassessment.posts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PostNavKey

// Nav2
fun NavGraphBuilder.postGraph() {
    composable<PostNavKey> {
        val viewModel: PostViewModel = hiltViewModel()
        val postUiState by viewModel.posts.collectAsStateWithLifecycle()
        PostScreen(
            postUiState = postUiState,
            onRefresh = viewModel::refreshPosts,
            onFavouriteClick = viewModel::toggleFavourite,
            onDeleteAllClick = viewModel::deleteAllPosts
        )
    }
}