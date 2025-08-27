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
    val popUpToDestinationId = popUpToRoute?.let { route ->
        try {
            // Handle string routes for backward compatibility
            if (route is String) {
                this.graph.findNode(route)?.id ?: currentGraphStartDestinationId
            } else {
                // For serializable objects, use graph start as safe fallback
                // In most authentication flows, clearing to start is the desired behavior
                currentGraphStartDestinationId
            }
        } catch (e: IllegalArgumentException) {
            // Fallback to graph start if route resolution fails
            currentGraphStartDestinationId
        }
    } ?: currentGraphStartDestinationId


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