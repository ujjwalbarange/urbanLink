package com.nagpur.connect.ui.screens.report

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale

enum class SpeechRecognitionStatus {
    IDLE,
    LISTENING,
    PROCESSING,
    ERROR
}

class SpeechToTextManager(private val context: Context) {
    private var speechRecognizer: SpeechRecognizer? = null

    private val _status = MutableStateFlow(SpeechRecognitionStatus.IDLE)
    val status: StateFlow<SpeechRecognitionStatus> = _status.asStateFlow()

    private val _transcript = MutableStateFlow("")
    val transcript: StateFlow<String> = _transcript.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(createListener())
            }
        }
    }

    fun startListening(languageCode: String = "en-IN") {
        if (speechRecognizer == null) {
            _errorMessage.value = "Speech recognition is not available on this device."
            return
        }

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
        }

        try {
            _status.value = SpeechRecognitionStatus.LISTENING
            _errorMessage.value = null
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _status.value = SpeechRecognitionStatus.ERROR
            _errorMessage.value = e.message
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
            _status.value = SpeechRecognitionStatus.IDLE
        } catch (e: Exception) {
            // Ignore
        }
    }

    fun destroy() {
        try {
            speechRecognizer?.destroy()
            speechRecognizer = null
        } catch (e: Exception) {
            // Ignore
        }
    }

    private fun createListener(): RecognitionListener {
        return object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {
                _status.value = SpeechRecognitionStatus.LISTENING
            }

            override fun onBeginningOfSpeech() {
                _status.value = SpeechRecognitionStatus.LISTENING
            }

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(buffer: ByteArray?) {}

            override fun onEndOfSpeech() {
                _status.value = SpeechRecognitionStatus.PROCESSING
            }

            override fun onError(error: Int) {
                _status.value = SpeechRecognitionStatus.ERROR
                _errorMessage.value = "Recognition error code: $error"
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    _transcript.value = matches[0]
                }
                _status.value = SpeechRecognitionStatus.IDLE
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    _transcript.value = matches[0]
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }
}
