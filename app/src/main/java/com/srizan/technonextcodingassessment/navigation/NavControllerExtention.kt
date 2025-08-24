package com.srizan.technonextcodingassessment.navigation

import androidx.navigation.NavController
import androidx.navigation.navOptions

/**
 * Navigates to a destination, popping up to the start destination of the graph
 * to avoid building up a large back stack. This is typically used for
 * top-level destinations or when clearing a flow (e.g., after authentication).
 */
fun NavController.navigateClearingStack(
    route: Any, // Or a more specific type if all your keys conform to something
    popUpToRoute: Any? = null, // Optional: specify a different route to pop up to
    inclusive: Boolean = true,
    saveState: Boolean = true, // Usually good to save state of popped screens
    launchSingleTop: Boolean = true,
    restoreState: Boolean = true // Usually good to restore state if navigating back
) {
    val currentGraphStartDestinationId = this.graph.startDestinationId
    val popUpToDestinationId = if (popUpToRoute != null) {
        // This is a bit more complex if popUpToRoute is not a NavDestination.id
        // For simplicity, this example assumes popUpToRoute maps to an ID or we stick to graph start
        // If popUpToRoute is a @Serializable object, you might not directly get its ID here
        // without more context or a way to resolve it to an ID within the current graph.
        // For most common cases, popping to graph.findStartDestination().id is what you need.
        // If you need to pop to a specific *route object*, you'd typically pass its ID
        // or let NavOptionsBuilder resolve it if it can.
        // For this reusable function, sticking to graph's start is often safest.
        try {
            // Attempt to find the destination if 'popUpToRoute' is a route string or serializable
            // This is a simplification; robust route-to-ID resolution might be needed.
            if (popUpToRoute is String) this.graph.findNode(popUpToRoute)?.id
                ?: currentGraphStartDestinationId
            else currentGraphStartDestinationId // Fallback or handle based on how you define popUpToRoute
        } catch (e: IllegalArgumentException) {
            currentGraphStartDestinationId // Fallback if route not found
        }
    } else {
        currentGraphStartDestinationId
    }


    val options = navOptions {
        popUpTo(popUpToDestinationId) {
            this.inclusive = inclusive
            this.saveState = saveState
        }
        this.launchSingleTop = launchSingleTop
        this.restoreState = restoreState
    }
    this.navigate(route, options)
}


fun NavController.getTopLevelNavOptions() = navOptions {
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