package com.ahugenb.tv.shoutscreen

import android.media.MediaPlayer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Stable
class ShoutPlayerState(
    private val coroutineScope: CoroutineScope
) {
    private var mediaPlayer: MediaPlayer? = null
    private var _playerState: MutableState<PlayerState> = mutableStateOf(PlayerState())
    val playerState: State<PlayerState> = _playerState

    fun playFile(filePath: String) {
        if (_playerState.value.currentlyPlayingFilepath == filePath) {
            togglePlayback()
            return
        }

        stopAndPrepareNewPlayer()
        coroutineScope.launch {
            mediaPlayer?.apply {
                try {
                    setDataSource(filePath)
                    prepare()
                    start()
                    _playerState.value = PlayerState(filePath, true)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }


    private fun togglePlayback() {
        mediaPlayer?.apply {
            if (_playerState.value.isPlaying) {
                pause()
                _playerState.value = _playerState.value.copy(isPlaying = false)
            } else {
                start()
                _playerState.value = _playerState.value.copy(isPlaying = true)
            }
        }
    }

    fun stopAndPrepareNewPlayer() {
        mediaPlayer?.apply {
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = MediaPlayer().apply {
            setOnCompletionListener {
                stopAndReleasePlayer()
            }
        }
    }

    private fun stopAndReleasePlayer() {
        mediaPlayer?.release()
        mediaPlayer = null
        // Update the combined state to reflect that nothing is playing
        _playerState.value = PlayerState(currentlyPlayingFilepath = null, isPlaying = false)
    }
}

data class PlayerState(
    val currentlyPlayingFilepath: String? = null,
    val isPlaying: Boolean = false
)