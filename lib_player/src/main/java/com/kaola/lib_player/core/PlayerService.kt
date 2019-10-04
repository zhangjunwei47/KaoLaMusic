package com.kaola.lib_player.core

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.os.Binder
import android.os.IBinder
import com.kaola.lib_player.core.audiofocus.AudioFocusManager
import com.kaola.lib_player.core.listener.IAudioFocusListener
import com.kaola.lib_player.core.listener.IPlayerBufferProgressListener
import com.kaola.lib_player.core.listener.IPlayerInitCompleteListener
import com.kaola.lib_player.core.listener.IPlayerStateCoreListener
import com.kaola.lib_player.core.model.BaseAudioFocus

class PlayerService : Service() {
    lateinit var mMediaPlayerContext: MediaPlayerStrategyContext

    var mPlayingUri: String = ""

    lateinit var mAudioFocusManager: BaseAudioFocus

    private var mIAudioFocusListener: IAudioFocusListener? = null
    /**
     * 是否失去了音频焦点
     */
    private var isLoseAudioFocus = true

    override fun onBind(intent: Intent?): IBinder? {
        return PlayerServiceBinder()
    }

    override fun onCreate() {
        super.onCreate()
        initContextMediaPlayer()
    }

    private fun initContextMediaPlayer() {
        mMediaPlayerContext =
            MediaPlayerStrategyContext(MediaPlayerStrategyContext.TYPE_IJK_MEDIA_PLAYER)
        mAudioFocusManager = AudioFocusManager(this)
        mAudioFocusManager.mIAudioFocusListener = iAudioFocusListener
    }

    inner class PlayerServiceBinder : Binder() {
        val isPlaying: Boolean
            get() = isPlayingInner()

        @JvmOverloads
        fun start(uri: String, isLocalFile: Boolean = false) {
            startInner(uri, isLocalFile)
        }

        fun setPosition(sec: Long) {
            setPositionInner(sec)
        }

        fun pause() {
            pauseInner()
        }

        fun play() {
            playInner()
        }

        fun stop() {
            stopInner()
        }

        fun reset() {
            resetInner()
        }

        fun release() {
            releaseInner()
        }

        fun seek(mSec: Long) {
            seekInner(mSec)
        }

        fun setPlayerStateListener(listener: IPlayerStateCoreListener) {
            setPlayerStateListenerInner(listener)
        }

        fun setPlayerBufferProgressListener(listener: IPlayerBufferProgressListener) {
            setPlayerBufferProgressListenerInner(listener)
        }

        fun setInitCompleteListener(listener: IPlayerInitCompleteListener) {
            setInitCompleteListenerInner(listener)
        }

        fun setLoudnessNormalization(active: Int) {
            setLoudnessNormalizationInner(active)
        }

        fun requestAudioFocus(): Boolean {
            return requestAudioFocusInner()
        }

        fun abandonAudioFocus() {
            abandonAudioFocusInner()
        }

        fun setCustomAudioFocus(audioFocus: BaseAudioFocus) {
            setCustomAudioFocusInner(audioFocus)
        }

        fun setAudioFocusListener(iAudioFocusListener: IAudioFocusListener) {
            setAudioFocusListenerInner(iAudioFocusListener)
        }
    }

    private fun startInner(uri: String, isLocalFile: Boolean) {
        if (uri == mPlayingUri){
            return
        }
        if (!checkAudioFocus()) {
            return
        }
        mPlayingUri = uri
        mMediaPlayerContext.mediaPlayer.start(uri)
    }

    private fun setPositionInner(sec: Long) {
        mMediaPlayerContext.mediaPlayer.seek(sec)
    }

    private fun pauseInner() {
        mMediaPlayerContext.mediaPlayer.pause()
    }

    private fun playInner() {
        if (!checkAudioFocus()) {
            return
        }
        mMediaPlayerContext.mediaPlayer.play()
    }

    private fun stopInner() {
        mMediaPlayerContext.mediaPlayer.stop()
    }

    private fun resetInner() {
        mMediaPlayerContext.mediaPlayer.reset()
    }

    private fun releaseInner() {
        mMediaPlayerContext.mediaPlayer.release()
    }

    private fun seekInner(mSec: Long) {
        mMediaPlayerContext.mediaPlayer.seek(mSec)
    }

    private fun setPlayerStateListenerInner(iPlayerState: IPlayerStateCoreListener) {
        mMediaPlayerContext.mediaPlayer.setPlayerStateListener(iPlayerState)
    }

    private fun setPlayerBufferProgressListenerInner(listener: IPlayerBufferProgressListener) {
        mMediaPlayerContext.mediaPlayer.setBufferProgressListener(listener)
    }

    private fun setInitCompleteListenerInner(iPlayerInitCompleteListener: IPlayerInitCompleteListener) {
        mMediaPlayerContext.mediaPlayer.setInitPlayerInitCompleteListener(iPlayerInitCompleteListener)
    }

    private fun setLoudnessNormalizationInner(active: Int) {
        mMediaPlayerContext.mediaPlayer.setLoudnessNormalization(active)
    }

    private fun isPlayingInner(): Boolean {
       return mMediaPlayerContext.mediaPlayer.isPlaying()
    }

    private fun setCustomAudioFocusInner(audioFocus: BaseAudioFocus?) {
        if (audioFocus == null) {
            return
        }
        mAudioFocusManager = audioFocus
        mAudioFocusManager?.mIAudioFocusListener = iAudioFocusListener
    }

    private fun setAudioFocusListenerInner(iAudioFocusListener: IAudioFocusListener) {
        mIAudioFocusListener = iAudioFocusListener
    }

    private fun requestAudioFocusInner(): Boolean {
        return mAudioFocusManager.requestAudioFocus()
    }

    private fun abandonAudioFocusInner() {
        mAudioFocusManager.abandonAudioFocus()
    }

    private val iAudioFocusListener = object : IAudioFocusListener {
        override fun onFocusChange(focusChange: Int) {
            isLoseAudioFocus = focusChange !== AudioManager.AUDIOFOCUS_REQUEST_GRANTED
            if (isLoseAudioFocus) {
                pauseInner()
            }
            mIAudioFocusListener?.onFocusChange(focusChange)
        }
    }

    /**
     * 检查音频焦点
     *
     * @return
     */
    private fun checkAudioFocus(): Boolean {
        return !(isLoseAudioFocus && !requestAudioFocusInner())
    }

}