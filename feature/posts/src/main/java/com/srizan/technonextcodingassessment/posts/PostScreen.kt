package com.srizan.technonextcodingassessment.posts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srizan.technonextcodingassessment.model.Post

@Composable
fun PostScreen(posts: List<Post>, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
    ) {
        items(posts) { post ->
            PostItem(
                post = Post(
                    id = post.id,
                    userId = 0,
                    title = "Post Title $post",
                    body = "This is the body of post number $post"
                ), modifier = Modifier
            )
        }
    }
}


