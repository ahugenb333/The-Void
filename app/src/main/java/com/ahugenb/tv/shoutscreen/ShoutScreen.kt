package com.ahugenb.tv.shoutscreen

import android.media.MediaPlayer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ahugenb.tv.ShoutItem
import com.ahugenb.tv.ShoutItemListener

@Composable
fun ShoutScreen(
    shoutItems: List<ShoutItem>,
    shoutItemListener: ShoutItemListener
) {
    // Initialize ShoutPlayerState
    val shoutPlayerState = rememberShoutPlayerState()

    // Remember the item that's currently being edited
    val shoutToEdit = remember { mutableStateOf<ShoutItem?>(null) }

    // Edit dialog logic
    val toEdit = shoutToEdit.value
    if (toEdit != null) {
        EditShoutItemDialog(
            shoutItem = toEdit,
            existingShoutItems = shoutItems,
            onDismiss = { shoutToEdit.value = null },
            onRenameCompleted = { newItem ->
                shoutItemListener.onEditCompleted(newItem)
                shoutToEdit.value = null
            }
        )
    }

    LazyColumn {
        items(shoutItems.size, key = { index -> shoutItems[index].uuid }) { index ->
            val shoutItem = shoutItems[index]
            ShoutItemRow(
                item = shoutItem,
                shoutPlayerState = shoutPlayerState,
                onPlayClicked = {
                    shoutPlayerState.playFile(shoutItem.filePath)
                },
                onEditClicked = { shoutToEdit.value = shoutItem },
                onDeleteClicked = { shoutItemListener.onDeleteCompleted(shoutItem) }
            )
        }
    }
}