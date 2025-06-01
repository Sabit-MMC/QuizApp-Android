package com.example.quizapp.tools

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import kotlin.collections.get

class SoundPlayerHelper(context: Context) {

    private val soundPool: SoundPool
    private val soundMap: MutableMap<String, Int> = mutableMapOf()
    private val loadedSounds: MutableSet<Int> = mutableSetOf()
    private var pendingPlay: String? = null

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(audioAttributes)
            .build()

        soundPool.setOnLoadCompleteListener { _, sampleId, status ->
            if (status == 0) {
                loadedSounds.add(sampleId)
                if (pendingPlay != null && soundMap[pendingPlay] == sampleId) {
                    soundPool.play(sampleId, 1f, 1f, 0, 0, 1f)
                    pendingPlay = null
                }
            }
        }

        soundMap["15_seconds_start"] = soundPool.load(context.assets.openFd("sounds/15_seconds_start.mp3"), 1)
        soundMap["5_seconds_left"] = soundPool.load(context.assets.openFd("sounds/5_seconds_left.mp3"), 1)
    }

    fun playSound(name: String) {
        val soundId = soundMap[name]
        if (soundId != null) {
            if (loadedSounds.contains(soundId)) {
                soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
            } else {
                pendingPlay = name
            }
        }
    }

    fun release() {
        soundPool.release()
    }
}