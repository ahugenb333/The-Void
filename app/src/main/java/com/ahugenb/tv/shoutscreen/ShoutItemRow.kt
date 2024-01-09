package com.ahugenb.tv.shoutscreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahugenb.tv.ShoutItem

@Composable
fun ShoutItemRow(
    item: ShoutItem,
    isPlaying: Boolean,
    onPlayClicked: () -> Unit,
    onEditClicked: () -> Unit,
    onDeleteClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = item.fileName, modifier = Modifier.weight(1f))

        IconButton(onClick = {
            onPlayClicked()
        }) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Star else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Stop" else "Play"
            )
        }

        IconButton(onClick = {
            onEditClicked()
        }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }

        IconButton(onClick = {
            onDeleteClicked()
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}