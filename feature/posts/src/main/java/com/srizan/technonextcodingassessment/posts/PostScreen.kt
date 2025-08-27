package com.srizan.technonextcodingassessment.posts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.ui.AppAlertDialog
import com.srizan.technonextcodingassessment.ui.FullScreenLoading
import com.srizan.technonextcodingassessment.ui.PostItem
import kotlinx.coroutines.delay

/**
 * Internal composable that integrates with ViewModel and handles state management
 */
@Composable
internal fun PostScreen(
    modifier: Modifier = Modifier
) {
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
        modifier = modifier
    )
}

/**
 * Public composable that accepts UI state and callbacks - for testing and previews
 */

/**
 * Main screen for displaying posts with search, refresh, and management capabilities.
 * 
 * @param postUiState Current UI state containing loading/error states and search query
 * @param posts Paginated list of posts
 * @param onQueryChange Callback for search query changes
 * @param onRefresh Callback for refresh action
 * @param onFavouriteClick Callback for favorite toggle
 * @param onDeleteAllClick Callback for delete all posts
 * @param onSignOut Callback for sign out
 * @param onErrorDismiss Callback for error dismissal
 * @param modifier Modifier for the composable
 */

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
    onErrorDismiss: () -> Unit = {},
    onDataLoaded: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    var isSearchEnabled by remember { mutableStateOf(false) }
    var showSignOutConfirmationDialog by remember { mutableStateOf(false) }
    var showDeleteAllPostDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    // Handle error messages
    LaunchedEffect(postUiState.errorMessage) {
        postUiState.errorMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onErrorDismiss()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("PostScreen")
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
            
            PostContent(
                postUiState = postUiState,
                posts = posts,
                onRefresh = onRefresh,
                onFavouriteClick = onFavouriteClick,
                onDataLoaded = onDataLoaded,
                onQueryChange = onQueryChange,
                modifier = Modifier.weight(1f)
            )
        }

        // Error handling with Snackbar
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) { snackbarData ->
            Snackbar(
                snackbarData = snackbarData,
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        }

        // Confirmation dialogs
        if (showSignOutConfirmationDialog) {
            AppAlertDialog(
                onDismissRequest = { showSignOutConfirmationDialog = false },
                onConfirmation = {
                    showSignOutConfirmationDialog = false
                    onSignOut()
                },
                dialogTitle = "Confirm Sign Out",
                dialogText = "Are you sure you want to sign out?",
                icon = Icons.Default.Warning,
            )
        }

        if (showDeleteAllPostDialog) {
            AppAlertDialog(
                onDismissRequest = { showDeleteAllPostDialog = false },
                onConfirmation = {
                    showDeleteAllPostDialog = false
                    onDeleteAllClick()
                },
                dialogTitle = "Confirm Delete All Posts",
                dialogText = "Are you sure you want to delete all posts? This action cannot be undone.",
                icon = Icons.Default.Warning,
            )
        }
    }
}

