package com.ahugenb.tv.shoutscreen

import android.content.Context
import android.os.Environment
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ahugenb.tv.ShoutItem
import java.io.File

@Composable
fun EditShoutItemDialog(
    shoutItem: ShoutItem,
    existingShoutItems: List<ShoutItem>,
    onDismiss: () -> Unit,
    onRenameCompleted: (ShoutItem) -> Unit,
) {
    val editedName = remember { mutableStateOf(shoutItem.displayName) }
    val context = LocalContext.current

    CustomDialog(
        onDismiss = { onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(8.dp), // Rounded corners
                border = BorderStroke(1.dp, Color.White) // White border
            ) {
                BasicTextField(
                    value = editedName.value,
                    onValueChange = { it: String ->
                        editedName.value = it
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    textStyle = TextStyle(color = Color.White)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                    var finalName = editedName.value.trim()
                        if (finalName.isEmpty()) {
                            finalName = "Untitled Shout"
                        }

                        // Exclude the current editing item from the list of existing names
                        val existingNames = existingShoutItems.filter { it.uuid != shoutItem.uuid }
                            .map { it.displayName }
                        var displayName = finalName
                        var fileCount = 1

                        while (existingNames.contains(displayName)) {
                            fileCount++
                            displayName = "$finalName ($fileCount)"
                        }
                        val fileName = displayName.plus(".mp3")

                        val path = saveUpdatedItem(context, shoutItem.fileName, fileName)
                        val updatedItem = shoutItem.copy(
                            displayName = displayName,
                            fileName = fileName,
                            filePath = path
                        )
                        onRenameCompleted(updatedItem)
                    },
                    modifier = Modifier
                        .weight(1f) // Equal weight
                        .padding(8.dp)
                ) {
                    Text(text = "Save")
                }

                // Cancel Button
                Button(
                    onClick = { onDismiss() },
                    modifier = Modifier
                        .weight(1f) // Equal weight
                        .padding(8.dp)
                ) {
                    Text(text = "Cancel")
                }
            }
        }
    }
}

@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium, // You can define your own shape
            color = Color.Black // Set your desired background color
        ) {
            content()
        }
    }
}

fun saveUpdatedItem(context: Context, originalFileName: String, newName: String): String {
    val recordingsDirectory =
        File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")

    // Path to the original file
    val originalFile =
        File(recordingsDirectory, originalFileName) // assuming file extension is .mp3

    // Path to the new file
    val newFile = File(recordingsDirectory, newName)

    // Rename the file if it exists
    if (originalFile.exists()) {
        originalFile.renameTo(newFile)
    }
    return newFile.absolutePath
}