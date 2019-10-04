package com.kaola.lib_player.core.listener



interface IPlayerBufferProgressListener {
    fun onBufferProgress(downloadSize: Long, totalSize: Long)
}
