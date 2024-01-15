package com.ahugenb.tv.shoutscreen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun CustomDialog(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .border(2.dp, Color.White, RoundedCornerShape(8.dp)),
            shape = MaterialTheme.shapes.medium,
            color = Color.Black
        ) {
            content()
        }
    }
}