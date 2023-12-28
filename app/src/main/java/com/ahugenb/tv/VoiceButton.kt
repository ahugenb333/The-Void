package com.ahugenb.tv

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun VoiceButton(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val recordingState by viewModel.recordingState.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState().value

    var fileUri by remember { mutableStateOf<Uri?>(null) }
    var errorOccurred by remember { mutableStateOf(false) }

    FloatingActionButton(
        onClick = {},
        interactionSource = interactionSource,
        modifier = modifier
            .background(recordingState.buttonColor, CircleShape)
            .size(56.dp)
    ) {
        Text("Record")

        LaunchedEffect(isPressed) {
            if (isPressed) {
                fileUri = createFileUri(context)
                viewModel.startRecording(fileUri!!)
            } else {
                viewModel.stopRecording()
                fileUri?.let { viewModel.playAudio(it) }
            }
        }
    }

    if (errorOccurred) {
        // Handle the error (e.g., show a message to the user)
    }
}

fun createFileUri(context: Context): Uri {
    val contentValues = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, "MyAudioRecording_${System.currentTimeMillis()}.mp3")
        put(MediaStore.MediaColumns.RELATIVE_PATH, "Music/Recordings/")
    }

    return context.contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
        ?: throw IllegalStateException("Failed to create file URI")
}