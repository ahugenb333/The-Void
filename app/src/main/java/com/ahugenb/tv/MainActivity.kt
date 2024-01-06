package com.ahugenb.tv

import android.Manifest
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.ahugenb.tv.ui.theme.TheVoidTheme

class MainActivity : ComponentActivity() {
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TheVoidTheme {
                val configuration = LocalConfiguration.current
                val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // Set the background color to black
                ) {
                    //button color state will persist through orientation change
                    val isAudioPlaying = rememberSaveable { mutableStateOf(false) }
                    VoidContent(isAudioPlaying, isLandscape)
                }
            }
        }
        requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }
}

@Composable
fun VoidContent(isAudioPlaying: MutableState<Boolean>, isLandscape: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.weight(if (isLandscape) 1f else 0.8f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            AnimationLoader(isAudioPlaying)
        }
        Row(
            modifier = Modifier.weight(if (isLandscape) 1f else 0.2f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            VoiceButton(isAudioPlaying)
        }
    }
}