package com.srizan.technonextcodingassessment.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.srizan.technonextcodingassessment.favourites.FavouritesNavKey
import com.srizan.technonextcodingassessment.posts.PostNavKey
import com.srizan.technonextcodingassessment.profile.ProfileNavKey

enum class Destination(
    val navKey: Any, val label: String, val icon: ImageVector, val contentDescription: String
) {
    POSTS(PostNavKey, "Posts", Icons.Default.Create, "Posts"),
    FAVOURITES(FavouritesNavKey, "Favourites", Icons.Default.Favorite, "Favourites"),
    PROFILE(ProfileNavKey, "Profile", Icons.Default.Person, "Profile");
}