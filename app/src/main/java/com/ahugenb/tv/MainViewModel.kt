package com.ahugenb.tv

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    fun onNavigationItemSelected(screen: Screen) {
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }
}

data class MainUiState(
    val currentScreen: Screen = Screen.Void
)