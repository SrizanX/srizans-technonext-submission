package com.srizan.technonextcodingassessment.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srizan.technonextcodingassessment.model.Post

@Composable
fun PostList(
    posts: List<Post>, onFavouriteClick: (Post) -> Unit, modifier: Modifier = Modifier
) = LazyColumn(
    modifier = modifier
) {
    items(posts) { post ->
        PostItem(
            post = post, modifier = Modifier, onFavouriteClick = onFavouriteClick
        )
    }
}