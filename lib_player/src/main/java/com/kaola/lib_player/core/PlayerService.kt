package com.kaola.lib_player.core

import android.app.Service
import android.content.Intent
import android.os.IBinder

class PlayerService : Service()
{
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}