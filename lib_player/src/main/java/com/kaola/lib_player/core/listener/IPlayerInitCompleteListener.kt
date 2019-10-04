package com.kaola.lib_player.core.listener


/**
 * @author zhangchao on 2019-09-09.
 */

interface IPlayerInitCompleteListener {
    /**
     * * 播放时初始化完毕回调
     * *
     * * @param flag true初始化成功 false初始化失败
     */
    fun onPlayerInitComplete(flag: Boolean)
}
