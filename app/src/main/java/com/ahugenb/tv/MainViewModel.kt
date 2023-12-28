package com.ahugenb.tv

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.File
import java.io.FileInputStream

class MainViewModel(private val audioRecorder: AudioRecorder) : ViewModel() {
    private val _recordingState = MutableStateFlow(RecordingState(isRecording = false, buttonColor = Color.Black))
    val recordingState: StateFlow<RecordingState> = _recordingState.asStateFlow()
    private var mediaPlayer: MediaPlayer? = null

    fun startRecording(fileUri: Uri) {
        audioRecorder.startRecording(fileUri)
        _recordingState.value = RecordingState(isRecording = true, buttonColor = Color.White)
    }

    fun stopRecording() {
        audioRecorder.stopRecording()
        _recordingState.value = RecordingState(isRecording = false, buttonColor = Color.Black)
    }

    fun playAudio(fileUri: Uri) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer().apply {
                val file = File(fileUri.path)
                if (!file.exists()) {
                    file.createNewFile()
                }

                val fis = FileInputStream(file)
                setDataSource(fis.fd)
                prepare()
                start()
                setOnCompletionListener {
                    it.release()
                }
                fis.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle exceptions such as IOException, IllegalArgumentException, SecurityException, or IllegalStateException
        }
    }
}

data class RecordingState(val isRecording: Boolean, val buttonColor: Color)
