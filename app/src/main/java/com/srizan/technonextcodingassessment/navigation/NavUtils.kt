package com.srizan.technonextcodingassessment.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.navOptions

object NavUtils {

    fun topLevelNavOptions(navcontroller: NavController) = navOptions {
        /**
         * Avoid multiple copies of the same destination
         * when re-selecting the same item */
        launchSingleTop = true

        /**
         * Pop up to the start destination of the graph to
         * avoid building up a large stack of destinations
         * on the back stack as users select items*/
        popUpTo(0) { // 0 = root of the back stack
            inclusive = false
            //saveState = true
        }

        /** Restore state when re-selecting a previously selected item */
        //restoreState = true
    }

    fun shouldShowBottomBar(destination: NavDestination?): Boolean {
        return Destination.entries.any { destination?.hasRoute(it.navKey::class) == true }
    }
}