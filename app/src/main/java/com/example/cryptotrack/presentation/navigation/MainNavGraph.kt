package com.example.cryptotrack.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.remember
import androidx.compose.ui.layout.ScaleFactor
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.cryptotrack.presentation.screens.CoinDetailsScreen
import com.example.cryptotrack.presentation.screens.FavoritesScreen
import com.example.cryptotrack.presentation.screens.MarketScreen
import com.example.cryptotrack.presentation.screens.ProfileScreen
import com.example.cryptotrack.presentation.screens.AddPurchaseScreen
import com.example.cryptotrack.presentation.screens.PurchaseHistoryScreen
import com.example.cryptotrack.presentation.screens.PurchaseScreen
import com.example.cryptotrack.presentation.screens.SearchScreen
import com.example.cryptotrack.presentation.screens.SplashScreen
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinViewModel
import com.example.cryptotrack.presentation.viewmodel.UserViewModel


@RequiresApi(Build.VERSION_CODES.S)
fun NavGraphBuilder.MainNavGraph(
    navController: NavController,
) {

    composable(route = Screen.Splash.route) {backStackEntry ->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)

        SplashScreen(
            viewModel = viewModel,
            onMoveToMain = {
                navController.navigate(Screen.Market.route) {
                    popUpTo("Splash") {inclusive = true}
                }
            }
        )
    }

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

    composable(route = Screen.Profile.route) { backStackEntry->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)
        val userViewModel: UserViewModel = hiltViewModel(parentEntry)

        ProfileScreen(
            navController = navController,
            coinViewModel = coinViewModel,
            viewModel = viewModel,
            userViewModel = userViewModel,
        )
    }

    composable(route = Screen.Favorites.route) { backStackEntry->
        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)

        FavoritesScreen(
            navController = navController,
            coinViewModel = coinViewModel,
        )
    }

    composable(route = Screen.AddPurchase.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)

        AddPurchaseScreen(
            navController = navController,
            viewModel = viewModel,
            coinViewModel = coinViewModel,
        )

    }

    composable(route = Screen.Purchase.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)

        PurchaseScreen(
            navController = navController,
            viewModel = viewModel,
            coinViewModel = coinViewModel,
        )

    }

    composable(route = Screen.PurchaseHistory.route) { backStackEntry ->

        val parentEntry = remember(backStackEntry) {
            navController.getBackStackEntry("main_graph")
        }
        val coinViewModel: CoinViewModel = hiltViewModel(parentEntry)
        val viewModel: CoinGeckoViewModel = hiltViewModel(parentEntry)

        PurchaseHistoryScreen(
            navController = navController,
            viewModel = viewModel,
            coinViewModel = coinViewModel,
        )

    }

}