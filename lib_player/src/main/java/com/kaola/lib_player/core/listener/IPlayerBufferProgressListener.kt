package com.kaola.lib_player.core.listener

/**
 * @author zhangchao on 2019-06-12.
 */

interface IPlayerBufferProgressListener {
    fun onBufferProgress(downloadSize: Long, totalSize: Long)
}
