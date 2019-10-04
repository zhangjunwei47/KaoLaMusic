package com.kaola.lib_common_ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.kaola.lib_common_ui.util.StatusBarUtil

open class BaseActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        StatusBarUtil.statusBarLightMode(this)
    }

}
