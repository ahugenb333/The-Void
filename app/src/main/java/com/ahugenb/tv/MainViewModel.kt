package com.ahugenb.tv


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

class MainViewModel : ViewModel(), ShoutItemListener {
    private val _mainState = MutableStateFlow(MainUiState())
    val mainState: StateFlow<MainUiState> = _mainState

    private val _shoutItems = MutableStateFlow<List<ShoutItemUiState>>(listOf())
    val shoutItems: StateFlow<List<ShoutItemUiState>> = _shoutItems

    fun onNavigationItemSelected(screen: Screen) {
        _mainState.value = _mainState.value.copy(currentScreen = screen)
    }

    fun updateShoutItems(newItems: List<ShoutItemUiState>) {
        _shoutItems.value = newItems
    }

    fun loadShoutItems(directory: File) {
        val audioFiles = directory.listFiles()?.filter { it.isFile && it.canRead() } ?: emptyList()
        val shoutItems = audioFiles.map { file ->
            ShoutItemUiState(
                shoutItem = ShoutItem(fileName = file.name, filePath = file.absolutePath),
                isPlaying = false
            )
        }
        _shoutItems.value = shoutItems
    }

    override fun onPlayClicked(shoutItem: ShoutItem) {
        val updatedItems = _shoutItems.value.map { uiState ->
            if (uiState.shoutItem == shoutItem) {
                uiState.copy(isPlaying = !uiState.isPlaying)
            } else {
                uiState.copy(isPlaying = false)
            }
        }
        _shoutItems.value = updatedItems
    }

    override fun onPlaybackComplete(shoutItem: ShoutItem) {
        val updatedItems = _shoutItems.value.map { uiState ->
            if (uiState.shoutItem == shoutItem) {
                uiState.copy(isPlaying = false)
            } else {
                uiState
            }
        }
        _shoutItems.value = updatedItems
    }

    override fun onEditClicked(shoutItem: ShoutItem) {
        // Implement edit logic
    }

    override fun onDeleteClicked(shoutItem: ShoutItem) {
        // Implement delete logic
    }

    fun restorePlayingState() {
        // Identify if any shout item was playing before the orientation change
        _shoutItems.value.find { it.isPlaying }?.let { currentlyPlaying ->
            updateShoutItems(_shoutItems.value.map { uiState ->
                if (uiState.shoutItem == currentlyPlaying.shoutItem) {
                    uiState.copy(isPlaying = true)
                } else {
                    uiState
                }
            })
        }
    }
}

enum class Screen {
    Void, Shouts
}

data class MainUiState(
    val currentScreen: Screen = Screen.Void,
)

data class ShoutItemUiState(
    val shoutItem: ShoutItem,
    val isPlaying: Boolean
)

data class ShoutItem(
    val fileName: String,
    val filePath: String,
)

interface ShoutItemListener {
    fun onPlayClicked(shoutItem: ShoutItem)
    fun onPlaybackComplete(shoutItem: ShoutItem)
    fun onEditClicked(shoutItem: ShoutItem)
    fun onDeleteClicked(shoutItem: ShoutItem)
}