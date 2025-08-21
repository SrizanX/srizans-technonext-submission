package com.srizan.technonextcodingassessment.favourites

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object FavouritesNavKey

// Nav2
fun NavGraphBuilder.favouritesGraph() {
    composable<FavouritesNavKey> {
        val viewModel: FavouritesViewModel = hiltViewModel()
        FavouritesScreen()
    }
}

fun NavController.navigateToFavouritesScreen(navOptions: NavOptions? = null) {
    navigate(FavouritesNavKey, navOptions)
}