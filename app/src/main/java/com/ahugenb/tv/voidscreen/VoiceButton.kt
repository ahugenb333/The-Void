package com.ahugenb.tv.voidscreen

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.os.Environment
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.File
import java.util.UUID

@Composable
fun VoiceButton(isAudioPlaying: MutableState<Boolean>) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }

    // Remember the last recorded file path
    var lastRecordedFilePath by rememberSaveable { mutableStateOf("") }

    FloatingActionButton(
        onClick = {},
        interactionSource = interactionSource,
        containerColor = when {
            isPressed -> Color.Red
            isAudioPlaying.value -> Color.Green
            else -> Color.White
        },
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
                //stop any playing audio
                mediaPlayer.value?.let { mp ->
                    if (mp.isPlaying) {
                        mp.stop()
                    }
                    mp.release()
                    mediaPlayer.value = null
                }
                //start recording
                val audioFile = getAudioFilePath(context)
                lastRecordedFilePath = audioFile.absolutePath
                mediaRecorder = startRecording(context, audioFile)
            } else {
                //stop recording
                mediaRecorder?.let {
                    stopRecording(it)
                    mediaRecorder = null
                    //start playback
                    mediaPlayer.value =
                        playRecording(context, File(lastRecordedFilePath), OnCompletionListener { mp ->
                            mp.release()
                            isAudioPlaying.value = false
                            mediaPlayer.value = null
                        })
                    isAudioPlaying.value = true
                }
            }
        }
    }
}


private fun startRecording(context: Context, audioFile: File): MediaRecorder {
    return MediaRecorder(context).apply {
        setAudioSource(MediaRecorder.AudioSource.MIC)
        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        setOutputFile(audioFile.absolutePath)
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

private fun playRecording(
    context: Context,
    audioFile: File,
    onCompletionListener: OnCompletionListener
): MediaPlayer {
    return MediaPlayer().apply {
        try {
            setDataSource(audioFile.absolutePath)
            prepare()
            start()
            setOnCompletionListener(onCompletionListener)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions
        }
    }
}

private fun getAudioFilePath(context: Context): File {
    val recordingsDirectory = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")
    if (!recordingsDirectory.exists()) {
        recordingsDirectory.mkdirs()
    }

    val uuid = UUID.randomUUID().toString()
    val newFileName = "MyAudioRecording_$uuid.mp3"

    return File(recordingsDirectory, newFileName)
}