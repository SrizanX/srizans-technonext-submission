package com.srizan.technonextcodingassessment.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srizan.technonextcodingassessment.model.Post

@Composable
fun PostItem(
    post: Post, onFavouriteClick: (Post) -> Unit, modifier: Modifier = Modifier
) {
    ListItem(
        leadingContent = {
            Text(text = post.id.toString())
        }, headlineContent = {
            Text(
                text = post.title, maxLines = 2, style = MaterialTheme.typography.titleLarge
            )
        }, supportingContent = {
            Text(
                text = post.body,
                maxLines = 3,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
        }, trailingContent = {
            IconButton(onClick = { onFavouriteClick(post) }) {
                Icon(
                    imageVector = if (post.isFavourite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                    contentDescription = if (post.isFavourite) "Remove from favorites" else "Add to favorites",
                    tint = if (post.isFavourite) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }, modifier = modifier.padding(vertical = 8.dp)
    )
}