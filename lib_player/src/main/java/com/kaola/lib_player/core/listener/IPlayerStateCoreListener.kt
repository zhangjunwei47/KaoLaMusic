package com.kaola.lib_player.core.listener

/**
 * @author zhangchao on 2019-05-28.
 */

interface IPlayerStateCoreListener {

    fun onIdle(url: String?)

    fun onPlayerPreparing(url: String?)

    fun onPlayerPlaying(url: String?)

    fun onPlayerPaused(url: String?)

    fun onProgress(url: String?, progress: Long, total: Long)

    fun onPlayerFailed(url: String?, what: Int, extra: Int)

    fun onPlayerEnd(url: String?)

    fun onSeekStart(url: String?)

    fun onSeekComplete(url: String?)

    fun onBufferingStart(url: String?)

    fun onBufferingEnd(url: String?)
}
