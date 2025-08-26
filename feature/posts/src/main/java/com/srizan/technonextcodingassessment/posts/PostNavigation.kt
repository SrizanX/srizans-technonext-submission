package com.srizan.technonextcodingassessment.posts

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import kotlinx.serialization.Serializable

@Serializable
object PostNavKey

/**
 * Navigation graph for the Posts feature.
 * Sets up the composable route and connects the ViewModel to the UI.
 */
fun NavGraphBuilder.postGraph() {
    composable<PostNavKey> {
        val viewModel: PostViewModel = hiltViewModel()
        val postUiState by viewModel.uiState.collectAsStateWithLifecycle()
        val posts = viewModel.pagedPosts.collectAsLazyPagingItems()
        
        PostScreen(
            postUiState = postUiState,
            posts = posts,
            onQueryChange = viewModel::updateSearchQuery,
            onRefresh = viewModel::refreshPosts,
            onFavouriteClick = viewModel::toggleFavourite,
            onDeleteAllClick = viewModel::deleteAllPosts,
            onSignOut = viewModel::signOut,
            onErrorDismiss = viewModel::clearError,
            onDataLoaded = viewModel::onDataLoaded,
        )
    }
}