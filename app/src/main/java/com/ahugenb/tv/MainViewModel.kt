package com.ahugenb.tv


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.UUID

class MainViewModel : ViewModel(), ShoutItemListener {
    private val _mainState = MutableStateFlow(MainUiState())
    val mainState: StateFlow<MainUiState> = _mainState

    private val _shoutItems = MutableStateFlow<List<ShoutItem>>(listOf())
    val shoutItems: StateFlow<List<ShoutItem>> = _shoutItems

    fun onNavigationItemSelected(screen: Screen) {
        _mainState.value = _mainState.value.copy(currentScreen = screen)
    }

    fun loadShoutItems(directory: File) {
        viewModelScope.launch {
            val shoutItems = withContext(Dispatchers.IO) {
                // Perform file reading and processing in a background thread
                loadShoutsFromDirectory(directory)
            }
            _shoutItems.value = shoutItems
        }
    }

    private fun loadShoutsFromDirectory(directory: File): List<ShoutItem> {
        val audioFiles = directory.listFiles()?.filter { it.isFile && it.canRead() } ?: emptyList()
        return audioFiles.map { file ->
            val name = if (file.name.contains("uuid")) "Untitled Shout" else file.name.removeSuffix(".mp3")
            ShoutItem(displayName = name, fileName = file.name, filePath = file.absolutePath)
        }
    }

    override fun onEditCompleted(newItem: ShoutItem) {
        _shoutItems.value = _shoutItems.value.map { existingItem ->
            if (existingItem.uuid == newItem.uuid) newItem else existingItem
        }
    }

    override fun onDeleteCompleted(shoutItem: ShoutItem) {
        //todo
    }
}

enum class Screen {
    Void, Shouts
}

data class MainUiState(
    val currentScreen: Screen = Screen.Void,
)

data class ShoutItem(
    val displayName: String,
    val fileName: String,
    val filePath: String,
    val uuid: UUID = UUID.randomUUID()
)

interface ShoutItemListener {

    fun onEditCompleted(newItem: ShoutItem)

    fun onDeleteCompleted(shoutItem: ShoutItem)
}