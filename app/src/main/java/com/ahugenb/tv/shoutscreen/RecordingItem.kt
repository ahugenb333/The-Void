package com.ahugenb.tv.shoutscreen

import android.media.MediaPlayer
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahugenb.tv.ShoutItemUiState
import com.ahugenb.tv.ShoutItemListener

@Composable
fun RecordingItem(
    uiState: ShoutItemUiState,
    shoutItemListener: ShoutItemListener
) {
    val mediaPlayer = remember { MediaPlayer().apply {
        setOnCompletionListener { shoutItemListener.onPlaybackComplete(uiState.shoutItem) }
    } }

    DisposableEffect(key1 = mediaPlayer) {
        onDispose {
            mediaPlayer.release()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = uiState.shoutItem.fileName, modifier = Modifier.weight(1f))

        IconButton(onClick = {
            if (uiState.isPlaying) {
                mediaPlayer.stop()
                shoutItemListener.onPlaybackComplete(uiState.shoutItem)
            } else {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(uiState.shoutItem.filePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                shoutItemListener.onPlayClicked(uiState.shoutItem)
            }
        }) {
            Icon(
                imageVector = if (uiState.isPlaying) Icons.Default.Star else Icons.Default.PlayArrow,
                contentDescription = if (uiState.isPlaying) "Stop" else "Play"
            )
        }

        IconButton(onClick = { shoutItemListener.onEditClicked(uiState.shoutItem) }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }

        IconButton(onClick = { shoutItemListener.onDeleteClicked(uiState.shoutItem) }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
}