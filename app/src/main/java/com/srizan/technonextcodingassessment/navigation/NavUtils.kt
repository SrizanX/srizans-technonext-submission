package com.srizan.technonextcodingassessment.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute

/**
 * Utility object for navigation-related operations.
 * 
 * Provides helper functions for determining UI behavior based on navigation state.
 */
object NavUtils {
    /**
     * Determines whether the bottom navigation bar should be visible for the given destination.
     * 
     * The bottom bar is shown only for main app destinations (Posts, Favourites, Profile)
     * and hidden for authentication screens (Sign In, Sign Up).
     * 
     * @param destination Current navigation destination
     * @return true if bottom bar should be visible, false otherwise
     */
    fun shouldShowBottomBar(destination: NavDestination?): Boolean {
        return Destination.entries.any { destination?.hasRoute(it.navKey::class) == true }
    }
}