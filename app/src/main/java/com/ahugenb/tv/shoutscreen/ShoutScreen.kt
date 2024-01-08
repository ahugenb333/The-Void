package com.ahugenb.tv.shoutscreen

import android.os.Environment
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import java.io.File

@Composable
fun ShoutScreen() {
    val context = LocalContext.current
    val recordings = remember { mutableStateOf(listOf<ShoutItem>()) }

    LaunchedEffect(Unit) {
        val recordingsDirectory = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")
        val audioFiles = recordingsDirectory.listFiles()?.filter { it.isFile && it.canRead() }
        recordings.value = audioFiles?.map {
            if (it.name.contains("uuid")) {
                ShoutItem("Untitled Shout", it.absolutePath)
            } else {
                ShoutItem(it.name, it.absolutePath)
            }
        } ?: emptyList()
    }

    LazyColumn {
        items(recordings.value.size) { index ->
            RecordingItem(recordings.value[index])
        }
    }
}

data class ShoutItem(
    val fileName: String,
    val filePath: String
)