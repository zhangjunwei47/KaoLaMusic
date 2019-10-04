package com.kaola.lib_player.core.model

import com.kaola.lib_player.core.listener.IPlayerBufferProgressListener
import com.kaola.lib_player.core.listener.IPlayerInitCompleteListener
import com.kaola.lib_player.core.listener.IPlayerStateCoreListener


/**
 * @author zhangchao on 2019-05-23.
 */

abstract class BaseMediaPlayer {

    abstract fun play()

    abstract fun pause()

    abstract fun stop()

    abstract fun start(url: String)

    abstract fun start(url: String, position: Long)

    abstract fun seek(mSec: Long)

    abstract fun setPlayerStateListener(listener: IPlayerStateCoreListener)

    abstract fun setBufferProgressListener(progressListener: IPlayerBufferProgressListener)

    /**
     * 设置播放器初始化成功回调
     *
     * @param initPlayerInitCompleteListener
     */
    abstract fun setInitPlayerInitCompleteListener(initPlayerInitCompleteListener: IPlayerInitCompleteListener)

    abstract fun setPlayRatio(ratio: Float)

    abstract fun preload(url: String)

    abstract fun reset()

    abstract fun release()

    abstract fun prepare()

    abstract fun prepare(needSeek: Int)

    abstract fun setDataSource(source: String)

    abstract fun isPlaying(): Boolean

    fun setMediaVolume(leftVolume: Float, rightVolume: Float) {

    }

    fun setLoudnessNormalization(active: Int) {

    }

    fun setDuration(urlDuration: Int, totalDuration: Int) {

    }

}
