package com.srizan.technonextcodingassessment.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

object NavUtils {
    fun shouldShowBottomBar(destination: NavDestination?): Boolean {
        return Destination.entries.any { destination?.hasRoute(it.navKey::class) == true }
    }
}