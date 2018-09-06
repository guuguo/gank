package com.guuguo.gank.app.gank.fragment

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.app.LBaseFragmentSupport

import com.guuguo.gank.R
import com.guuguo.gank.app.gank.activity.AboutActivity
import com.guuguo.gank.base.BaseFragment
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.base_toolbar_common.*
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class HomeFragment : LBaseFragment(), Toolbar.OnMenuItemClickListener {

    val mFragments: ArrayList<LBaseFragment> = arrayListOf()
    var mFragment: LBaseFragment? = null
    override fun isNavigationBack()=false
    companion object {
        fun getInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    private val STATE_FRAGMENT_SHOW = "STATE_FRAGMENT_SHOW"
    private var mTextMessage: TextView? = null
    override fun getHeaderTitle() = "gank"
    override fun getMenuResId() = R.menu.main_menu
    override fun getToolBar() = contentView?.findViewById<Toolbar>(R.id.id_tool_bar)
    override fun getLayoutResId() = R.layout.fragment_main

    override fun setTitle(title: String) {
        id_tool_bar.title = title
    }

    override fun initToolbar() {
        super.initToolbar()
    }
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_daily -> {
                ViewCompat.setElevation(appbar, 10f)
                showHideFragment(mFragments[0], mFragment)
                mFragment = mFragments[0]
                true
            }
            R.id.navigation_category -> {
                ViewCompat.setElevation(appbar, 0f)
                showHideFragment(mFragments[1], mFragment)
                mFragment = mFragments[1]
                true
            }
            R.id.navigation_star -> {
            ViewCompat.setElevation(appbar, 0f)
            showHideFragment(mFragments[2], mFragment)
            mFragment = mFragments[2]
            true
        }
            else -> true
        }
    }

    override fun initVariable(savedInstanceState: Bundle?) {
        super.initVariable(savedInstanceState)
        if (savedInstanceState == null) {
            mFragments.add(GankDailyFragment())
            mFragments.add(GankCategoryFragment())
            mFragments.add(GankCategoryContentFragment.newInstance(GankCategoryContentFragment.GANK_TYPE_STAR))
            loadMultipleRootFragment(R.id.container_view, 0, *mFragments.toTypedArray())
            mFragment = mFragments[0]
        } else {
            mFragments.add(findFragment(GankDailyFragment::class.java))
            mFragments.add(findFragment(GankCategoryFragment::class.java))
            mFragments.add(findFragment(GankCategoryContentFragment::class.java))
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_check_up -> Beta.checkUpgrade()
            R.id.menu_search -> {
                val fragment = findFragment(SearchFragment::class.java)
                if (fragment == null) {
                    popTo(HomeFragment::class.java, false) { start(SearchFragment()) }
                } else {
                   popTo(SearchFragment::class.java, false)
                }
            }
            R.id.menu_about -> AboutActivity.intentTo(activity)
            else -> return false
        }
        return true
    }

    override fun initView() {
        super.initView()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        getToolBar()?.inflateMenu(getMenuResId())
        getToolBar()?.setOnMenuItemClickListener(this)
    }

    private var currentFragment: Fragment? = null
}
