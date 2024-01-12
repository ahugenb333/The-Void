package com.ahugenb.tv.voidscreen

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaRecorder
import android.os.Environment
import android.util.Log
import android.widget.Toast
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
fun VoiceButton(
    isAudioPlaying: MutableState<Boolean>,
) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var isRecording by remember { mutableStateOf(false) }
    val mediaPlayer = remember { mutableStateOf<MediaPlayer?>(null) }
    var lastRecordedFilePath by rememberSaveable { mutableStateOf("") }
    var recordingStartTime by remember { mutableStateOf(0L) }

    FloatingActionButton(
        onClick = { /* Implement if needed for a short tap */ },
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
    }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            isRecording = true
            mediaPlayer.value?.stopAndRelease()
            mediaPlayer.value = null
            val audioFile = getAudioFilePath(context)
            lastRecordedFilePath = audioFile.absolutePath
            recordingStartTime = System.currentTimeMillis()
            mediaRecorder = startRecording(context, audioFile)
        } else if (isRecording) {
            isRecording = false
            val recordingDuration = System.currentTimeMillis() - recordingStartTime
            mediaRecorder?.safeStopRecording()
            mediaRecorder = null

            if (recordingDuration >= 1000) {  // Check if recording duration is at least 1 second
                mediaPlayer.value = playRecording(File(lastRecordedFilePath), onCompletionListener = { mp ->
                    mp.stopAndRelease()
                    isAudioPlaying.value = false
                }).apply {
                    setOnErrorListener { _, _, _ ->
                        stopAndRelease()
                        isAudioPlaying.value = false
                        true // Error was handled
                    }
                }
                isAudioPlaying.value = true
            } else {
                isAudioPlaying.value = false
                File(lastRecordedFilePath).delete()  // Delete the short recording
                Toast.makeText(
                    context,
                    "Please shout for at least 1 second!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}

// MediaPlayer extension function
private fun MediaPlayer.stopAndRelease() {
    try {
        if (isPlaying) {
            stop()
        }
    } catch (e: IllegalStateException) {
        // Log the exception or handle it as necessary.
        // This can happen if MediaPlayer is in an incorrect state.
    } finally {
        try {
            release()
        } catch (e: IllegalStateException) {
            // Log the exception or handle it as necessary.
        }
    }
}

// MediaRecorder extension function
private fun MediaRecorder?.safeStopRecording() {
    this?.apply {
        try {
            stop()
        } catch (e: IllegalStateException) {
            // Handle exception for stop() called in an invalid state
        } catch (e: RuntimeException) {
            // Handle runtime exception during stop()
        } finally {
            reset()
            release()
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

private fun playRecording(
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
    val recordingsDirectory =
        File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")
    if (!recordingsDirectory.exists()) {
        recordingsDirectory.mkdirs()
    }

    val uuid = UUID.randomUUID().toString()
    val newFileName = "MyAudioRecording_uuid=$uuid.mp3"

    return File(recordingsDirectory, newFileName)
}