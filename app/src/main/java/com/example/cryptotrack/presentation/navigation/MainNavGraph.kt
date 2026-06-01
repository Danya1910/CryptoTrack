package com.example.cryptotrack.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cryptotrack.presentation.screens.CoinDetailsScreen
import com.example.cryptotrack.presentation.screens.MarketScreen
import com.example.cryptotrack.presentation.screens.SearchScreen
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel


@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.MainNavGraph(
    navController: NavController,
) {

    composable(route = Screen.Market.route) { backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)

        MarketScreen(
            viewModel = viewModel,
            navController = navController,
            coinViewModel = coinViewModel
        )
    }

    composable(
        route = Screen.CoinDetails.route,
        arguments = listOf(
            navArgument("id") { type = NavType.StringType }
        )
    ) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)
        val id = backStackEntry.arguments?.getString("id") ?: ""

        CoinDetailsScreen(
            coinId = id,
            viewModel = viewModel,
            navController = navController,
            coinViewModel = coinViewModel,
        )
    }

    composable(route = Screen.Search.route) { backStackEntry->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)

        SearchScreen(
            navController = navController,
            viewModel = viewModel,
            coinViewModel = coinViewModel,
        )
    }


}