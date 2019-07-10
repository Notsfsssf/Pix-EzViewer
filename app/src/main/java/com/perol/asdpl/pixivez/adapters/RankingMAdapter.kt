package com.perol.asdpl.pixivez.adapters

import android.content.Context
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.fragments.HelloM.RankingMFragment
import com.perol.asdpl.pixivez.services.PxEZApp

class RankingMAdapter(var context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private val modelist = arrayOf("day", "day_male", "day_female", "week_original", "week_rookie", "week", "month", "day_r18","week_r18")
//    private val modelistcn= arrayOf("每日","男性","女性","原创","新人","每周","每月","XVIII","XVIII_Week")
    override fun getCount(): Int =modelist.size
    override fun getItem(position: Int)=
        RankingMFragment.newInstance(modelist[position])

    override fun getPageTitle(position: Int): CharSequence?
        =context.resources.getStringArray(R.array.modellist)[position]

}