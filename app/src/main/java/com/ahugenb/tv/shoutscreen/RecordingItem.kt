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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun RecordingItem(recording: ShoutItem) {
    val context = LocalContext.current
    var isPlaying by remember { mutableStateOf(false) }
    val mediaPlayer = remember { MediaPlayer() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = recording.fileName, modifier = Modifier.weight(1f))

        IconButton(onClick = {
            if (isPlaying) {
                mediaPlayer.stop()
                isPlaying = false
            } else {
                mediaPlayer.reset()
                mediaPlayer.setDataSource(recording.filePath)
                mediaPlayer.prepare()
                mediaPlayer.start()
                isPlaying = true
            }
        }) {
            Icon(
                imageVector = if (isPlaying) Icons.Default.Star else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play"
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
            mediaPlayer.release()
        }
    }
}