package com.ahugenb.tv

import android.content.ContentValues
import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
    val backgroundColor = if (isPressed) Color.Red else Color.White

    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var fileUri by remember { mutableStateOf<Uri?>(null) }
    Box(
        modifier = modifier
            .background(Color.Black)
            .fillMaxWidth()  // Fills the width of the parent
            .fillMaxHeight() // Fills the height of the parent
            .padding(32.dp), // Optional padding
        contentAlignment = Alignment.BottomCenter // Centers the FAB inside the Box
    ) {
        FloatingActionButton(
            onClick = {},
            interactionSource = interactionSource,
            containerColor = backgroundColor,
            modifier = Modifier.size(96.dp)
        ) {
            Text(text = "Hold To\r\nShout",
                textAlign = TextAlign.Center
            )
eckout
            LaunchedEffect(isPressed) {
                if (isPressed) {
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
                            playRecording(uri, context)
                        }
                        mediaRecorder = null
                    }
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

private fun playRecording(fileUri: Uri, context: Context) {
    MediaPlayer().apply {
        try {
            setDataSource(context, fileUri)
            prepare()
            start()
            setOnCompletionListener { mp ->
                mp.release()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions
        }
    }
}

fun createFileUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "MyAudioRecording_${System.currentTimeMillis()}.mp3")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Music/Recordings/")
    }

    return context.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)!!
}