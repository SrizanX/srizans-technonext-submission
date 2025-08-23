package com.srizan.technonextcodingassessment.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.ui.PostList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postUiState: PostUiState,
    onRefresh: () -> Unit,
    onFavouriteClick: (Post) -> Unit,
    onDeleteAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSearchEnabled by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    val filteredPosts = remember(searchQuery, postUiState.posts) {
        if (searchQuery.isBlank()) postUiState.posts
        else postUiState.posts.filter {
            it.title.contains(searchQuery, ignoreCase = true).or(
                it.body.contains(searchQuery, ignoreCase = true)
            )
        }
    }

    Column(modifier = modifier.fillMaxSize()) {
        PostsAppBar(
            isSearchEnabled = isSearchEnabled,
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            onSearchToggle = { isSearchEnabled = !isSearchEnabled },
            onDeleteAllClick = onDeleteAllClick
        )

        when {
            postUiState.isRefreshing && postUiState.posts.isEmpty() -> {
                FullScreenLoading(modifier = Modifier.weight(1f))
            }

            postUiState.posts.isEmpty() -> {
                EmptyDataUi(onRefreshClick = onRefresh, modifier = Modifier.weight(1f))
            }

            else -> {
                PullToRefreshBox(
                    isRefreshing = postUiState.isRefreshing, onRefresh = onRefresh
                ) {
                    PostList(
                        posts = filteredPosts, onFavouriteClick = onFavouriteClick
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsAppBar(
    isSearchEnabled: Boolean,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onDeleteAllClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(title = {
        if (isSearchEnabled) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                placeholder = { Text("Search posts…") },
                singleLine = true,
            )
        } else {
            Text("Posts")
        }
    }, actions = {
        IconButton(onClick = onSearchToggle) {
            Icon(Icons.Default.Search, contentDescription = "Search")
        }
        IconButton(onClick = { showMenu = !showMenu }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More")
        }
        DropdownMenu(
            expanded = showMenu, onDismissRequest = { showMenu = false }) {
            DropdownMenuItem(text = { Text("Delete All Posts") }, onClick = {
                onDeleteAllClick()
                showMenu = false
            })
            DropdownMenuItem(text = { Text("Logout") }, onClick = {
                // Logout logic here
                showMenu = false
            })
        }
    })
}


@Composable
fun EmptyDataUi(
    onRefreshClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("No posts yet", style = MaterialTheme.typography.titleLarge)
        Text("Try refreshing to fetch the latest posts.")
        OutlinedButton(
            onClick = onRefreshClick
        ) {
            Text("Refresh")
        }
    }
}


@Composable
fun FullScreenLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}