package com.perol.asdpl.pixivez.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.PagerAdapter
import com.perol.asdpl.pixivez.fragments.PictureMFragment
import com.perol.asdpl.pixivez.fragments.PictureXFragment

class PicturePagerAdapter(fm: FragmentManager, private val fragments: LongArray) : FragmentStatePagerAdapter(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getItem(position: Int): Fragment {

        return PictureXFragment.newInstance(fragments[position])
    }

    override fun getCount(): Int {
        return fragments.size
    }
}
