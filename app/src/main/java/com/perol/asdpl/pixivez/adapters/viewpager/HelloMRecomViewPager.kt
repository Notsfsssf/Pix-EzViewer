package com.perol.asdpl.pixivez.adapters.viewpager

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import android.view.View
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.HelloM.HelloMRecommandFragment
import com.perol.asdpl.pixivez.fragments.HelloM.HelloRecomUserFragment
import com.perol.asdpl.pixivez.services.PxEZApp

class HelloMRecomViewPager (var activity:Context?,fm: FragmentManager) : FragmentStatePagerAdapter(fm,FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(p0: Int): Fragment =when(p0){
        0->HelloMRecommandFragment.newInstance("","");
        else->HelloRecomUserFragment.newInstance("","");
    }

    override fun getCount()=2

    override fun getPageTitle(position: Int): CharSequence? {

        return when (position) {
            0 ->  activity!!.getString(R.string.illust);
            else ->  activity!!.getString(R.string.painter);
        }

    }
}