/**
 * Content section of the PostScreen that handles different states.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PostContent(
    postUiState: PostViewModel.UiState,
    posts: LazyPagingItems<Post>,
    onRefresh: () -> Unit,
    onFavouriteClick: (Post) -> Unit,
    onDataLoaded: () -> Unit,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isRefreshLoading = posts.loadState.refresh is LoadState.Loading
    val isRefreshNotLoading = posts.loadState.refresh is LoadState.NotLoading
    val hasItems = posts.itemCount > 0
    val isRefreshError = posts.loadState.refresh is LoadState.Error

    // Handle initial data loading completion with a small delay to prevent flicker
    LaunchedEffect(isRefreshNotLoading, hasItems, isRefreshError) {
        if (postUiState.isInitialLoading && (isRefreshNotLoading || hasItems || isRefreshError)) {
            // Small delay to ensure UI stability
            delay(50)
            onDataLoaded()
        }
    }

    when {
        // Don't show anything until we've checked cache and given paging time to load
        !postUiState.hasCheckedCache -> {
            FullScreenLoading()
        }
        // Show initial loading when we're loading for first time and no cached data
        postUiState.isInitialLoading && !hasItems && (isRefreshLoading || (!isRefreshNotLoading && !isRefreshError)) -> {
            FullScreenLoading()
        }
        // Show manual refresh loading when user triggered refresh and we have no items
        postUiState.isRefreshing && !hasItems && !postUiState.isInitialLoading -> {
            FullScreenLoading()
        }
        // Show error state when load failed and we have no items and we're not in initial loading
        isRefreshError && !hasItems && !postUiState.isInitialLoading -> {
            ErrorState(
                message = (posts.loadState.refresh as LoadState.Error).error.message 
                    ?: "Failed to load posts",
                onRetry = { posts.retry() },
                modifier = modifier
            )
        }
        // Show empty state only when we're definitely done loading and have no data
        !postUiState.isInitialLoading && !hasItems && isRefreshNotLoading && postUiState.hasCheckedCache -> {
            if (!postUiState.searchQuery.isNullOrBlank()) {
                NoSearchResultsUi(
                    searchQuery = postUiState.searchQuery,
                    onClearSearch = { onQueryChange("") },
                    modifier = modifier
                )
            } else {
                EmptyDataUi(
                    onRefreshClick = onRefresh, 
                    modifier = modifier
                )
            }
        }
        // Show posts list - prioritize showing content when available
        hasItems || (!postUiState.isInitialLoading && isRefreshNotLoading) -> {
            PullToRefreshBox(
                isRefreshing = postUiState.isRefreshing, 
                onRefresh = onRefresh,
                modifier = modifier
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("PostsList")
                ) {
                    items(posts.itemCount) { index ->
                        posts[index]?.let { post ->
                            PostItem(
                                post = post, 
                                onFavouriteClick = onFavouriteClick,
                                modifier = Modifier.testTag("PostItem_${post.id}")
                            )
                        }
                    }
                    
                    // Handle pagination loading
                    if (posts.loadState.append is LoadState.Loading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.semantics {
                                        contentDescription = "Loading more posts"
                                    }
                                )
                            }
                        }
                    }
                    
                    // Handle pagination error
                    if (posts.loadState.append is LoadState.Error) {
                        item {
                            PaginationErrorItem(
                                onRetry = { posts.retry() }
                            )
                        }
                    }
                }
            }
        }
        // Fallback to loading
        else -> {
            FullScreenLoading()
        }
    }
}

/**
 * App bar for the Posts screen with search and menu functionality.
 */
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("SearchField"),
                    placeholder = { Text("Search posts…") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(
                            onClick = onSearchToggle,
                            modifier = Modifier.semantics {
                                contentDescription = "Close search"
                            }
                        ) {
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
            if (!isSearchEnabled) {
                IconButton(
                    onClick = onSearchToggle,
                    modifier = Modifier.semantics {
                        contentDescription = "Search posts"
                    }
                ) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            }
            IconButton(
                onClick = { showMenu = !showMenu },
                modifier = Modifier.semantics {
                    contentDescription = "More options"
                }
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(
                expanded = showMenu, 
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Delete All Posts") }, 
                    onClick = {
                        onDeleteAllClick()
                        showMenu = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Logout") }, 
                    onClick = {
                        onSignOutClick()
                        showMenu = false
                    }
                )
            }
        },
    )
}

/**
 * Error state component shown when posts fail to load.
 */
@Composable
fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            text = "Something went wrong",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedButton(
            onClick = onRetry,
            modifier = Modifier.testTag("RetryButton")
        ) {
            Text("Retry")
        }
    }
}

/**
 * Error item shown at the bottom of the list when pagination fails.
 */
@Composable
fun PaginationErrorItem(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Failed to load more posts",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextButton(
            onClick = onRetry,
            modifier = Modifier.testTag("PaginationRetryButton")
        ) {
            Text("Retry")
        }
    }
}

/**
 * Empty state component shown when no posts are available.
 */
@Composable
fun EmptyDataUi(
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            "No posts yet", 
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "Try refreshing to fetch the latest posts.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedButton(
            onClick = onRefreshClick,
            modifier = Modifier.testTag("RefreshButton")
        ) {
            Text("Refresh")
        }
    }
}

/**
 * No search results component shown when search query returns no results.
 */
@Composable
fun NoSearchResultsUi(
    searchQuery: String,
    onClearSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Text(
            "No posts found", 
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            "No posts match your search for \"$searchQuery\"",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        OutlinedButton(
            onClick = onClearSearch,
            modifier = Modifier.testTag("ClearSearchButton")
        ) {
            Text("Clear Search")
        }
    }
}

@Preview(showBackground = true, name = "Empty Posts State")
@Composable
private fun EmptyDataUiPreview() {
    AppTheme {
        EmptyDataUi(
            onRefreshClick = {}
        )
    }
}

@Preview(showBackground = true, name = "No Search Results State")
@Composable
private fun NoSearchResultsUiPreview() {
    AppTheme {
        NoSearchResultsUi(
            searchQuery = "android development",
            onClearSearch = {}
        )
    }
}