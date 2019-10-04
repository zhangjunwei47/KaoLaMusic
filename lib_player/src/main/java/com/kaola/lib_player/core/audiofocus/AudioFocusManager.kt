package com.kaola.lib_player.core.audiofocus

import android.content.Context
import android.media.AudioManager
import com.kaola.lib_player.core.model.BaseAudioFocus

class AudioFocusManager(context: Context) : BaseAudioFocus() {
    var mAudioManager: AudioManager =
        context.getSystemService(Context.AUDIO_SERVICE) as AudioManager


    /**
     * 请求焦点
     *
     * @param listener
     */
    override fun requestAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager?.requestAudioFocus(
            listener,
            AudioManager.STREAM_MUSIC,
            AudioManager.AUDIOFOCUS_GAIN
        )
    }


    override fun abandonAudioFocus(): Boolean {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED == mAudioManager?.abandonAudioFocus(listener)
    }


    val listener =
        AudioManager.OnAudioFocusChangeListener { focusChange -> notifyAudioFocusChange(focusChange) }
}