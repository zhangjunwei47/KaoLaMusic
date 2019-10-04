package com.kaola.lib_player.core.ijk

import com.kaola.lib_player.core.listener.IPlayerBufferProgressListener
import com.kaola.lib_player.core.listener.IPlayerInitCompleteListener
import com.kaola.lib_player.core.listener.IPlayerStateCoreListener
import com.kaola.lib_player.core.model.BaseMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.ref.WeakReference

class IJKMediaPlayerAdapter : BaseMediaPlayer() {

    var duration: Long = -1

    var currentPosition: Long = -1

    /**
     * 播放状态
     */
    private var mIPlayerState: IPlayerStateCoreListener? = null

    /**
     * 缓冲回调
     */
    private var mIPlayerBufferProgressListener: IPlayerBufferProgressListener? = null

    /**
     * 播放器初始化成功
     */
    private var mIPlayerInitCompleteListener: IPlayerInitCompleteListener? = null

    /**
     * 正在播放的url
     */
    private var mDataSource: String? = null

    /**
     * ijk mediaplayer
     */
    private var mIjkMediaPlayer: IjkMediaPlayer

    init {
        mIjkMediaPlayer = IjkMediaPlayer()
        mIjkMediaPlayer.ijkPlayerCallBack = IJKCallBack(this)
    }

    override fun play() {
        mIjkMediaPlayer.play()
        notifyPlayerPlaying()
    }

    override fun pause() {
        mIjkMediaPlayer.pause()
        notifyPlayerPaused()
    }


    override fun stop() {
        mIjkMediaPlayer.stop()
    }

    override fun start(url: String) {
        notifyPlayerIdle()
        stop()
        setDataSource(url)
        prepare()
    }

    override fun start(url: String, position: Long) {
        start(url)
        seek(position)
    }

    override fun seek(mSec: Long) {
        mIjkMediaPlayer.seek(mSec)
        notifySeekStart()
    }


    override fun setPlayRatio(ratio: Float) {
    }

    override fun preload(url: String) {
    }

    override fun reset() {
        stop()
        notifyPlayerEnd()
        notifyPlayerIdle()
    }

    override fun release() {
    }

    override fun prepare() {
        prepare(0)
    }

    override fun prepare(needSeek: Int) {
        mIjkMediaPlayer.prepare(needSeek)
        notifyPlayerPreparing()
    }

    override fun setDataSource(url: String) {
        mDataSource = url
        mIjkMediaPlayer.setDataSource(url)
    }

    override fun isPlaying(): Boolean {
        return mIjkMediaPlayer.isPlaying
    }

    override fun setPlayerStateListener(listener: IPlayerStateCoreListener) {
        mIPlayerState = listener
    }

    override fun setBufferProgressListener(listener: IPlayerBufferProgressListener) {
        mIPlayerBufferProgressListener = listener
    }

    override fun setInitPlayerInitCompleteListener(listener: IPlayerInitCompleteListener) {
        mIPlayerInitCompleteListener = listener
    }



    private class IJKCallBack(ijkMediaPlayerAdapter: IJKMediaPlayerAdapter) :
        IjkMediaPlayer.ijkPlayerCallBack {
        private val ijkMediaPlayerAdapterWeakReference: WeakReference<IJKMediaPlayerAdapter> =
            WeakReference(ijkMediaPlayerAdapter)

        override fun message(what: Int, arg1: Int, arg2: Int, obj: Any?) {
            val ijkMediaPlayerAdapter = ijkMediaPlayerAdapterWeakReference.get() ?: return
            when (what) {
                IjkMediaPlayer.MEDIA_PREPARED -> ijkMediaPlayerAdapter.notifyPlayerPlaying()
                IjkMediaPlayer.MEDIA_PLAYBACK_COMPLETE -> ijkMediaPlayerAdapter.notifyPlayerEnd()
                IjkMediaPlayer.MEDIA_SEEK_COMPLETE -> ijkMediaPlayerAdapter.notifySeekComplete()
                IjkMediaPlayer.MEDIA_IJK_SO_INIT_SUCCESS -> ijkMediaPlayerAdapter.notifyInitComplete()
                IjkMediaPlayer.MEDIA_ERROR -> ijkMediaPlayerAdapter.notifyPlayerFailed(arg1, arg2)
                IjkMediaPlayer.MEDIA_PLAYER_VOD_PTS_PRELOAD_UPDATE -> {
                    if (arg1 < 0 || arg2 < 0) {
                        return
                    }
                    ijkMediaPlayerAdapter.notifyBufferProgress(arg1.toLong(), arg2.toLong())
                }
                IjkMediaPlayer.MEDIA_PLAYER_PTS_UPDATE -> {
                    if (arg1 < 0 || arg2 < 0 || (arg2 < arg1)) {
                        ijkMediaPlayerAdapter.notifyPlayerEnd()
                        return
                    }
                    ijkMediaPlayerAdapter.notifyProgress(arg1.toLong(), arg2.toLong())
                }
                IjkMediaPlayer.MEDIA_INFO -> {
                    if (arg1 == tv.danmaku.ijk.media.player.IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
                        ijkMediaPlayerAdapter.notifyBufferingStart()
                    } else if (arg1 == tv.danmaku.ijk.media.player.IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
                        ijkMediaPlayerAdapter.notifyBufferingEnd()
                    }
                }
            }
        }
    }

    private fun notifyPlayerPreparing() {
        mIPlayerState?.onPlayerPreparing(mDataSource)
    }

    private fun notifyPlayerIdle() {
        mIPlayerState?.onIdle(mDataSource)
    }

    fun notifyPlayerPlaying() {
        mIPlayerState?.onPlayerPlaying(mDataSource)
    }

    private fun notifyPlayerPaused() {
        mIPlayerState?.onPlayerPaused(mDataSource)
    }

    private fun notifyProgress(progress: Long, total: Long) {
        mIPlayerState?.onProgress(mDataSource, progress, total)
    }

    private fun notifyPlayerFailed(what: Int, extra: Int) {
        mIPlayerState?.onPlayerFailed(mDataSource, what, extra)
    }

    private fun notifyPlayerEnd() {
        mIPlayerState?.onPlayerEnd(mDataSource)
    }

    private fun notifySeekStart() {
        mIPlayerState?.onSeekStart(mDataSource)
    }

    private fun notifySeekComplete() {
        mIPlayerState?.onSeekComplete(mDataSource)
    }

    private fun notifyBufferingStart() {
        mIPlayerState?.onBufferingStart(mDataSource)
    }

    private fun notifyBufferingEnd() {
        mIPlayerState?.onBufferingEnd(mDataSource)
    }

    private fun notifyBufferProgress(position: Long, total: Long) {
        mIPlayerBufferProgressListener?.onBufferProgress(position, total)
    }

    private fun notifyInitComplete() {
        mIPlayerInitCompleteListener?.onPlayerInitComplete(true)
    }

}