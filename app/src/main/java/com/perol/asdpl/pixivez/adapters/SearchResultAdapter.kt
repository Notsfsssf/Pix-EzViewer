package com.perol.asdpl.pixivez.adapters


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.perol.asdpl.pixivez.R
import com.perol.asdpl.pixivez.services.PxEZApp


class SearchResultAdapter(var context: Context,fm: FragmentManager, private var arrayList: ArrayList<Fragment>) : FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getCount(): Int = arrayList.size


    override fun getItem(position: Int): Fragment = arrayList[position]

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> {
            context.getString(R.string.illust)
        }
        else -> {
            context.getString(R.string.painter)
        }
    }


}