package com.kaola.lib_player.core

import com.kaola.lib_player.core.ijk.IJKMediaPlayerStrategyAdapter
import com.kaola.lib_player.core.model.BaseMediaPlayerStrategy


class MediaPlayerStrategyContext(type: Int) {
    companion object {
        const val TYPE_IJK_MEDIA_PLAYER = 1
        const val TYPE_EXO_MEDIA_PLAYER = 2
    }

    lateinit var mediaPlayer: BaseMediaPlayerStrategy

    init {
        if (TYPE_IJK_MEDIA_PLAYER == type) {
            mediaPlayer = IJKMediaPlayerStrategyAdapter()
        }
    }

}