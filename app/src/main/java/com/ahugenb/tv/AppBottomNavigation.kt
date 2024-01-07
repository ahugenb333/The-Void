package com.ahugenb.tv

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomNavigation(currentScreen: Screen, onItemSelected: (Screen) -> Unit) {
    BottomAppBar {
        BottomNavigationItem(
            label = "The Void",
            isSelected = currentScreen == Screen.Void,
            onClick = { onItemSelected(Screen.Void) }
        )
        Spacer(Modifier.weight(1f, true))
        BottomNavigationItem(
            label = "Shouts",
            isSelected = currentScreen == Screen.Shouts,
            onClick = { onItemSelected(Screen.Shouts) }
        )
    }
}

@Composable
fun BottomNavigationItem(label: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clickable(onClick = onClick)
            .background(if (isSelected) Color.Black else Color.Transparent)
            .size(60.dp) // Adjust size as needed
            .padding(8.dp)
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.Black else Color.White
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(Color.White, CircleShape)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

enum class Screen {
    Void, Shouts
}