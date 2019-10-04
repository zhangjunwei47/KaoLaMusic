package com.kaola.lib_player.core.listener



interface IPlayerInitCompleteListener {
    /**
     * * 播放时初始化完毕回调
     * *
     * * @param flag true初始化成功 false初始化失败
     */
    fun onPlayerInitComplete(flag: Boolean)
}
