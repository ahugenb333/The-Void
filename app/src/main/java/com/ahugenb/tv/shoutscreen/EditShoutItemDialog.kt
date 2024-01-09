package com.ahugenb.tv.shoutscreen

import android.content.Context
import android.os.Environment
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
                .fillMaxWidth()
        ) {
            BasicTextField(
                textStyle = TextStyle(color = Color.White),
                value = editedName.value,
                onValueChange = { it: String ->
                    editedName.value = it
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Trim and sanitize the edited name
                    var finalName = editedName.value.trim()
                    if (finalName.isEmpty()) {
                        finalName = "Untitled Shout"
                    }

                    // Check if the name exists among shout items
                    var newName = finalName
                    var fileCount = 1
                    val existingNames = existingShoutItems.map { it.displayName }
                    while (existingNames.contains(newName)) {
                        fileCount++
                        newName = "$finalName ($fileCount)"
                    }

                    // Update the shout item's name and filename
                    val updatedItem = shoutItem.copy(displayName = newName, fileName = newName.plus(".mp3"))
                    saveUpdatedItem(context, shoutItem.fileName, updatedItem)
                    onRenameCompleted(updatedItem)
                }
            ) {
                Text(text = "Save")
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
fun saveUpdatedItem(context: Context, originalFileName: String, updatedItem: ShoutItem) {
    val recordingsDirectory = File(context.getExternalFilesDir(Environment.DIRECTORY_MUSIC), "Recordings")

    // Path to the original file
    val originalFile = File(recordingsDirectory, originalFileName) // assuming file extension is .mp3

    // Path to the new file
    val newFile = File(recordingsDirectory, updatedItem.fileName)

    // Rename the file if it exists
    if (originalFile.exists()) {
        originalFile.renameTo(newFile)
    }
}