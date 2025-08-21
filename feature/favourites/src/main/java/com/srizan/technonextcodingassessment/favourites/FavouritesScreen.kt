package com.srizan.technonextcodingassessment.favourites

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srizan.technonextcodingassessment.model.Post

@Composable
fun FavouritesScreen(modifier: Modifier = Modifier) {

    LazyColumn(
        modifier = modifier
    ) {
        items(10) { index ->
            PostItem(
                post = Post(
                    id = index,
                    userId = 0,
                    title = "Post Title $index",
                    body = "This is the body of post number $index"
                ), modifier = Modifier
            )
        }
    }
}