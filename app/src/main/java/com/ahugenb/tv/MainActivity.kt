package com.ahugenb.tv

import android.Manifest
import android.os.Bundle
import android.os.Environment
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ahugenb.tv.shoutscreen.ShoutScreen
import com.ahugenb.tv.ui.theme.TheVoidTheme
import com.ahugenb.tv.voidscreen.VoidScreen
import java.io.File

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MainViewModel = viewModel()
            val mainState = viewModel.mainState.collectAsStateWithLifecycle().value

            if (mainState.currentScreen == Screen.Shouts) {
                val recordingsDirectory = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")
                viewModel.updateShoutItems(loadShoutsFromDirectory(recordingsDirectory))
            }

            TheVoidTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
                    VoidAppScreen(
                        currentScreen = mainState.currentScreen,
                        onNavigationItemSelected = viewModel::onNavigationItemSelected,
                        shoutItemsState = viewModel.shoutItems.collectAsStateWithLifecycle().value,
                        onShoutItemClicked = viewModel::onShoutItemClicked
                    )
                }
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    private fun loadShoutsFromDirectory(directory: File): List<ShoutItemUiState> {
        val audioFiles = directory.listFiles()?.filter { it.isFile && it.canRead() } ?: emptyList()
        return audioFiles.map { file ->
            ShoutItemUiState(
                shoutItem = ShoutItem(fileName = file.name, filePath = file.absolutePath),
                isPlaying = false
            )
        }
    }
}

@Composable
fun VoidAppScreen(
    currentScreen: Screen,
    onNavigationItemSelected: (Screen) -> Unit,
    shoutItemsState: List<ShoutItemUiState>,
    onShoutItemClicked: (ShoutItem) -> Unit
) {
    Scaffold(
        bottomBar = { AppBottomNavigation(currentScreen, onNavigationItemSelected) }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                Screen.Void -> VoidScreen()
                Screen.Shouts -> ShoutScreen(shoutItemsState, onShoutItemClicked)
            }
        }
    }
}