package com.ahugenb.tv

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun AppBottomNavigation(currentScreen: Screen, onNavigationItemSelected: (Screen) -> Unit) {
    BottomAppBar(
        containerColor = Color.White,
        contentPadding = PaddingValues(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavigationItem(
                label = "The Void",
                isSelected = currentScreen == Screen.Void,
                onClick = { onNavigationItemSelected(Screen.Void) },
                modifier = Modifier.weight(1f)
            )
            Spacer(
                modifier = Modifier
                    .width(2.dp)
                    .fillMaxHeight()
                    .background(color = Color.Black)
            )
            BottomNavigationItem(
                label = "Shouts",
                isSelected = currentScreen == Screen.Shouts,
                onClick = { onNavigationItemSelected(Screen.Shouts) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BottomNavigationItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentWidth(align = Alignment.CenterHorizontally)
            .background(if (isSelected) Color.Black else Color.Transparent, CircleShape)
            .padding(8.dp)
            .clickable(onClick = onClick) // Remove the indication here
    ) {
        Text(
            text = label,
            color = if (isSelected) Color.White else Color.Black
        )
    }
}

enum class Screen {
    Void, Shouts
}