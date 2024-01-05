package com.ahugenb.tv

import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun VoiceButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value
    val isPlaying = remember { mutableStateOf(false) }
    val backgroundColor =
        if (isPressed)
            Color.Red
        else if (isPlaying.value)
            Color.Green
        else
            Color.White

    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null)}

    var fileUri by remember { mutableStateOf<Uri?>(null) }

    FloatingActionButton(
        onClick = {},
        interactionSource = interactionSource,
        containerColor = backgroundColor,
        modifier = Modifier.size(96.dp)
    ) {
        Text(
            text = "Hold To\r\nShout",
            textAlign = TextAlign.Center
        )
        LaunchedEffect(isPressed) {
            if (isPressed) {
                val mp = mediaPlayer.value
                if (mp != null && mp.isPlaying) {
                    mp.stop()
                    mp.release()
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
                }
                startRecording(mediaRecorder)
            } else {
                mediaRecorder?.let {
                    stopRecording(it)
                    fileUri?.let { uri ->
                        mediaPlayer.value = playRecording(uri, context, onCompletionListener = { mp ->
                            mp.release()
                            isPlaying.value = false
                            mediaPlayer.value = null
                        })
                        isPlaying.value = true
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

fun createFileUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(
            MediaStore.MediaColumns.DISPLAY_NAME,
            "MyAudioRecording_${System.currentTimeMillis()}.mp3"
        )
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Music/Recordings/")
    }

    return context.contentResolver.insert(
        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )!!
}