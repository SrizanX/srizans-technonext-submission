package com.srizan.technonextcodingassessment.posts

import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srizan.technonextcodingassessment.model.Post

@Composable
fun PostItem(post: Post, modifier: Modifier = Modifier) {
    ListItem(
        headlineContent = { Text(post.title) },
        supportingContent = { Text(post.body) },
        modifier = modifier
    )
}