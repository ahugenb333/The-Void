package com.ahugenb.tv

import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VoiceButton(isAudioPlaying: MutableState<Boolean>) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val isPlaying = remember { mutableStateOf(false) }

    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    var fileUri by remember { mutableStateOf<Uri?>(null) }

    FloatingActionButton(
        onClick = {},
        interactionSource = interactionSource,
        containerColor = if (isPressed) Color.Red else if (isPlaying.value) Color.Green else Color.White,
        modifier = Modifier.size(128.dp),
        shape = CircleShape
    ) {
        Text(
            text = "Hold To\r\nShout",
            textAlign = TextAlign.Center,
            style = TextStyle(color = Color.Black, fontSize = 24.sp)
        )
        LaunchedEffect(isPressed) {
            if (isPressed) {
                mediaPlayer.value?.let { mp ->
                    if (mp.isPlaying) {
                        mp.stop()
                    }
                    mp.release()
                    mediaPlayer.value = null
                }
                fileUri = createFileUri(context)
                mediaRecorder = MediaRecorder(context).apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    setOutputFile(
                        context.contentResolver.openFileDescriptor(
                            fileUri!!,
                            "w"
                        )?.fileDescriptor
                    )
                    startRecording(this)
                }
            } else {
                mediaRecorder?.let {
                    stopRecording(it)
                    fileUri?.let { uri ->
                        mediaPlayer.value = playRecording(uri, context, onCompletionListener = { mp ->
                            mp.release()
                            isAudioPlaying.value = false
                            isPlaying.value = false
                            mediaPlayer.value = null
                        })
                        isPlaying.value = true
                        isAudioPlaying.value = true
                    }
                    mediaRecorder = null
                }
            }
        }
    }
}


private fun startRecording(mediaRecorder: MediaRecorder?) {
    mediaRecorder?.apply {
        try {
            prepare()
            start()
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exception
        }
    }
}

private fun stopRecording(mediaRecorder: MediaRecorder) {
    try {
        mediaRecorder.stop()
        mediaRecorder.reset()
        mediaRecorder.release()
    } catch (e: Exception) {
        e.printStackTrace()
        // Handle exception
    }
}

private fun playRecording(fileUri: Uri, context: Context, onCompletionListener: OnCompletionListener): MediaPlayer {
    return MediaPlayer().apply {
        try {
            setDataSource(context, fileUri)
            prepare()
            start()
            setOnCompletionListener(onCompletionListener)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions
        }
    }
}

private fun createFileUri(context: Context): Uri {
    val filename = "MyAudioRecording.mp3" // Constant filename
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Music/Recordings/")
    }

    return context.contentResolver.insert(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        contentValues
    ) ?: throw IllegalStateException("Unable to create file Uri")
}