package com.ahugenb.tv.shoutscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.ahugenb.tv.ShoutItem
import com.ahugenb.tv.ShoutItemUiState

@Composable
fun ShoutScreen(
    shoutItemsState: List<ShoutItemUiState>,
    onShoutItemClicked: (ShoutItem) -> Unit
) {
    LazyColumn {
        items(shoutItemsState.size) { index ->
            val shoutItemUiState = shoutItemsState[index]
            RecordingItem(
                uiState = shoutItemUiState,
                onPlayClicked = { onShoutItemClicked(it) }
            )
        }
    }
}