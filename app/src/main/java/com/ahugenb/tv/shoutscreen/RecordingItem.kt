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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ahugenb.tv.ShoutItem
import com.ahugenb.tv.ShoutItemUiState

@Composable
fun RecordingItem(
    uiState: ShoutItemUiState,
    onPlayClicked: (ShoutItem) -> Unit
) {
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = uiState.shoutItem.fileName, modifier = Modifier.weight(1f))

        IconButton(onClick = {
            if (uiState.isPlaying) {
                mediaPlayer.value?.stop()
                // Notify the ViewModel that playback has stopped
                onPlayClicked(uiState.shoutItem)
            } else {
                // Prepare and start the MediaPlayer
                mediaPlayer.value = MediaPlayer().apply {
                    setDataSource(uiState.shoutItem.filePath)
                    prepare()
                    start()
                }
                // Notify the ViewModel that this item is now playing
                onPlayClicked(uiState.shoutItem)
            }
        }) {
            Icon(
                imageVector = if (uiState.isPlaying) Icons.Default.Star else Icons.Default.PlayArrow,
                contentDescription = if (uiState.isPlaying) "Pause" else "Play"
            )
        }

        IconButton(onClick = { /* Implement rename logic */ }) {
            Icon(Icons.Default.Edit, contentDescription = "Rename")
        }

        IconButton(onClick = { /* Implement delete logic */ }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }

    DisposableEffect(key1 = mediaPlayer) {
        onDispose {
            mediaPlayer.value?.release()
        }
    }
}