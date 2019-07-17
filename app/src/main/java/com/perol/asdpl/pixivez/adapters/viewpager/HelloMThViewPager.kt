package com.perol.asdpl.pixivez.adapters.viewpager

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.HelloM.HelloMMyFragment
import com.perol.asdpl.pixivez.fragments.IllustratorFragment
import com.perol.asdpl.pixivez.services.PxEZApp

class HelloMThViewPager(var context: Context,fm: FragmentManager, var long: Long) : FragmentStatePagerAdapter(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                HelloMMyFragment.newInstance("1", "2")

            }
            else -> {
                IllustratorFragment.newInstance(long, true)
            }

        }
    }

    override fun getPageTitle(position: Int): CharSequence? =
            when (position) {
                0 -> {
                    context.getString(R.string.new1)
                }
                else -> {
                    context.getString(R.string.painter)
                }
            }


    override fun getCount() = 2

}