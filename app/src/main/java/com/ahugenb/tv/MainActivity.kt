package com.ahugenb.tv

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.ahugenb.tv.ui.theme.TheVoidTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            // You can use this to update a state if you need to react to the permission result
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheVoidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.Black)
                            .fillMaxWidth()  // Fills the width of the parent
                            .fillMaxHeight() // Fills the height of the parent
                            .padding(32.dp), // Optional padding
                        contentAlignment = Alignment.BottomCenter // Centers the FAB inside the Box
                    ) {
                        VoiceButton()
                    }
                }
            }
        }

        // Requesting RECORD_AUDIO permission
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}