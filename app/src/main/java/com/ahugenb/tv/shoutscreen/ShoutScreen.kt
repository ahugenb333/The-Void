package com.ahugenb.tv.shoutscreen

import android.media.MediaPlayer
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.ahugenb.tv.ShoutItem

@Composable
fun ShoutScreen(
    shoutItems: List<ShoutItem>,
) {
    val mediaPlayer = remember { MediaPlayer() }
    val currentlyPlayingItem = remember { mutableStateOf<ShoutItem?>(null) }

    Column {
        shoutItems.forEach { shoutItem ->
            ShoutItemRow(
                item = shoutItem,
                isPlaying = currentlyPlayingItem.value == shoutItem,
                onPlayClicked = {
                    if (currentlyPlayingItem.value == null) {
                        mediaPlayer.apply {
                            reset()
                            setDataSource(shoutItem.filePath)
                            prepare()
                            start()
                            setOnCompletionListener {
                                // Handle playback completion
                            }
                        }
                        currentlyPlayingItem.value = shoutItem
                    } else {
                        if (currentlyPlayingItem.value == shoutItem) {
                            // If the same item is clicked again, stop playback
                            mediaPlayer.stop()
                            mediaPlayer.reset()
                            currentlyPlayingItem.value = null
                        } else {
                            // If a different item is clicked, stop the current playback
                            mediaPlayer.stop()
                            mediaPlayer.reset()
                            mediaPlayer.apply {
                                setDataSource(shoutItem.filePath)
                                prepare()
                                start()
                                setOnCompletionListener {
                                    // Handle playback completion
                                }
                            }
                            currentlyPlayingItem.value = shoutItem
                        }
                    }
                },
                onEditClicked = {
                    // Handle edit logic
                },
                onDeleteClicked = {
                    // Handle delete logic
                }
            )
        }
    }
}