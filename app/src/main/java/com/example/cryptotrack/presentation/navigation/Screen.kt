package com.example.cryptotrack.presentation.navigation

sealed interface Screen {

    val route: String

    data object Market : Screen {
        override val route = "Market"
    }

    data object CoinDetails : Screen {
        override val route = "coin_details/{id}"

        fun createRoute(id: String): String {
            return "coin_details/$id"
        }
    }

    data object Search: Screen {
        override val route = "Search"
    }

}