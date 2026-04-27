package com.example.cryptotrack

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.cryptotrack.presentation.viewmodel.CoinGeckoViewModel
import com.example.cryptotrack.ui.theme.CryptoTrackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CryptoTrackTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        innerPadding = innerPadding
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    innerPadding: PaddingValues
) {
    val viewModel: CoinGeckoViewModel = hiltViewModel()
    val state = viewModel.globalMarket.value

    val state2 = viewModel.market.value

    LaunchedEffect(Unit) {
        viewModel.loadGlobalMarket()
        viewModel.loadMarket()
    }

    Log.d("Greeting Screen", "$state")
    Log.d("Greeting Screen", "$state2")

}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    CryptoTrackTheme {
//        Greeting("Android")
//    }
//}