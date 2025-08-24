package com.srizan.technonextcodingassessment.favourites

import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object FavouritesNavKey

// Nav2
fun NavGraphBuilder.favouritesGraph() {
    composable<FavouritesNavKey> {
        val viewModel: FavouritesViewModel = hiltViewModel()
        val favouritePostsUiState by viewModel.posts.collectAsStateWithLifecycle()
        FavouritesScreen(
            posts = favouritePostsUiState,
            onFavouriteClick = viewModel::toggleFavourite,
            onClearAllClick = viewModel::clearAllFavourites,
            modifier = Modifier
        )
    }
}