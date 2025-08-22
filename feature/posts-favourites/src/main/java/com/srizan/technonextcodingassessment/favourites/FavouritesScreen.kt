package com.srizan.technonextcodingassessment.favourites

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.ui.PostList

@Composable
fun FavouritesScreen(
    postUiState: FavouritePostsUiState,
    onFavouriteClick: (Post) -> Unit,
    modifier: Modifier = Modifier
) {

    if (postUiState.posts.isNotEmpty()) PostList(
        postUiState.posts, onFavouriteClick = onFavouriteClick
    )
    else Text("Empty")
}