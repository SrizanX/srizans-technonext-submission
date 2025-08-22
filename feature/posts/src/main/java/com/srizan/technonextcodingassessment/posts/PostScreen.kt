package com.srizan.technonextcodingassessment.posts

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.ui.PostList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostScreen(
    postUiState: PostUiState, onRefresh: () -> Unit, onFavouriteClick: (Post) -> Unit,

    modifier: Modifier = Modifier
) {

    if (postUiState.errorMessage != null) {
        // Show error message
        // For example, you can use a Text composable to display the error
        Text(text = postUiState.errorMessage, modifier = modifier)

    } else {
        PullToRefreshBox(
            isRefreshing = postUiState.isRefreshing, onRefresh = onRefresh, modifier = modifier
        ) {
            PostList(
                posts = postUiState.posts, onFavouriteClick = onFavouriteClick, modifier = modifier
            )
        }
    }
}


