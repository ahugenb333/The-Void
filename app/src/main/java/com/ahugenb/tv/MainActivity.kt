package com.ahugenb.tv

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahugenb.tv.ui.theme.TheVoidTheme
import com.ahugenb.tv.voidscreen.VoidScreen

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheVoidTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    val viewModel: MainViewModel = viewModel()
                    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
                    VoidAppScreen(
                        currentScreen = uiState.currentScreen,
                        onNavigationItemSelected = viewModel::onNavigationItemSelected
                    )
                }
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}

@Composable
fun VoidAppScreen(
    currentScreen: Screen,
    onNavigationItemSelected: (Screen) -> Unit
) {
    Scaffold(
        bottomBar = { AppBottomNavigation(currentScreen, onNavigationItemSelected) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Void -> VoidScreen()
                Screen.Shouts -> LazyColumn {}
            }
        }
    }
}