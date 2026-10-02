package com.example.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SoundManager private constructor(context: Context) {

    private val appContext = context.applicationContext
    private val prefs = appContext.getSharedPreferences("guess_sound_prefs", Context.MODE_PRIVATE)

    private val _isSoundEnabled = MutableStateFlow(prefs.getBoolean("sound_enabled", true))
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private val soundPool: SoundPool
    private val soundCorrectId: Int
    private val soundWrongId: Int
    private val soundClickId: Int
    private val soundTimerId: Int
    private val soundVictoryId: Int

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(6)
            .setAudioAttributes(audioAttributes)
            .build()

        soundCorrectId = soundPool.load(appContext, R.raw.snd_correct, 1)
        soundWrongId = soundPool.load(appContext, R.raw.snd_wrong, 1)
        soundClickId = soundPool.load(appContext, R.raw.snd_click, 1)
        soundTimerId = soundPool.load(appContext, R.raw.snd_timer, 1)
        soundVictoryId = soundPool.load(appContext, R.raw.snd_victory, 1)
    }

    fun toggleSound(): Boolean {
        val newValue = !_isSoundEnabled.value
        _isSoundEnabled.value = newValue
        prefs.edit().putBoolean("sound_enabled", newValue).apply()
        if (newValue) {
            playClick()
        }
        return newValue
    }

    fun setSoundEnabled(enabled: Boolean) {
        _isSoundEnabled.value = enabled
        prefs.edit().putBoolean("sound_enabled", enabled).apply()
    }

    fun playCorrect() {
        if (!_isSoundEnabled.value) return
        soundPool.play(soundCorrectId, 1f, 1f, 1, 0, 1f)
    }

    fun playWrong() {
        if (!_isSoundEnabled.value) return
        soundPool.play(soundWrongId, 0.9f, 0.9f, 1, 0, 1f)
    }

    fun playClick() {
        if (!_isSoundEnabled.value) return
        soundPool.play(soundClickId, 0.7f, 0.7f, 0, 0, 1f)
    }

    fun playTimer() {
        if (!_isSoundEnabled.value) return
        soundPool.play(soundTimerId, 0.5f, 0.5f, 0, 0, 1f)
    }

    fun playVictory() {
        if (!_isSoundEnabled.value) return
        soundPool.play(soundVictoryId, 1f, 1f, 2, 0, 1f)
    }

    fun release() {
        soundPool.release()
    }

    companion object {
        @Volatile
        private var instance: SoundManager? = null

        fun getInstance(context: Context): SoundManager {
            return instance ?: synchronized(this) {
                instance ?: SoundManager(context).also { instance = it }
            }
        }
    }
}
