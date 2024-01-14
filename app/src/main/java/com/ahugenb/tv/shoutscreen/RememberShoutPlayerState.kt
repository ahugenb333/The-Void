package com.ahugenb.tv.shoutscreen

import android.media.MediaPlayer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun rememberShoutPlayerState(): ShoutPlayerState {
    val coroutineScope = rememberCoroutineScope()
    return remember {
        ShoutPlayerState(coroutineScope)
    }
}