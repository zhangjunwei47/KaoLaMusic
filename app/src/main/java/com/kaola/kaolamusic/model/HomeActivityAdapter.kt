package com.kaola.kaolamusic.model

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.kaola.kaolamusic.fragment.MyFragment
import com.kaola.kaolamusic.util.Constant
import kotlin.math.absoluteValue

class HomeActivityAdapter constructor(fragmentManager: FragmentManager, list: IntArray) :
    FragmentPagerAdapter(fragmentManager) {
    var mTitleList: IntArray

    init {
        this.mTitleList = list
    }

    override fun getItem(position: Int): Fragment {
        var type = mTitleList[position].absoluteValue
        return when (type) {
            Constant.MY_CHANNEL -> MyFragment.newInstance()
            Constant.DISTORY_CHANNEL -> MyFragment.newInstance()
            else -> MyFragment.newInstance()
        }
    }

    override fun getCount(): Int {
        return mTitleList.size
    }

}