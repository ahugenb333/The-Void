package com.ahugenb.tv.shoutscreen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
    // Remember the item that's currently being considered for deletion
    val shoutToDelete = remember { mutableStateOf<ShoutItem?>(null) }

    // Edit dialog logic
    val toEdit = shoutToEdit.value
    if (toEdit != null) {
        EditShoutItemDialog(
            shoutItem = toEdit,
            existingShoutItems = shoutItems,
            onDismiss = { shoutToEdit.value = null },
            onRenameCompleted = { newItem ->
                shoutPlayerState.stopAndPrepareNewPlayer()
                shoutItemListener.onEditCompleted(newItem)
                shoutToEdit.value = null
            }
        )
    }
    // Confirm delete dialog logic
    val toDelete = shoutToDelete.value
    if (toDelete != null) {
        ConfirmDeleteDialog(
            shoutItem = toDelete,
            onDismiss = { shoutToDelete.value = null },
            onDeleteConfirmed = { item ->
                shoutItemListener.onDeleteCompleted(item)
                shoutToDelete.value = null
            }
        )
    }


    LazyColumn {
        items(shoutItems, key = { item -> item.uuid }) { shoutItem ->
            ShoutItemRow(
                item = shoutItem,
                shoutPlayerState = shoutPlayerState,
                onPlayClicked = {
                    shoutPlayerState.playFile(shoutItem.filePath)
                },
                onEditClicked = { shoutToEdit.value = shoutItem },
                onDeleteClicked = { shoutToDelete.value = shoutItem }
            )
        }
    }
}