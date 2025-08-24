package com.srizan.technonextcodingassessment.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
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
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.ui.AppAlertDialog
import com.srizan.technonextcodingassessment.ui.FullScreenLoading
import com.srizan.technonextcodingassessment.ui.PostItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postUiState: PostViewModel.UiState,
    posts: LazyPagingItems<Post>,
    onQueryChange: (String) -> Unit,
    onRefresh: () -> Unit,
    onFavouriteClick: (Post) -> Unit,
    onDeleteAllClick: () -> Unit,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isSearchEnabled by remember { mutableStateOf(false) }
    var showSignOutConfirmationDialog by remember { mutableStateOf(false) }
    var showDeleteAllPostDialog by remember { mutableStateOf(false) }


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            PostsAppBar(
                isSearchEnabled = isSearchEnabled,
                searchQuery = postUiState.searchQuery,
                onSearchQueryChange = { onQueryChange(it) },
                onSearchToggle = {
                    onQueryChange("")
                    isSearchEnabled = !isSearchEnabled
                },
                onDeleteAllClick = {
                    showDeleteAllPostDialog = true
                },
                onSignOutClick = { showSignOutConfirmationDialog = true },
            )
            when {
                postUiState.isRefreshing && posts.itemCount == 0 -> FullScreenLoading()
                posts.itemCount < 1 -> {
                    EmptyDataUi(onRefreshClick = onRefresh, modifier = Modifier.weight(1f))
                }

                else -> {
                    PullToRefreshBox(
                        isRefreshing = postUiState.isRefreshing, onRefresh = onRefresh
                    ) {
                        LazyColumn {
                            items(posts.itemCount) { index ->
                                posts[index]?.let { post ->
                                    PostItem(post = post, onFavouriteClick = onFavouriteClick)
                                }
                            }
                        }
                    }
                }
            }

            if (posts.loadState.append is LoadState.Loading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
        if (showSignOutConfirmationDialog) AppAlertDialog(
            onDismissRequest = { showSignOutConfirmationDialog = false },
            onConfirmation = {
                showSignOutConfirmationDialog = false
                onSignOut()
            },
            dialogTitle = "Confirm Sign Out",
            dialogText = "Are you sure you want to sign out?",
            icon = Icons.Default.Warning,
        )

        if (showDeleteAllPostDialog) AppAlertDialog(
            onDismissRequest = { showDeleteAllPostDialog = false },
            onConfirmation = {
                showDeleteAllPostDialog = false
                onDeleteAllClick()
            },
            dialogTitle = "Confirm Delete All Posts",
            dialogText = "Are you sure you want to delete all posts?",
            icon = Icons.Default.Warning,
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostsAppBar(
    isSearchEnabled: Boolean,
    searchQuery: String?,
    onSearchQueryChange: (String) -> Unit,
    onSearchToggle: () -> Unit,
    onDeleteAllClick: () -> Unit,
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }

    TopAppBar(
        title = {
            if (isSearchEnabled) {
                OutlinedTextField(
                    value = searchQuery ?: "",
                    onValueChange = onSearchQueryChange,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Search posts…") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = onSearchToggle) {
                            Icon(Icons.Default.Clear, contentDescription = "Close search")
                        }
                    },
                )
            } else {
                Text("Posts")
            }
        },
        modifier = modifier,
        actions = {
            if (isSearchEnabled.not()) IconButton(onClick = onSearchToggle) {
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
                    onSignOutClick()
                    showMenu = false
                })
            }
        },
    )
}


@Composable
fun EmptyDataUi(
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
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