package com.kaola.kaolamusic

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.kaola.kaolamusic.model.HomeActivityAdapter
import com.kaola.kaolamusic.util.Constant
import com.kaola.lib_common_ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView

class HomeActivity : BaseActivity() {

    val CHANNELS: IntArray =
        intArrayOf(Constant.MY_CHANNEL, Constant.DOWNLOAD_CHANNEL, Constant.DISTORY_CHANNEL)
    lateinit var view_pager: ViewPager
    lateinit var homeAdapter: HomeActivityAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        view_pager = findViewById(R.id.view_pager)
        initMagicIndicator()
        homeAdapter = HomeActivityAdapter(supportFragmentManager, CHANNELS)
        view_pager.adapter = homeAdapter
    }


    fun initMagicIndicator() {
        magic_indicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(this@HomeActivity)
                //simplePagerTitleView.text = CHANNELS[index] + ""
                simplePagerTitleView.textSize = 19f
                simplePagerTitleView.setOnClickListener {
                    view_pager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getCount(): Int {
                return CHANNELS.size
            }
 
            override fun getIndicator(context: Context?): IPagerIndicator? {
                return null
            }
        }
        magic_indicator.navigator = commonNavigator
        ViewPagerHelper.bind(magic_indicator, view_pager)
    }

}
