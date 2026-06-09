package com.example.cryptotrack.presentation.widgets

import com.example.cryptotrack.R

sealed class BottomNavComponents (
    val route: String,
    val title: String,
    val icon: Int
) {
    data object Market: BottomNavComponents(
        route = "Market",
        title = "Market",
        icon = R.drawable.ic_graph
    )
    data object Purchase: BottomNavComponents(
        route = "Purchase",
        title = "Portfolio",
        icon = R.drawable.ic_wallet,
    )
    data object Search: BottomNavComponents(
        route = "Search",
        title = "Search",
        icon = R.drawable.ic_search_navigation
    )
    data object Favorite: BottomNavComponents(
        route = "Favorites",
        title = "Favorite",
        icon = R.drawable.ic_star
    )
    data object Profile: BottomNavComponents(
        route = "Profile",
        title = "Profile",
        icon = R.drawable.ic_profile
    )
}