package com.ahugenb.tv.shoutscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import com.ahugenb.tv.ShoutItem
import com.ahugenb.tv.ShoutItemListener
import com.ahugenb.tv.ShoutItemUiState

@Composable
fun ShoutScreen(
    shoutItemsState: List<ShoutItemUiState>,
    shoutItemListener: ShoutItemListener
) {
    LazyColumn {
        items(shoutItemsState.size) { index ->
            // Retrieve the item at the current index
            val uiState = shoutItemsState[index]
            RecordingItem(uiState, shoutItemListener)
        }
    }
}