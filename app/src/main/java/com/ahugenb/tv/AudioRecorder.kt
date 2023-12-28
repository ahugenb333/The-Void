package com.ahugenb.tv

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri

class AudioRecorder(private val context: Context) {
    private var mediaRecorder: MediaRecorder? = null
    private var isRecording = false

    fun startRecording(fileUri: Uri) {
        mediaRecorder = MediaRecorder(context).apply {
            try {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                setOutputFile(fileUri.toString())
                prepare()
                start()
                isRecording = true
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle initialization error
            }
        }
    }

    fun stopRecording() {
        if (isRecording) {
            try {
                mediaRecorder?.apply {
                    stop()
                    reset()
                    release()
                }
                isRecording = false
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                // Handle the error
            }
        }
        mediaRecorder = null
    }
}