package com.kaola.lib_player.core.model

import com.kaola.lib_player.core.listener.IAudioFocusListener


abstract class BaseAudioFocus
{
    lateinit var  mIAudioFocusListener: IAudioFocusListener

    var mFocusState: Int = 0

    /**
     * 请求焦点
     */
    abstract fun requestAudioFocus(): Boolean

    /**
     * 主动放弃音频焦点
     */
    abstract fun abandonAudioFocus(): Boolean

    /**
     * 通知焦点改变
     *
     * @param change
     */
    protected fun notifyAudioFocusChange(state: Int) {
        mFocusState = state
        mIAudioFocusListener?.onFocusChange(state)
    }

}