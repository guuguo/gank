package com.guuguo.gank.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.MenuItem
import android.widget.TextView
import com.guuguo.android.lib.app.LBaseFragment
import com.guuguo.android.lib.utils.LogUtil

import com.guuguo.gank.R
import com.guuguo.gank.ui.base.BaseActivity
import com.guuguo.gank.ui.fragment.GankCategoryFragment
import com.guuguo.gank.ui.fragment.GankDailyFragment
import com.guuguo.gank.util.ActivityHelper
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private val STATE_FRAGMENT_SHOW = "STATE_FRAGMENT_SHOW"

    private var mTextMessage: TextView? = null
    override fun getHeaderTitle(): String {
        return "gank"
    }

    override fun getMenuResId(): Int {
        return R.menu.main_menu
    }


    override fun getLayoutResId(): Int {
        return R.layout.activity_main
    }

    override fun getTintSystemBarColor(): Int {
        return ContextCompat.getColor(activity, R.color.colorPrimary)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        when (item.itemId) {
            R.id.navigation_daily -> {
                ViewCompat.setElevation(appbar,10f)
//                appbar.elevation=10f
                switchContent(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_category -> {
                ViewCompat.setElevation(appbar,0f)
//                appbar.elevation=0f
                switchContent(1)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun initAboutInstanceStat(savedInstanceState: Bundle?) {
        super.initAboutInstanceStat(savedInstanceState)

        if (savedInstanceState == null) {
            mFragments.add(GankDailyFragment())
            mFragments.add(GankCategoryFragment())

        } else {
            val manager = supportFragmentManager
            val saveName = savedInstanceState.getString(STATE_FRAGMENT_SHOW)
            currentFragment = null
            if (!TextUtils.isEmpty(saveName))
                currentFragment = manager.findFragmentByTag(saveName)

            LogUtil.i(manager.fragments.size.toString() + "")
            restoreAddFragment(manager, GankDailyFragment::class.java)
            restoreAddFragment(manager, GankCategoryFragment::class.java)
        }
    }

    private fun restoreAddFragment(manager: FragmentManager, clazz: Class<*>) {
        val fragment = manager.findFragmentByTag(clazz.name) as LBaseFragment?
        if (fragment != null)
            mFragments.add(fragment)
        else {
            try {
                mFragments.add(clazz.newInstance() as LBaseFragment)
            } catch (e: InstantiationException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.menu_check_up -> {
                Beta.checkUpgrade()
                return true
            }
            android.R.id.home -> {
                return true
            }
            R.id.menu_search -> {
                val intent = Intent(activity, SearchActivity::class.java)
                startActivity(intent);
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun initView() {
        super.initView()
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        toolbar.navigationIcon=ContextCompat.getDrawable(activity,R.drawable.ic_launcher_xml)
        switchContent(0)
    }

    private var currentFragment: Fragment? = null

    /**
     * 切换fragment
     * @param toPosition
     */
    fun switchContent(toPosition: Int) {
        val to = mFragments[toPosition]
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        if (currentFragment != null)
            transaction.hide(currentFragment)
        if (!to.isAdded)
            transaction.add(R.id.content, to, to.javaClass.name) // 隐藏当前的fragment，add下一个到Activity中
        transaction.show(to).commitAllowingStateLoss()
        currentFragment = to
    }
}
