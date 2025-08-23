package com.srizan.technonextcodingassessment.favourites

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import com.srizan.technonextcodingassessment.designsystem.theme.AppTheme
import com.srizan.technonextcodingassessment.model.Post
import com.srizan.technonextcodingassessment.ui.PostList
import kotlin.random.Random

@Composable
fun FavouritesScreen(
    posts: List<Post>,
    onFavouriteClick: (Post) -> Unit,
    onClearAllClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Column {
        FavouritesAppBar(
            onClearAllClick = onClearAllClick,
            onLogoutClick = onLogoutClick,
            modifier = modifier.testTag("AppBar")
        )

        if (posts.isNotEmpty()) Column {
            PostList(
                posts, onFavouriteClick = onFavouriteClick
            )
        }
        else Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                "No Favourites Yet", style = MaterialTheme.typography.titleLarge
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesAppBar(
    onClearAllClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var showMenu by remember { mutableStateOf(false) }
    TopAppBar(
        title = { Text("Favourites") },
        modifier = modifier,
        actions = {
            IconButton(
                onClick = { showMenu = !showMenu }, modifier = Modifier.testTag("MoreButton")
            ) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.testTag("MoreMenu")
            ) {
                DropdownMenuItem(
                    text = { Text("Clear All Favourites") },
                    onClick = {
                        onClearAllClick()
                        showMenu = false
                    },
                    modifier = Modifier.testTag("ClearAllFavouritesMenuItem"),
                )
                DropdownMenuItem(
                    text = { Text("Logout") }, onClick = {
                    showMenu = false
                    onLogoutClick()
                }, modifier = Modifier.testTag("LogoutMenuItem")
                )
            }
        },
    )
}


@Preview(device = "spec:width=411dp,height=891dp")
@Composable
private fun FavouritesScreenPreview() {
    AppTheme {
        Surface {
            FavouritesScreen(
                posts = List(5) {
                    Post(
                        id = it,
                        userId = it,
                        title = "Post Title $it",
                        body = "Post Body $it",
                        isFavourite = Random.nextBoolean()
                    )
                },
                onFavouriteClick = {}, onClearAllClick = {}, onLogoutClick = {},
            )
        }
    }
}