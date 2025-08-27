package com.srizan.technonextcodingassessment.favourites

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object FavouritesNavKey

fun NavGraphBuilder.favouritesGraph() {
    composable<FavouritesNavKey> {
        FavouritesScreen()
    }
}