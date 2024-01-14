package com.ahugenb.tv.shoutscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahugenb.tv.R
import com.ahugenb.tv.ShoutItem

@Composable
fun ShoutItemRow(
    item: ShoutItem,
    shoutPlayerState: ShoutPlayerState,
    onPlayClicked: () -> Unit,
    onEditClicked: (ShoutItem) -> Unit,
    onDeleteClicked: (ShoutItem) -> Unit
) {
    // Observing the states for recomposition
    val playerState = shoutPlayerState.playerState.value
    val isCurrentItemPlaying = playerState.currentlyPlayingFilepath == item.filePath && playerState.isPlaying

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp) // Fixed height for LazyColumn Performance
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = item.displayName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        IconButton(onClick = onPlayClicked) {
            Icon(
                imageVector = if (isCurrentItemPlaying)
                    ImageVector.vectorResource(id = R.drawable.ic_stop_24)
                else
                    Icons.Default.PlayArrow,
                contentDescription = "Play/Pause"
            )
        }

        IconButton(onClick = {
            onEditClicked(item)
        }) {
            Icon(Icons.Default.Edit, contentDescription = "Edit")
        }

        IconButton(onClick = {
            onDeleteClicked(item)
        }) {
            Icon(Icons.Default.Delete, contentDescription = "Delete")
        }
    }
    Spacer(
        modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(color = Color.White, shape = RectangleShape)
    )
}