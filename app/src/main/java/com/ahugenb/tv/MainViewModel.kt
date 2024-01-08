package com.ahugenb.tv

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {
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

    fun onShoutItemClicked(clickedItem: ShoutItem) {
        val updatedItems = _shoutItems.value.map { uiState ->
            if (uiState.shoutItem == clickedItem) {
                uiState.copy(isPlaying = !uiState.isPlaying)
            } else {
                uiState.copy(isPlaying = false)
            }
        }
        _shoutItems.value = updatedItems
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
    var isPlaying: Boolean = false
)